package pnnl.cyclist.view.event;

import javafx.event.Event;
import javafx.event.EventType;

public class CyclistDropEvent extends CyclistEvent {

	private static final long serialVersionUID = 1L;

	public static final EventType<CyclistDropEvent> DROP = new EventType<CyclistDropEvent>(CyclistEvent.ANY, "DROP");
	
	private String _name;
	private double _x;
	private double _y;
	
	public CyclistDropEvent(EventType<? extends Event> eventType, String name, double x, double y) {
		super(eventType);
		_name = name;
		_x = x;
		_y = y;
	}
	
	public String getName() {
		return _name;
		
	}
	public double getX() {
		return _x;
	}
	
	public double getY() {
		return _y;
	}
	
}
