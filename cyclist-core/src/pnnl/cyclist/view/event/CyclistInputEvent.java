package cyclist.view.event;

import javafx.event.Event;
import javafx.event.EventType;

public class CyclistInputEvent extends CyclistEvent {

	public static final EventType<CyclistInputEvent> ANY = new EventType<CyclistInputEvent>(CyclistEvent.ANY, "INPUT");
	
	public static final EventType<CyclistInputEvent> SELECT = new EventType<CyclistInputEvent>(CyclistInputEvent.ANY, "SELECT");
	
	private String _name;
	
	public CyclistInputEvent(EventType<? extends Event> eventType, String name) {
		super(eventType);
		_name = name; 
	}
	
	
	public String getName() {
		return _name;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5609379198746817986L;


}
