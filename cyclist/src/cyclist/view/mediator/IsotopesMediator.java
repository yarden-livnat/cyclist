package cyclist.view.mediator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import org.puremvc.java.multicore.interfaces.INotification;

import cyclist.CyclistNames;
import cyclist.controller.ApplicationConstants;
import cyclist.model.proxy.DataSourcesProxy;
import cyclist.model.vo.Isotope;
import cyclist.model.vo.SimulationDataStream;
import cyclist.view.component.tools.IsotopesView;

public class IsotopesMediator extends CyclistMediator {

	public IsotopesMediator() {
		super(CyclistNames.ISOTOPES_MEDATOR, null);
	}
	
	@Override
	public void setViewComponent(Object view) {
		super.setViewComponent(view);
		if (view != null)
			getViewComponent().itemsProperty().addListener(new ChangeListener<ObservableList<Isotope>>() {

				@Override
				public void changed(
						ObservableValue<? extends ObservableList<Isotope>> arg0,
						ObservableList<Isotope> oldValue, ObservableList<Isotope> newValue) 
				{
					if (oldValue == null)
						getViewComponent().setWaiting(false);
				}
				
			});
	}
	@Override
	public IsotopesView getViewComponent() {
		return (IsotopesView) super.getViewComponent();
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
		SimulationDataStream ds;
		
    	switch (notification.getName()) {
    	case ApplicationConstants.MEDIATOR_INIT:
    		DataSourcesProxy proxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
    		ds = proxy.getDefaultSimulationDataStream();
    		if (ds != null) {
    			getViewComponent().setWaiting(true);
        		getViewComponent().itemsProperty().bind(ds.getIsotopes());
    		}
    		break;
    	case ApplicationConstants.DEFAULT_SIMULATION_SOURCE:
    		ds = (SimulationDataStream) notification.getBody();
			getViewComponent().setWaiting(true);
    		getViewComponent().itemsProperty().bind(ds.getIsotopes());
    		break;
    	};
	}
}
