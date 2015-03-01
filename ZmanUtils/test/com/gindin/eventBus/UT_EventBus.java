/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.eventBus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UT_EventBus {


	private EventBus                                          eventBus;


	@Before
	public void setup() {
		eventBus = new EventBus();
	}


	@Test
	public void simpleTest() {

		final boolean[] receivedEvent = new boolean[1];

		Event1.Handler handler = new Event1.Handler() {
			@Override
			public void onEvent1() {
				receivedEvent[ 0 ] = true;
			}
		};
		eventBus.addHandler( handler );

		Event1 event1 = new Event1();
		eventBus.fire( event1 );
		eventBus.removeHandler( handler );
		
		Assert.assertTrue( receivedEvent[ 0 ] );
	}


	@Test
	public void nestedEvents() {

		final List<EventHandler> handlersInOrder = new ArrayList<EventHandler>();

		final Event1.Handler handler1 = new Event1.Handler() {
			@Override
			void onEvent1() {
				handlersInOrder.add( this );
				Event2 event2 = new Event2();
				eventBus.fire( event2 );
			}
		};
		eventBus.addHandler( handler1 );

		final Event2.Handler handler2 = new Event2.Handler() {
			@Override
			void onEvent2() {
				handlersInOrder.add( this );
			}
		};
		eventBus.addHandler( handler2 );

		final Event1.Handler handler3 = new Event1.Handler() {
			@Override
			void onEvent1() {
				handlersInOrder.add( this );
			}
		};
		eventBus.addHandler( handler3 );

		Event1 event1 = new Event1();
		eventBus.fire( event1 );

		Assert.assertEquals( handler2, handlersInOrder.get( handlersInOrder.size() - 1 ) );
	}


	@Test
	public void addHandlerDuringEventHandling() {

		final List<EventHandler> handlersInOrder = new ArrayList<EventHandler>();

		final Event2.Handler handler2 = new Event2.Handler() {
			@Override
			void onEvent2() {
				handlersInOrder.add( this );
			}
		};

		final Event1.Handler handler1 = new Event1.Handler() {
			@Override
			void onEvent1() {
				handlersInOrder.add( this );
				eventBus.addHandler( handler2 );

				Event2 event2 = new Event2();
				eventBus.fire( event2 );
			}
		};
		eventBus.addHandler( handler1 );

		final Event1.Handler handler3 = new Event1.Handler() {
			@Override
			void onEvent1() {
				handlersInOrder.add( this );
			}
		};
		eventBus.addHandler( handler3 );

		Event1 event1 = new Event1();
		eventBus.fire( event1 );

		Assert.assertEquals( handler2, handlersInOrder.get( handlersInOrder.size() - 1 ) );
	}


	@Test
	public void removeHandlerUponReceiptOfEvent() {

		final List<EventHandler> handlersInOrder = new ArrayList<EventHandler>();

		final Event1.Handler handler1 = new Event1.Handler() {
			@Override
			void onEvent1() {
				handlersInOrder.add( this );

				eventBus.removeHandler( this );

				Event2 event2 = new Event2();
				eventBus.fire( event2 );
			}
		};
		eventBus.addHandler( handler1 );

		final Event2.Handler handler2 = new Event2.Handler() {
			@Override
			void onEvent2() {
				handlersInOrder.add( this );
			}
		};
		eventBus.addHandler( handler2 );

		final Event1.Handler handler3 = new Event1.Handler() {
			@Override
			void onEvent1() {
				handlersInOrder.add( this );
			}
		};
		eventBus.addHandler( handler3 );

		Event1 event1 = new Event1();
		eventBus.fire( event1 );

		Assert.assertEquals( handler2, handlersInOrder.get( handlersInOrder.size() - 1 ) );
	}


	@Test
	public void addHandlerDuringPostponedEvent() {

		final List<EventHandler> handlersInOrder = new ArrayList<EventHandler>();

		final Event3.Handler handler3 = new Event3.Handler() {
			@Override
			void onEvent3() {
				handlersInOrder.add( this );
			}
		};

		final Event2.Handler handler2 = new Event2.Handler() {
			@Override
			void onEvent2() {
				handlersInOrder.add( this );
				eventBus.addHandler( handler3 );

				Event3 event3 = new Event3();
				eventBus.fire( event3 );
			}
		};

		final Event1.Handler handler1 = new Event1.Handler() {
			@Override
			void onEvent1() {
				handlersInOrder.add( this );
				eventBus.addHandler( handler2 );

				Event2 event2 = new Event2();
				eventBus.fire( event2 );
			}
		};
		eventBus.addHandler( handler1 );

		Event1 event1 = new Event1();
		eventBus.fire( event1 );

		Assert.assertEquals( handler1, handlersInOrder.get( 0 ) );
		Assert.assertEquals( handler2, handlersInOrder.get( 1 ) );
		Assert.assertEquals( handler3, handlersInOrder.get( 2 ) );
	}


	@Test
	public void addHandlerWithCatchup() {

		final List<EventHandler> handlersInOrder = new ArrayList<EventHandler>();

		final Event1.Handler handler1 = new Event1.Handler() {
			@Override
			void onEvent1() {
				handlersInOrder.add( this );
			}
		};
		eventBus.addHandler( handler1 );

		Event1 event1 = new Event1();
		eventBus.fire( event1 );

		final Event1.Handler handler2 = new Event1.Handler() {
			@Override
			void onEvent1() {
				handlersInOrder.add( this );
			}
		};
		eventBus.addHandler( handler2, true );

		Assert.assertEquals( handler1, handlersInOrder.get( 0 ) );
		Assert.assertEquals( handler2, handlersInOrder.get( 1 ) );
	}


	@Test
	public void testUncachableEvent() {

		// To test uncachable events:
		//  1. fire an event that is not cachable
		eventBus.fire( new UncachableEvent() );

		//  2. add a handler w/ catchup and ensure it does not get any event.
		final Boolean[] received = new Boolean[1];
		UncachableEvent.Handler handler = new UncachableEvent.Handler() {
			@Override
			void onUncachableEvent() {
				received[0] = Boolean.TRUE;
			}
		};
		eventBus.addHandler( handler, true );
		Assert.assertNull( received[0] );
	}


	@Test
	public void testCacheControl() {

		// To test cache control:
		//  1. fire a cachable event (e1)
		eventBus.fire( new Event1() );

		//  2. add handler for e1 w/ catchup to ensure it was cached
		final Boolean[] received = new Boolean[1];
		final Event1.Handler handler1 = new Event1.Handler() {
			@Override
			void onEvent1() {
				received[0] = Boolean.TRUE;
			}
		};
		eventBus.addHandler( handler1, true );
		Assert.assertEquals( Boolean.TRUE, received[ 0 ] );

		//  3. fire event that will clear that event from the cache
		eventBus.fire( new EventWithCacheControl() );

		//  4. add handler for e1 w/ catchup and ensure it does not get any catch up event
		received[0] = null;
		final Event1.Handler handler2 = new Event1.Handler() {
			@Override
			void onEvent1() {
				received[0] = Boolean.TRUE;
			}
		};
		eventBus.addHandler( handler2, true );
		Assert.assertNull( received[ 0 ] );
	}



	private static class Event1 extends Event<Event1.Handler> {

		static abstract class Handler implements EventHandler {
			abstract void onEvent1();

			@Override
			public Class getAssociatedType() {
				return Event1.class;
			}
		}


		@Override
		public void dispatch( Handler handler ) {
			handler.onEvent1();
		}
	}
	
	
	private static class Event2 extends Event<Event2.Handler> {

		static abstract class Handler implements EventHandler {
			abstract void onEvent2();

			@Override
			public Class getAssociatedType() {
				return Event2.class;
			}
		}


		@Override
		public void dispatch( Handler handler ) {
			handler.onEvent2();
		}
	}


	private static class Event3 extends Event<Event3.Handler> {

		static abstract class Handler implements EventHandler {
			abstract void onEvent3();

			@Override
			public Class getAssociatedType() {
				return Event3.class;
			}
		}


		@Override
		public void dispatch( Handler handler ) {
			handler.onEvent3();
		}
	}


	private static class EventWithCacheControl extends Event<EventWithCacheControl.Handler> {

		static abstract class Handler implements EventHandler<EventWithCacheControl> {
			abstract void onEventWithCacheControl();


			@Override
			public Class<EventWithCacheControl> getAssociatedType() {
				return EventWithCacheControl.class;
			}
		}


		@Override
		public void preDispatch( EventCacheControl cacheControl ) {
			super.preDispatch( cacheControl );

			cacheControl.clearCacheForType( Event1.class );
		}


		@Override
		public void dispatch( Handler handler ) {
			handler.onEventWithCacheControl();
		}
	}


	private static class UncachableEvent extends Event<UncachableEvent.Handler> {

		static abstract class Handler implements EventHandler<UncachableEvent> {
			abstract void onUncachableEvent();


			@Override
			public Class<UncachableEvent> getAssociatedType() {
				return UncachableEvent.class;
			}
		}


		@Override
		public boolean isCachable() {
			return false;
		}


		@Override
		public void dispatch( Handler handler ) {
			handler.onUncachableEvent();
		}
	}

} // End of UT_EventBus class
