package cyclist.view.event;

import cyclist.model.vo.ToolInfo;
import javafx.event.Event;
import javafx.event.EventType;

public class CyclistDropEvent extends CyclistEvent {

	private static final long serialVersionUID = 1L;

	public static final EventType<CyclistDropEvent> DROP = new EventType<CyclistDropEvent>(CyclistEvent.ANY, "DROP");
	
	private ToolInfo _info;
	private double _x;
	private double _y;
	
	public CyclistDropEvent(EventType<? extends Event> eventType, ToolInfo info, double x, double y) {
		super(eventType);
		_info = info; 
		_x = x;
		_y = y;
	}
	
	
	public ToolInfo getInfo() {
		return _info;
	}
	
	public double getX() {
		return _x;
	}
	
	public double getY() {
		return _y;
	}
	
}
