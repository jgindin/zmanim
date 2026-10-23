/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.eventBus

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UT_EventBus {

  private lateinit var eventBus: EventBus

  @BeforeTest
  fun setup() {
    eventBus = EventBus()
  }

  @Test
  fun simpleTest() {
    var receivedEvent = false

    val handler =
      object : Event1.Handler() {
        override fun onEvent1() {
          receivedEvent = true
        }
      }
    eventBus.addHandler(handler)

    eventBus.fire(Event1())
    eventBus.removeHandler(handler)

    assertTrue(receivedEvent)
  }

  @Test
  fun nestedEvents() {
    val handlersInOrder = mutableListOf<EventHandler<*>>()

    val handler1 =
      object : Event1.Handler() {
        override fun onEvent1() {
          handlersInOrder.add(this)
          eventBus.fire(Event2())
        }
      }
    eventBus.addHandler(handler1)

    val handler2 =
      object : Event2.Handler() {
        override fun onEvent2() {
          handlersInOrder.add(this)
        }
      }
    eventBus.addHandler(handler2)

    val handler3 =
      object : Event1.Handler() {
        override fun onEvent1() {
          handlersInOrder.add(this)
        }
      }
    eventBus.addHandler(handler3)

    eventBus.fire(Event1())

    assertEquals(handler2, handlersInOrder.last())
  }

  @Test
  fun addHandlerDuringEventHandling() {
    val handlersInOrder = mutableListOf<EventHandler<*>>()

    val handler2 =
      object : Event2.Handler() {
        override fun onEvent2() {
          handlersInOrder.add(this)
        }
      }

    val handler1 =
      object : Event1.Handler() {
        override fun onEvent1() {
          handlersInOrder.add(this)
          eventBus.addHandler(handler2)

          eventBus.fire(Event2())
        }
      }
    eventBus.addHandler(handler1)

    val handler3 =
      object : Event1.Handler() {
        override fun onEvent1() {
          handlersInOrder.add(this)
        }
      }
    eventBus.addHandler(handler3)

    eventBus.fire(Event1())

    assertEquals(handler2, handlersInOrder.last())
  }

  @Test
  fun removeHandlerUponReceiptOfEvent() {
    val handlersInOrder = mutableListOf<EventHandler<*>>()

    val handler1 =
      object : Event1.Handler() {
        override fun onEvent1() {
          handlersInOrder.add(this)

          eventBus.removeHandler(this)

          eventBus.fire(Event2())
        }
      }
    eventBus.addHandler(handler1)

    val handler2 =
      object : Event2.Handler() {
        override fun onEvent2() {
          handlersInOrder.add(this)
        }
      }
    eventBus.addHandler(handler2)

    val handler3 =
      object : Event1.Handler() {
        override fun onEvent1() {
          handlersInOrder.add(this)
        }
      }
    eventBus.addHandler(handler3)

    eventBus.fire(Event1())

    assertEquals(handler2, handlersInOrder.last())
  }

  @Test
  fun addHandlerDuringPostponedEvent() {
    val handlersInOrder = mutableListOf<EventHandler<*>>()

    val handler3 =
      object : Event3.Handler() {
        override fun onEvent3() {
          handlersInOrder.add(this)
        }
      }

    val handler2 =
      object : Event2.Handler() {
        override fun onEvent2() {
          handlersInOrder.add(this)
          eventBus.addHandler(handler3)

          eventBus.fire(Event3())
        }
      }

    val handler1 =
      object : Event1.Handler() {
        override fun onEvent1() {
          handlersInOrder.add(this)
          eventBus.addHandler(handler2)

          eventBus.fire(Event2())
        }
      }
    eventBus.addHandler(handler1)

    eventBus.fire(Event1())

    assertEquals(3, handlersInOrder.size)
    assertEquals(handler1, handlersInOrder[0])
    assertEquals(handler2, handlersInOrder[1])
    assertEquals(handler3, handlersInOrder[2])
  }

  @Test
  fun removeHandlerDuringPostponedEvent() {
    val handlersInOrder = mutableListOf<EventHandler<*>>()

    val handler2 =
      object : Event2.Handler() {
        override fun onEvent2() {
          handlersInOrder.add(this)

          eventBus.fire(Event3())
        }
      }

    val handler1 =
      object : Event1.Handler() {
        override fun onEvent1() {
          handlersInOrder.add(this)

          eventBus.removeHandler(this)

          eventBus.fire(Event2())
        }
      }

    eventBus.addHandler(handler1)
    eventBus.addHandler(handler2)

    eventBus.fire(Event1())

    assertEquals(2, handlersInOrder.size)
    assertEquals(handler1, handlersInOrder[0])
    assertEquals(handler2, handlersInOrder[1])
  }

  @Test
  fun addHandlerWithCatchup() {
    val handlersInOrder = mutableListOf<EventHandler<*>>()

    val handler1 =
      object : Event1.Handler() {
        override fun onEvent1() {
          handlersInOrder.add(this)
        }
      }
    eventBus.addHandler(handler1)

    eventBus.fire(Event1())

    val handler2 =
      object : Event1.Handler() {
        override fun onEvent1() {
          handlersInOrder.add(this)
        }
      }
    eventBus.addHandler(handler2, true)

    assertEquals(handler1, handlersInOrder[0])
    assertEquals(handler2, handlersInOrder[1])
  }

  @Test
  fun testUncachableEvent() {
    // To test uncachable events:
    //  1. fire an event that is not cachable
    eventBus.fire(UncachableEvent())

    //  2. add a handler w/ catchup and ensure it does not get any event.
    var received: Boolean? = null
    val handler =
      object : UncachableEvent.Handler() {
        override fun onUncachableEvent() {
          received = true
        }
      }
    eventBus.addHandler(handler, true)
    assertNull(received)
  }

  @Test
  fun testCacheControl() {
    // To test cache control:
    //  1. fire a cachable event (e1)
    eventBus.fire(Event1())

    //  2. add handler for e1 w/ catchup to ensure it was cached
    var received: Boolean? = null
    val handler1 =
      object : Event1.Handler() {
        override fun onEvent1() {
          received = true
        }
      }
    eventBus.addHandler(handler1, true)
    assertEquals(true, received)

    //  3. fire event that will clear that event from the cache
    eventBus.fire(EventWithCacheControl())

    //  4. add handler for e1 w/ catchup and ensure it does not get any catch up event
    received = null
    val handler2 =
      object : Event1.Handler() {
        override fun onEvent1() {
          received = true
        }
      }
    eventBus.addHandler(handler2, true)
    assertNull(received)
  }

  private class Event1 : Event<Event1.Handler>() {

    abstract class Handler : EventHandler<Event1> {
      abstract fun onEvent1()

      override val associatedType: Class<Event1> = Event1::class.java
    }

    override fun dispatch(handler: Handler) {
      handler.onEvent1()
    }
  }

  private class Event2 : Event<Event2.Handler>() {

    abstract class Handler : EventHandler<Event2> {
      abstract fun onEvent2()

      override val associatedType: Class<Event2> = Event2::class.java
    }

    override fun dispatch(handler: Handler) {
      handler.onEvent2()
    }
  }

  private class Event3 : Event<Event3.Handler>() {

    abstract class Handler : EventHandler<Event3> {
      abstract fun onEvent3()

      override val associatedType: Class<Event3> = Event3::class.java
    }

    override fun dispatch(handler: Handler) {
      handler.onEvent3()
    }
  }

  private class EventWithCacheControl : Event<EventWithCacheControl.Handler>() {

    abstract class Handler : EventHandler<EventWithCacheControl> {
      abstract fun onEventWithCacheControl()

      override val associatedType: Class<EventWithCacheControl> = EventWithCacheControl::class.java
    }

    override fun preDispatch(cacheControl: EventCacheControl) {
      super.preDispatch(cacheControl)

      cacheControl.clearCacheForType(Event1::class.java)
    }

    override fun dispatch(handler: Handler) {
      handler.onEventWithCacheControl()
    }
  }

  private class UncachableEvent : Event<UncachableEvent.Handler>() {

    abstract class Handler : EventHandler<UncachableEvent> {
      abstract fun onUncachableEvent()

      override val associatedType: Class<UncachableEvent> = UncachableEvent::class.java
    }

    override fun isCachable(): Boolean = false

    override fun dispatch(handler: Handler) {
      handler.onUncachableEvent()
    }
  }
}
