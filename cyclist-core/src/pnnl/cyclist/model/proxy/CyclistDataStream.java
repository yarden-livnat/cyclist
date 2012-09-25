package pnnl.cyclist.model.proxy;

import pnnl.cyclist.model.vo.CyclistDataSource;
import javafx.beans.property.ObjectProperty;

public interface CyclistDataStream {
	enum State {NOT_READY, CONNECTING, OK, WARNING, ERROR}
	
	ObjectProperty<State> stateProperty();
	
	String getDataSourceName();
	
	void setDataSource(CyclistDataSource ds);
}
