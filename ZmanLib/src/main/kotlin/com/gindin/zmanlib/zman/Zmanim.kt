/*
 * Copyright (c) 2020. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Collections

/** Contains a set of related zmanim. */
public abstract class Zmanim
protected constructor(
  @JvmField public val type: Type,
  private val zmanimCalculator: ZmanimCalculator,
) {

  /** The individual zmanim for this set. */
  private var allZmanim: List<Zman>? = null

  /**
   * Finds the named zman.
   *
   * @param zmanName which zman to locate.
   * @return the named zman, or the default zman if the specified name cannot be found.
   */
  public open fun getByName(zmanName: String?): Zman? {

    val zmanim = ensureZmanim()

    if (zmanName.isNullOrEmpty()) {
      return getDefault()
    }

    for (zman in zmanim) {
      if (zmanName == zman.name) {
        return zman
      }
    }

    return getDefault()
  }

  /**
   * Finds a default zman to represent this group.
   *
   * @return As currently implemented, returns the first non-advanced zman in the group.
   */
  public open fun getDefault(): Zman? {

    for (zman in ensureZmanim()) {
      if (!zman.advanced) {
        return zman
      }
    }

    // Should never get here.
    return null
  }

  public open fun asList(): List<Zman> {
    return ensureZmanim()
  }

  /** Called at most once to create the list of zmanim represented. */
  protected abstract fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman>

  /** Builds the list of zmanim the first time any of them is needed. */
  private fun ensureZmanim(): List<Zman> {

    var zmanim = allZmanim
    if (zmanim == null) {
      zmanim = Collections.unmodifiableList(buildZmanimList(zmanimCalculator))
      allZmanim = zmanim
    }

    return zmanim
  }

  /**
   * Used for easier, programmatic access to the zmanim. Use these to get the actual zmanim for each zman.
   *
   * <p>
   * Consider the explanations of the times at http://kehillatisrael.net/hcal/zmanim.html
   */
  public enum class Type(
    /**
     * Name of the set of zmanim. E.g., "Sof Zman Shema". Not called `name` because that would hide the name of the enum
     * constant.
     */
    @JvmField public val displayName: String,

    /** Describes the set of zmanim. E.g., "Latest time to say Shema". */
    @JvmField public val description: String,
  ) {

    Alot("Alot Hashachar", "Dawn") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return AlotHashachar(zmanimCalculator)
      }
    },
    Talit("Talit/Tefilin", "Earliest Time to don Talit and Tefilin") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return Talit(zmanimCalculator)
      }
    },
    Hanetz("Hanetz", "Sunrise") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return Hanetz(zmanimCalculator)
      }
    },
    SofZmanShema("Sof Zman Shema", "Latest time to finish Shema") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return SofZmanShema(zmanimCalculator)
      }
    },
    SofZmanTefila("Sof Zman Tefila", "Latest time to finish Shacharit") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return SofZmanTefila(zmanimCalculator)
      }
    },
    SofZmanAchilatChametz("Achilat Chametz", "Latest time to eat Chametz") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return SofZmanAchilatChametz(zmanimCalculator)
      }
    },
    SofZmanBiurChametz("Biur Chametz", "Latest time to dispose Chametz") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return SofZmanBiurChametz(zmanimCalculator)
      }
    },
    Chatzot("Chatzot", "Mid-day") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return Chatzot(zmanimCalculator)
      }
    },
    MinchaGedola("Mincha Gedola", "Earliest time to start Mincha") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return MinchaGedola(zmanimCalculator)
      }
    },
    MinchaKetana("Mincha Ketana", "Earliest time to start Mincha (Rambam)") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return MinchaKetana(zmanimCalculator)
      }
    },
    PlagHaMincha("Plag HaMincha", "Earliest time to start Arvit") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return PlagHaMincha(zmanimCalculator)
      }
    },
    CandleLighting("Candle Lighting", "When candles should be lit") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return CandleLighting(zmanimCalculator)
      }
    },
    Shkia("Shkia", "Sunset") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return Shkia(zmanimCalculator)
      }
    },
    LatestMincha("Latest Mincha", "Latest time to finish Mincha") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return LatestMincha(zmanimCalculator)
      }
    },
    BeinHashmashot("Bein Hashmashot", "Twilight") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return BeinHashmashot(zmanimCalculator)
      }
    },
    FastEnd("Fast Ends", "Earliest end of fast days") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return FastEnds(zmanimCalculator)
      }
    },
    TzaitHacochavim("Tzait Hacochavim", "Nightfall") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return TzaitHacochavim(zmanimCalculator)
      }
    },
    ShabbatEnd("Havdalah", "Earliest Havdalah") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return ShabbatEnds(zmanimCalculator)
      }
    },
    YomTovSheniCandleLighting("Candle Lighting", "When candles should be lit") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return YomTovSheniCandleLighting(zmanimCalculator)
      }
    },
    NightChatzot("Midnight", "Halachic Midnight") {
      override fun get(zmanimCalculator: ZmanimCalculator): Zmanim {
        return NightChatzot(zmanimCalculator)
      }
    };

    public abstract fun get(zmanimCalculator: ZmanimCalculator): Zmanim
  }
}
