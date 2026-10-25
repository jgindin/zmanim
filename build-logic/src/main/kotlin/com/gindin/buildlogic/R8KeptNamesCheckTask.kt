/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

/**
 * Fails the build when a minified variant lost a name that the app's keep rules exist to preserve.
 *
 * A dropped or mistyped keep rule compiles and runs fine until the code that reaches the name at
 * runtime (deserialization, reflection, Enum.valueOf) quietly fails on a user's phone, and the unit
 * and debug on-device tests never see it because they run unminified code. This reads what R8
 * itself reports for the variant instead:
 * - `mapping.txt` lists every class in the output with its new name, and every member whose name
 *   changed. A member whose name R8 left alone is not listed, so the mapping alone cannot tell a
 *   kept member from a removed one.
 * - `seeds.txt` lists every class and member a keep rule matched and R8 kept. That supplies the
 *   "still there" half.
 * - `resources.txt` is the resource shrinker's log; its "Unused resources are:" section lists what
 *   was removed.
 *
 * The expectations file (one per app, next to its proguard-rules.pro) holds one entry per line:
 * - `class <binary name>`: the class must be in the output under its own name.
 * - `member <binary class name>: <member as seeds.txt prints it>`, e.g. `member com.example.Foo:
 *   long serialVersionUID` or `member com.example.Foo: com.example.Foo[] values()`: a keep rule
 *   must have kept the member, and the mapping must not rename it.
 * - `resourcesKept <type>:<name prefix>`, e.g. `resourcesKept string:library_`: no resource of that
 *   type whose name starts with the prefix may be among the resources the shrinker removed. Lines
 *   starting with `#` are comments. The comment block directly above a group of entries says which
 *   keep rule the group protects; it is repeated in the failure message so that whoever broke it
 *   sees why the name matters.
 */
@CacheableTask
abstract class R8KeptNamesCheckTask : DefaultTask() {

  @get:InputFile
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val expectations: RegularFileProperty

  @get:InputFile
  @get:PathSensitive(PathSensitivity.NONE)
  abstract val mappingFile: RegularFileProperty

  @get:InputFile
  @get:PathSensitive(PathSensitivity.NONE)
  abstract val seedsFile: RegularFileProperty

  @get:InputFile
  @get:PathSensitive(PathSensitivity.NONE)
  abstract val resourcesLog: RegularFileProperty

  @get:Input abstract val variantName: Property<String>

  /**
   * Lists what was checked; it exists so that Gradle can skip the check when none of the inputs
   * changed.
   */
  @get:OutputFile abstract val report: RegularFileProperty

  @TaskAction
  fun check() {

    val entries = parseExpectations(expectations.get().asFile)
    val mapping = Mapping.read(mappingFile.get().asFile)
    val seeds = seedsFile.get().asFile.readLines().toHashSet()
    val removedResources by lazy { removedResources(resourcesLog.get().asFile) }

    val failures = entries.mapNotNull { entry ->
      val problem =
        when (entry.kind) {
          "class" -> checkClass(entry.target, mapping)
          "member" -> checkMember(entry.target, mapping, seeds)
          "resourcesKept" -> checkResources(entry.target, removedResources)
          else -> "unknown entry kind '${entry.kind}'"
        }
      problem?.let { entry to it }
    }

    report
      .get()
      .asFile
      .writeText(entries.joinToString("\n", postfix = "\n") { "${it.kind} ${it.target}" })

    if (failures.isNotEmpty()) {
      val byRule =
        failures.groupBy(
          { it.first.reason },
          { "  - ${it.first.kind} ${it.first.target}\n      ${it.second}" },
        )
      throw GradleException(
        "R8 did not preserve ${failures.size} name(s) in the ${variantName.get()} build that the keep rules exist to " +
          "preserve. Check the rule quoted below and the expectations in " +
          "${expectations.get().asFile.let { "${it.parentFile.name}/${it.name}" }}.\n" +
          byRule.entries.joinToString("\n") { (reason, lines) ->
            "\nGuarded by:\n${reason.prependIndent("  | ")}\n${lines.joinToString("\n")}"
          }
      )
    }
  }

  private fun checkClass(className: String, mapping: Mapping): String? {
    val obfuscated =
      mapping.classes[className]
        ?: return "the class is not in the R8 output at all (it was removed)"
    return if (obfuscated == className) null else "R8 renamed the class to '$obfuscated'"
  }

