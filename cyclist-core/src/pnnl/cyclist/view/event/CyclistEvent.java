package cyclist.view.event;

import javafx.event.Event;
import javafx.event.EventType;

public class CyclistEvent extends Event {

	public static final EventType<CyclistEvent> ANY = new EventType<CyclistEvent>(Event.ANY, "Cyclist");
	
	public CyclistEvent(EventType<? extends Event> event) {
		super(event);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1019350662405439659L;
	

}
