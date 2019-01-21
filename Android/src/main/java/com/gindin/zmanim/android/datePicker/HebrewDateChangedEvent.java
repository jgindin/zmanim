/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.datePicker;

import com.gindin.eventBus.Event;
import com.gindin.eventBus.EventHandler;
import com.gindin.zmanlib.calendar.HebrewDate;

/**
 * Represents that a new date has been selected.
 */
public class HebrewDateChangedEvent
  extends Event<HebrewDateChangedEvent.Handler> {

  public static abstract class Handler implements EventHandler<HebrewDateChangedEvent> {
    public abstract void onHebrewDateChanged( HebrewDate newDate );


    @Override
    public Class getAssociatedType() {
      return HebrewDateChangedEvent.class;
    }
  }


  private final HebrewDate                                  newDate;


  public HebrewDateChangedEvent( HebrewDate newDate ) {
    this.newDate = newDate;
  }


  @Override
  public void dispatch( Handler handler ) {
    handler.onHebrewDateChanged( newDate );
  }


  @Override
  public String toString() {
    return "HebrewDateChangedEvent{" +
      "newDate=" + newDate +
      '}';
  }
} // End of HebrewDateChangedEvent class
