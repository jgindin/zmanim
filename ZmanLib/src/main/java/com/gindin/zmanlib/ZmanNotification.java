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

  public final long                                         msUntilZman;


  ZmanNotification(
    @NotNull Zman zman,
    long msUntilZman
  ) {
    this.zman = zman;
    this.msUntilZman = msUntilZman;
  }



  @Override
  public String toString() {
    return "ZmanNotification{" + "zman=" + zman.type + ", msUntilZman=" + msUntilZman + " (" + new Date(
      System.currentTimeMillis() + msUntilZman ) + ")" + '}';
  }

}
