package cyclist.view.tool.mediator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import org.puremvc.java.multicore.interfaces.INotification;

import cyclist.CyclistNames;
import cyclist.controller.ApplicationConstants;
import cyclist.model.proxy.DataSourcesProxy;
import cyclist.model.vo.Facility;
import cyclist.model.vo.SimulationDataStream;
import cyclist.view.mediator.CyclistMediator;
import cyclist.view.tool.view.FacilityView;

public class FacilityMediator extends CyclistMediator {
		
	private SimulationDataStream _ds;
	
	public FacilityMediator() {
		super(CyclistNames.FACILITY_MEDIATOR, null);
	}
	
	@Override
	public void setParam(String param) {
	}
	
	@Override
	public void setViewComponent(Object view) {
		super.setViewComponent(view);
		if (view != null)
			getViewComponent().facilityProperty().addListener(new ChangeListener<ObservableList<Facility>>() {

				@Override
				public void changed(
						ObservableValue<? extends ObservableList<Facility>> arg0,
						ObservableList<Facility> oldValue, ObservableList<Facility> newValue) 
				{
					if (oldValue == null)
						getViewComponent().setWaiting(false);
				}
				
			});
	}
	
	@Override
	public FacilityView getViewComponent() {
		return (FacilityView) super.getViewComponent();
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
    		if (_ds != null) {
    			getViewComponent().setWaiting(true);
        		getViewComponent().facilityProperty().bind(_ds.getFacilities());
    		}
    		break;
    	case ApplicationConstants.DEFAULT_SIMULATION_SOURCE:
    		_ds = (SimulationDataStream) notification.getBody();
			getViewComponent().setWaiting(true);
			getViewComponent().facilityProperty().bind(_ds.getFacilities());
    		break;
    	};
	}
}
