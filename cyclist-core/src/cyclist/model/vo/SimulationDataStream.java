package cyclist.model.vo;

import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;

public interface SimulationDataStream {
	
	enum State {NOT_READY, CONNECTING, OK, WARNING, ERROR}
	
	ObjectProperty<State> stateProperty();
	
	String getDataSourceName();
	
	void setDataSource(CyclistDataSource ds);
	
	ReadOnlyObjectProperty<ObservableList<Agent>> getAgents(final String agentType);
	
	ReadOnlyObjectProperty<ObservableList<Facility>> getFacilities();
	
	ReadOnlyObjectProperty<ObservableList<Isotope>> getIsotopes();
	
	ReadOnlyObjectProperty<Map<String, List<MaterialFlow>>> getMaterialFlow(final List<FilterSet> filters, final Details details);
}
