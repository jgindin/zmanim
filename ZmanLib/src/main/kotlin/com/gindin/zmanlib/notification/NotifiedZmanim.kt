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

/**
 * Keeps track of which zmanim we've already notified the user about for a given day.
 */
public class NotifiedZmanim : Serializable {

  public class Manager {

    private val notified: MutableList<NotifiedZmanim> = ArrayList()

    private var filesDir: File? = null


    public fun init(filesDir: File) {

      this.filesDir = filesDir
      val files = filesDir.listFiles(FileFilter { pathname ->
        pathname.name.endsWith(NOTIFIED_ZMANIM_FILE_EXTENSION)
      })


      if ((files == null) || files.isEmpty()) {
        return
      }

      // Clean up old files. Or read it in if it's not too old.
      val oldestToKeep = Calendar.getInstance()
      oldestToKeep.add(Calendar.DATE, -3)
      val mills = oldestToKeep.timeInMillis

      for (file in files) {
        if (file.lastModified() < mills) {
          file.delete()
        }
        else {
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


    public fun save(notifiedZmanim: NotifiedZmanim) {

      val fileName = notifiedZmanim.year.toString() + "_" + notifiedZmanim.month + "_" + notifiedZmanim.date +
        "." + NOTIFIED_ZMANIM_FILE_EXTENSION
      val storedData = File(filesDir, fileName)
      try {
        val oos = ObjectOutputStream(FileOutputStream(storedData, false))
        oos.writeObject(notifiedZmanim)
        oos.flush()
        oos.close()

      }
      catch (e: Exception) {
        // Urgh. Can't save... We're going to lose our state...
      }
    }


    private fun restore(storedData: File) {

      var restored: NotifiedZmanim

      if (!storedData.canRead()) {
        restored = NotifiedZmanim()
      }
      else {
        var ois: ObjectInputStream? = null
        try {
          ois = ObjectInputStream(FileInputStream(storedData))
          restored = ois.readObject() as NotifiedZmanim

          ois.close()
        }
        catch (e: Throwable) {
          if (ois != null) {
            try {
              ois.close()
            }
            catch (e1: Throwable) {
              // Oh well, we tried...
            }
          }
          restored = NotifiedZmanim()
        }
      }

      synchronized(notified) {
        notified.add(restored)
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

    val otherYear = other.get(Calendar.YEAR)
    if (year != otherYear) {
      return false
    }

    val otherMonth = other.get(Calendar.MONTH)
    if (month != otherMonth) {
      return false
    }

    val otherDate = other.get(Calendar.DATE)
    return date == otherDate
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

    @JvmField
    public val manager: Manager = Manager()


    private const val serialVersionUID: Long = 8982487060417217170L
  }

}
