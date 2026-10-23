package com.gindin.zmanlib.notification

import com.gindin.zmanlib.zman.Zmanim
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.Calendar

/** Name of the file created to store the notified zmanim. */
private const val NOTIFIED_ZMANIM_FILE_EXTENSION = "notifiedZmanim"

private const val DAYS_TO_KEEP_STATE = 3

/** Keeps track of which zmanim we've already notified the user about for a given day. */
public class NotifiedZmanim : Serializable {

  public class Manager {

    private val notified: MutableList<NotifiedZmanim> = ArrayList()

    private var filesDir: File? = null

    public fun init(filesDir: File) {

      this.filesDir = filesDir
      val files = filesDir.listFiles(FileFilter { pathname -> pathname.name.endsWith(NOTIFIED_ZMANIM_FILE_EXTENSION) })

      if ((files == null) || files.isEmpty()) {
        return
      }

      // Clean up old files. Or read it in if it's not too old.
      val oldestToKeep = Calendar.getInstance()
      oldestToKeep.add(Calendar.DATE, -DAYS_TO_KEEP_STATE)
      val mills = oldestToKeep.timeInMillis

      for (file in files) {
        if (file.lastModified() < mills) {
          file.delete()
        } else {
          restore(file)
        }
      }
    }

    public fun get(forDate: Calendar): NotifiedZmanim {

      val newNotifiedZmanim: NotifiedZmanim

      synchronized(notified) {
        for (notifiedZmanim in notified) {
          if (notifiedZmanim.isForDate(forDate)) {
            return notifiedZmanim
          }
        }

        newNotifiedZmanim = NotifiedZmanim(forDate)
        notified.add(newNotifiedZmanim)
      }

      return newNotifiedZmanim
    }

    // Saving is best effort: failing to write only loses the record of what the user was already told, which is not
    //  worth interrupting a notification for, so any failure is dropped.
    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    public fun save(notifiedZmanim: NotifiedZmanim) {

      val fileName =
        notifiedZmanim.year.toString() +
          "_" +
          notifiedZmanim.month +
          "_" +
          notifiedZmanim.date +
          "." +
          NOTIFIED_ZMANIM_FILE_EXTENSION
      val storedData = File(filesDir, fileName)
      try {
        val oos = ObjectOutputStream(FileOutputStream(storedData, false))
        oos.writeObject(notifiedZmanim)
        oos.flush()
        oos.close()
      } catch (e: Exception) {
        // Urgh. Can't save... We're going to lose our state...
      }
    }

    private fun restore(storedData: File) {

      val restored = if (storedData.canRead()) read(storedData) else NotifiedZmanim()

      synchronized(notified) { notified.add(restored) }
    }

    // A file written by another version of this app, or a damaged one, can fail to deserialize in many ways (including
    //  linkage errors), and the record is only a hint about what the user was already told, so anything unreadable
    //  is replaced by an empty record.
    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun read(storedData: File): NotifiedZmanim {
      return try {
        ObjectInputStream(FileInputStream(storedData)).use { it.readObject() as NotifiedZmanim }
      } catch (e: Throwable) {
        NotifiedZmanim()
      }
    }
  }

  /** The date this applies to. */
  private val year: Int
  private val month: Int
  private val date: Int

  /** Which of the zmanim types have already been notified? */
  private val notifiedTypes: MutableSet<Zmanim.Type> = HashSet()

  public constructor(day: Calendar) {

    this.year = day.get(Calendar.YEAR)
    this.month = day.get(Calendar.MONTH)
    this.date = day.get(Calendar.DATE)
  }

  private constructor() {

    val day = Calendar.getInstance()
    this.year = day.get(Calendar.YEAR)
    this.month = day.get(Calendar.MONTH)
    this.date = day.get(Calendar.DATE)
  }

  private fun isForDate(other: Calendar): Boolean {

    return year == other.get(Calendar.YEAR) && month == other.get(Calendar.MONTH) && date == other.get(Calendar.DATE)
  }

  /**
   * Once the user has been notified about the specified type, then this method is invoked to note that notification.
   */
  public fun notified(type: Zmanim.Type) {

    notifiedTypes.add(type)
  }

  public fun contains(type: Zmanim.Type): Boolean {
    return notifiedTypes.contains(type)
  }

  public companion object {

    @JvmField public val manager: Manager = Manager()

    private const val serialVersionUID: Long = 8982487060417217170L
  }
}
