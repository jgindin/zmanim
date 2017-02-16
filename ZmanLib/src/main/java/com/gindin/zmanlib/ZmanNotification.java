package com.gindin.zmanlib;

import com.gindin.zmanlib.zman.Zman;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * Contains information about an upcoming notification.
 */
public final class ZmanNotification {

  @NotNull
  public final Zman                                         zman;

  public final long                                         msUntilAction;


  ZmanNotification(
    @NotNull Zman zman,
    long          msUntilAction
  ) {
    this.zman = zman;
    this.msUntilAction = msUntilAction;
  }



  @Override
  public String toString() {
    return "ZmanNotification{" + "zman=" + zman.type + ", msUntilAction=" + msUntilAction + " (" + new Date(
      System.currentTimeMillis() + msUntilAction ) + ")" + '}';
  }

}
