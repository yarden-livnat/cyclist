/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cyclist.view.mediator;

import javafx.event.EventHandler;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import cyclist.CyclistNames;
import cyclist.model.proxy.DataSourcesProxy;
import cyclist.model.proxy.WeatherProxy;
import cyclist.view.component.MainScreen;
import cyclist.view.event.CyclistDataSourceEvent;

/**
 *
 * @author yarden
 */
public class ApplicationMediator extends Mediator { 
    
    /**
     * Constructor
     */
    public ApplicationMediator(MainScreen view) {
        super(CyclistNames.APPLICATION_MEDIATOR, view);
        view.setOnDataSourceAction(new EventHandler<CyclistDataSourceEvent>() {
			@Override
			public void handle(CyclistDataSourceEvent event) {
				if (event.getEventType() == CyclistDataSourceEvent.CREATE) {
					DataSourcesProxy proxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
					proxy.addDataSource(event.getDataSource());
				}
				else if (event.getEventType() == CyclistDataSourceEvent.SELECT_FILE) {
					WeatherProxy proxy = (WeatherProxy) getFacade().retrieveProxy(CyclistNames.WEATHER_PROXY);
					proxy.setDataSource(event.getName());
				}
			}
		});
    }
    
    @Override
    public String[] listNotificationInterests() {
		return new String[]{ 
		};
	}
    
    @Override
    public void handleNotification(INotification notification) {
    }
    
    @Override
    public MainScreen getViewComponent() {
        return (MainScreen) super.getViewComponent();
    }
    
    
}
