package weather.view.mediator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;

import org.puremvc.java.multicore.interfaces.INotification;

import pnnl.cyclist.CyclistNames;
import pnnl.cyclist.controller.ApplicationConstants;
import pnnl.cyclist.model.proxy.CyclistDataStream;
import pnnl.cyclist.model.proxy.DataSourcesProxy;
import pnnl.cyclist.model.proxy.WeatherDataStream;
import pnnl.cyclist.model.vo.Weather;
import pnnl.cyclist.model.vo.World;
import pnnl.cyclist.view.event.TimeEvent;
import pnnl.cyclist.view.mediator.CyclistMediator;
import weather.view.tool.view.WeatherMapView;

public class WeatherMapMediator extends CyclistMediator {

	public WeatherMapMediator() {
		super(CyclistNames.WEATHER_MAP_MEDIATOR, null);
	}
	
	@Override
	public void setViewComponent(Object view) {
		super.setViewComponent(view);
		if (view != null)
			getViewComponent().dataProperty().addListener(new ChangeListener<World>() {
				@Override
				public void changed(ObservableValue<? extends World> observable, World oldValue, World newValue) 
				{
					if (newValue != null)
						getViewComponent().setWaiting(false);		
				}
			});
		
			getViewComponent().weatherProperty().addListener(new ChangeListener<Weather>() {
				@Override
				public void changed(ObservableValue<? extends Weather> observable, Weather oldValue, Weather newValue) 
				{
					if (newValue != null)
						getViewComponent().setWaiting(false);		
				}
			});
		
			getViewComponent().onActionProperty().set(new EventHandler<TimeEvent>() {
				
				@Override
				public void handle(TimeEvent event) {
					DataSourcesProxy proxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
					fetchWeather(proxy.getDefaultDataStream(), event.getTimeId());
				}
			});
	}
	
	@Override
	public WeatherMapView getViewComponent() {
		return (WeatherMapView) super.getViewComponent();
	}
	
	@Override
    public String[] listNotificationInterests() {
		return new String[]{ 
				ApplicationConstants.MEDIATOR_INIT,
				ApplicationConstants.DEFAULT_WEATHER_SOURCE,
		};
	}
	
	@Override
    public void handleNotification(INotification notification) {
    	switch (notification.getName()) {
    	case ApplicationConstants.MEDIATOR_INIT: 
    		DataSourcesProxy proxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
    		fetchWorld(proxy.getDefaultDataStream());
    		break;
    	case ApplicationConstants.DEFAULT_WEATHER_SOURCE:
    		fetchWorld((CyclistDataStream) notification.getBody());
    		break;
    	};
	}
	
	/*
	 * fetchWorld
	 */
	private void fetchWorld(CyclistDataStream ds) {
		if (ds != null && ds instanceof WeatherDataStream) {
			WeatherDataStream wds = (WeatherDataStream) ds;
			getViewComponent().setWaiting(true);
			getViewComponent().dataProperty().bind(wds.getWorld());
		}
	}
	
	/*
	 * fetchWeather
	 */
	private void fetchWeather(CyclistDataStream ds, int time) {
		if (ds != null && ds instanceof WeatherDataStream) {
			WeatherDataStream wds = (WeatherDataStream) ds;
			getViewComponent().setWaiting(true);
			getViewComponent().weatherProperty().bind(wds.getWeather(time));
		}
	}
}
