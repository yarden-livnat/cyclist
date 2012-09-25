package pnnl.cyclist.view.mediator;

import javafx.event.EventHandler;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import pnnl.cyclist.CyclistNames;
import pnnl.cyclist.controller.ApplicationConstants;
import pnnl.cyclist.model.proxy.CyclistDataStream;
import pnnl.cyclist.model.proxy.DataSourcesProxy;
import pnnl.cyclist.model.proxy.CyclistDataStream.State;
import pnnl.cyclist.model.vo.CyclistDataSource;
import pnnl.cyclist.model.vo.SimulationDataStream;
import pnnl.cyclist.view.component.DataPane;
import pnnl.cyclist.view.event.CyclistDataSourceEvent;


public class DataPaneMediator extends Mediator{

	public DataPaneMediator(DataPane view) {
		super(CyclistNames.DATA_PANE_MEDIATOR, view);
		
		view.setOnAction(new EventHandler<CyclistDataSourceEvent>()  {
			@Override
			public void handle(CyclistDataSourceEvent event) {
				DataPane view = getViewComponent();
				DataSourcesProxy dsProxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
				
				if (event.getEventType() == CyclistDataSourceEvent.SELECT) {
					CyclistDataStream ds = dsProxy.getCyclistDataStream(view.getCurrent());
					view.getStatus().bind(ds.stateProperty());		
					if (ds.stateProperty().get() == State.OK) {
						String name = ds.getDataSourceName();
			    		if (getViewComponent().getCurrent().equals(name)) {
			    			sendNotification(ApplicationConstants.DEFAULT_DATA_SOURCE, ds);
			    		}
					}	
				} else if (event.getEventType() == CyclistDataSourceEvent.REMOVE) {
					boolean isDefault = view.getCurrent().equals(event.getName());
					
					dsProxy.removeDataSource(event.getName());
					
					if (isDefault)
						sendNotification(ApplicationConstants.DEFAULT_DATA_SOURCE, null);	
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
    		CyclistDataStream ds = (CyclistDataStream) notification.getBody();
    		String name = ds.getDataSourceName();
    		if (getViewComponent().getCurrent().equals(name)) {
    			sendNotification(ApplicationConstants.DEFAULT_DATA_SOURCE, ds);
    		}
    		break;
    	case ApplicationConstants.DATA_SOURCE_ADDED:
    		CyclistDataSource [] sources = {(CyclistDataSource) notification.getBody()};
    		getViewComponent().addItems(sources);
    	}
    }
}
