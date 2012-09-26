package pnnl.cyclist.model.vo;

import java.util.List;
import java.util.Map;

import pnnl.cyclist.model.proxy.CyclistDataStream;


import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;

public interface SimulationDataStream extends CyclistDataStream {
	
	ReadOnlyObjectProperty<ObservableList<Agent>> getAgents(final String agentType);
	
	ReadOnlyObjectProperty<ObservableList<Facility>> getFacilities();
	
	ReadOnlyObjectProperty<ObservableList<Isotope>> getIsotopes();
	
	ReadOnlyObjectProperty<Map<String, List<MaterialFlow>>> getMaterialFlow(final List<FilterSet> filters, final Details details);
}
