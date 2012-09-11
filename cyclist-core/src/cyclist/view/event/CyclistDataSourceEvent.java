/**
 * Cyclist
 * Copyright (c) Yarden Livnat 2012.
 * Scientific Computing and Imaging Institute
 * University of Utah
 */
package cyclist.view.event;

import cyclist.model.vo.CyclistDataSource;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * @author yarden
 *
 */
public class CyclistDataSourceEvent extends CyclistEvent {
	
	private static final long serialVersionUID = 1L;

	public static final EventType<CyclistDataSourceEvent> ANY = new EventType<CyclistDataSourceEvent>(CyclistEvent.ANY, "INPUT");
	
	public static final EventType<CyclistDataSourceEvent> CREATE 		= new EventType<CyclistDataSourceEvent>(CyclistDataSourceEvent.ANY, "CREATE");
	public static final EventType<CyclistDataSourceEvent> SELECT 		= new EventType<CyclistDataSourceEvent>(CyclistDataSourceEvent.ANY, "SELECT");
	public static final EventType<CyclistDataSourceEvent> REMOVE 		= new EventType<CyclistDataSourceEvent>(CyclistDataSourceEvent.ANY, "REMOVE");
	public static final EventType<CyclistDataSourceEvent> UPDATED 		= new EventType<CyclistDataSourceEvent>(CyclistDataSourceEvent.ANY, "UPDATED");
	public static final EventType<CyclistDataSourceEvent> SELECT_FILE 	= new EventType<CyclistDataSourceEvent>(CyclistDataSourceEvent.ANY, "SELECT_FILE");
	
	private String _name;
	private CyclistDataSource _ds;
	
	public CyclistDataSourceEvent(EventType<? extends Event> eventType, String name) {
		super(eventType);
		
		_name = name;
	}
	
	public CyclistDataSourceEvent(EventType<? extends Event> eventType, CyclistDataSource ds) {
		super(eventType);
		_ds = ds;
	}
	
	public String getName() {
		return _name;
	}
	
	public CyclistDataSource getDataSource() {
		return _ds;
	}
}