  private fun checkMember(target: String, mapping: Mapping, seeds: Set<String>): String? {

    val className = target.substringBefore(':')
    val member = target.substringAfter(": ")
    return when {
      target !in seeds ->
        "no keep rule kept this member: it is not in seeds.txt, so R8 was free to remove or rename it"
      // A constructor is printed as `SimpleName(args)`, with no return type, and can never be
      // renamed.
      !member.contains(' ') -> null
      else -> checkMemberName(className, member, mapping)
    }
  }

  private fun checkMemberName(className: String, member: String, mapping: Mapping): String? {
    val signature = memberSignature(member)
    val originalName = signature.substringAfter(' ').substringBefore('(')
    val renamedTo =
      mapping.members[className]
        .orEmpty()
        .filter { it.first == signature && it.second != originalName }
        .map { it.second }
        .distinct()
    return if (renamedTo.isEmpty()) null
    else "R8 renamed the member to '${renamedTo.joinToString("', '")}'"
  }

  private fun checkResources(target: String, removed: List<String>): String? {
    val lost = removed.filter { it.startsWith(target) }
    return if (lost.isEmpty()) null
    else
      "the resource shrinker removed ${lost.size}: ${lost.take(MAX_LISTED_RESOURCES).joinToString()}"
  }

  /**
   * The `type:name` of every resource listed in the shrinker log's "Unused resources are:" section.
   */
  private fun removedResources(log: File): List<String> {

    val lines = log.readLines()
    val start = lines.indexOfFirst { it.startsWith("Unused resources are:") }
    if (start < 0) {
      throw GradleException(
        "${log.path} has no 'Unused resources are:' section; is resource shrinking still enabled?"
      )
    }

    return lines
      .drop(start + 1)
      .takeWhile { it.startsWith(" ") }
      .map { it.trim().substringBeforeLast(':') }
  }

  /** One check from the expectations file, with the comment block that explains it. */
  private data class Entry(val kind: String, val target: String, val reason: String)

  private fun parseExpectations(file: File): List<Entry> {

    val entries = mutableListOf<Entry>()
    val comment = mutableListOf<String>()
    var reason = ""
    var lastWasEntry = false

    for (raw in file.readLines()) {
      val line = raw.trim()
      when {
        line.startsWith("#") -> {
          if (lastWasEntry) {
            comment.clear()
          }
          comment += line.removePrefix("#").trim()
          lastWasEntry = false
        }

        // A comment followed by a blank line describes the file, not the entries further down.
        line.isEmpty() -> if (!lastWasEntry) comment.clear()

        else -> {
          if (!lastWasEntry) {
            reason = comment.joinToString("\n")
            comment.clear()
          }
          entries += Entry(line.substringBefore(' '), line.substringAfter(' ').trim(), reason)
          lastWasEntry = true
        }
      }
    }

    if (entries.isEmpty()) {
      throw GradleException("${file.path} lists nothing to check")
    }
    return entries
  }

  /**
   * The parts of `mapping.txt` this check needs: each class's new name, and each listed member's
   * signature (`type name` or `returnType name(args)`) with its new name.
   */
  private class Mapping(
    val classes: Map<String, String>,
    val members: Map<String, List<Pair<String, String>>>,
  ) {

    companion object {

      fun read(file: File): Mapping {

        val classes = HashMap<String, String>()
        val members = HashMap<String, MutableList<Pair<String, String>>>()
        var current: MutableList<Pair<String, String>>? = null

        file.forEachLine { line ->
          when {
            line.startsWith("#") -> Unit

            !line.startsWith(" ") -> {
              val original = line.substringBefore(" -> ")
              classes[original] = line.substringAfter(" -> ").removeSuffix(":")
              current = members.getOrPut(original) { mutableListOf() }
            }

            line.trimStart().startsWith("#") -> Unit

            else -> {
              val signature = memberSignature(line.trim().substringBefore(" -> "))
              // Members inlined from another class are listed under their qualified name; they are
              // not this class's.
              if (!signature.substringAfter(' ').substringBefore('(').contains('.')) {
                current?.add(signature to line.substringAfter(" -> ").trim())
              }
            }
          }
        }

        return Mapping(classes, members)
      }
    }
  }
}

/**
 * How many removed resources a failure message names, so that a prefix matching hundreds stays
 * readable.
 */
private const val MAX_LISTED_RESOURCES = 10

/**
 * Strips the line-number ranges R8 puts around a method in `mapping.txt` (`1:3:void foo():12:14`).
 */
private fun memberSignature(member: String): String =
  member.replace(Regex("^\\d+:\\d+:"), "").replace(Regex("\\):\\d+(:\\d+)?$"), ")")
