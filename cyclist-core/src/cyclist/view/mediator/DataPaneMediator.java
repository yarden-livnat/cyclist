package cyclist.view.mediator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import cyclist.CyclistNames;
import cyclist.controller.ApplicationConstants;
import cyclist.model.proxy.DataSourcesProxy;
import cyclist.model.vo.CyclistDataSource;
import cyclist.model.vo.SimulationDataStream;
import cyclist.model.vo.SimulationDataStream.State;
import cyclist.view.component.DataPane;
import cyclist.view.event.CyclistDataSourceEvent;

public class DataPaneMediator extends Mediator{

	public DataPaneMediator(DataPane view) {
		super(CyclistNames.DATA_PANE_MEDIATOR, view);
		
		view.setOnAction(new EventHandler<CyclistDataSourceEvent>()  {
			@Override
			public void handle(CyclistDataSourceEvent event) {
				DataPane view = getViewComponent();
				DataSourcesProxy dsProxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
				
				if (event.getEventType() == CyclistDataSourceEvent.SELECT) {
					SimulationDataStream ds = dsProxy.getSimulationDataStread(view.getCurrent());
					view.getStatus().bind(ds.stateProperty());		
					if (ds.stateProperty().get() == State.OK) {
						String name = ds.getDataSourceName();
			    		if (getViewComponent().getCurrent().equals(name)) {
			    			sendNotification(ApplicationConstants.DEFAULT_SIMULATION_SOURCE, ds);
			    		}
					}	
				} else if (event.getEventType() == CyclistDataSourceEvent.REMOVE) {
					boolean isDefault = view.getCurrent().equals(event.getName());
					
					dsProxy.removeDataSource(event.getName());
					
					if (isDefault)
						sendNotification(ApplicationConstants.DEFAULT_SIMULATION_SOURCE, null);	
				} else if (event.getEventType() == CyclistDataSourceEvent.UPDATED) {
					dsProxy.updateDataSource(event.getDataSource());
				}
			}
		});
	}
	
	@Override
	public DataPane getViewComponent() {
		return (DataPane) super.getViewComponent();
	}
	
	@Override
    public String[] listNotificationInterests() {
		return new String[]{ 
				ApplicationConstants.READY,
				ApplicationConstants.DATA_SOURCE_OK,
				ApplicationConstants.DATA_SOURCE_ADDED,
		};
	}
    
    @Override
    public void handleNotification(INotification notification) {
    	switch (notification.getName()) {
    	case ApplicationConstants.READY:
    		 DataSourcesProxy proxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
    		 getViewComponent().addItems(proxy.getDataSources());
    		 break;
    	case ApplicationConstants.DATA_SOURCE_OK:
    		SimulationDataStream ds = (SimulationDataStream) notification.getBody();
    		String name = ds.getDataSourceName();
    		if (getViewComponent().getCurrent().equals(name)) {
    			sendNotification(ApplicationConstants.DEFAULT_SIMULATION_SOURCE, ds);
    		}
    		break;
    	case ApplicationConstants.DATA_SOURCE_ADDED:
    		CyclistDataSource [] sources = {(CyclistDataSource) notification.getBody()};
    		getViewComponent().addItems(sources);
    	}
    }
}
