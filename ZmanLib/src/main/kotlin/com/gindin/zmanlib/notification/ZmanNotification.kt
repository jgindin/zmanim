package com.gindin.zmanlib.notification

import com.gindin.zmanlib.zman.Zman

/**
 * Contains information about an upcoming notification.
 */
public class ZmanNotification internal constructor(
  @JvmField public val zman: Zman,
  @JvmField public val msUntilZman: Long
) {

  override fun toString(): String {
    return "ZmanNotification{" +
      "zman=" + zman.type +
      ", msUntilZman=" + msUntilZman + " (" + msUntilZman + ")" +
      '}'
  }

}
