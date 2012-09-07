package cyclist.view.tool.mediator;

import java.util.List;
import java.util.Map;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.puremvc.java.multicore.interfaces.INotification;

import cyclist.CyclistNames;
import cyclist.controller.ApplicationConstants;
import cyclist.model.proxy.DataSourcesProxy;
import cyclist.model.vo.Details;
import cyclist.model.vo.FilterSet;
import cyclist.model.vo.MaterialFlow;
import cyclist.model.vo.SimulationDataStream;
import cyclist.view.mediator.CyclistMediator;
import cyclist.view.tool.view.FlowView;

public class FlowMediator extends CyclistMediator {

	public static final String NAME = "FlowMediator";
	
	private SimulationDataStream _ds;
	
	public FlowMediator() {
		super(NAME, null);
	}
	

	@Override
	public FlowView getViewComponent() {
		return (FlowView) super.getViewComponent();
	}

	@Override
	public void setViewComponent(Object component) {
		super.setViewComponent(component);
		
		if (component != null) {
			getViewComponent().setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					fetchData();
				}
				
			});
			
			getViewComponent().setOnClose(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					sendNotification(ApplicationConstants.REMOVE_VIEW, getMediatorName());
				}
			});
		}
	}
	
	@Override
    public String[] listNotificationInterests() {
		return new String[]{ 
				ApplicationConstants.MEDIATOR_INIT,
				ApplicationConstants.DEFAULT_SIMULATION_SOURCE,
		};
	}
	
	@Override
    public void handleNotification(INotification notification) {
    	switch (notification.getName()) {
    	case ApplicationConstants.MEDIATOR_INIT:
    		DataSourcesProxy proxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
    		_ds = proxy.getDefaultSimulationDataStream();
    		fetchData();
    		break;
    	case ApplicationConstants.DEFAULT_SIMULATION_SOURCE:
    		_ds = (SimulationDataStream) notification.getBody();
    		fetchData();
    		break;
    	};
	}
	
	
	private void fetchData() {
		if (_ds == null) return;
		
		getViewComponent().setWaiting(true);
		List<FilterSet> filters = getViewComponent().getFilters();
		Details details = getViewComponent().getDetails();
		ReadOnlyObjectProperty<Map<String, List<MaterialFlow>>> valueProperty = _ds.getMaterialFlow(filters, details);
		if (valueProperty == null) {
			getViewComponent().setWaiting(false);
		} else 
			getViewComponent().itemsProperty().bind(valueProperty);
	}
}
