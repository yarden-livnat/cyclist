package pnnl.cyclist.view.event;

import javafx.event.Event;
import javafx.event.EventType;

public class TimeEvent extends Event {

	public static final EventType<TimeEvent> ANY = new EventType<TimeEvent>(Event.ANY, "Time");
	
	private int _timeId;
	
	public TimeEvent(EventType<? extends Event> event, int timeId) {
		super(event);
		
		_timeId = timeId;
	}
	
	public int getTimeId() {
		return _timeId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1019350662405439659L;
	

}
