package cyclist.view.tool.mediator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import org.puremvc.java.multicore.interfaces.INotification;

import cyclist.CyclistNames;
import cyclist.controller.ApplicationConstants;
import cyclist.model.proxy.WeatherProxy;
import cyclist.model.vo.Weather;
import cyclist.view.mediator.CyclistMediator;
import cyclist.view.tool.component.MeasurementsView;

public class MeasurementsMediator extends CyclistMediator {

	public MeasurementsMediator() {
		super(CyclistNames.MEASUREMENTS_MEDIATOR, null);
	}
	
	@Override
	public void setViewComponent(Object view) {
		super.setViewComponent(view);
		if (view != null)
			getViewComponent().dataProperty().addListener(new ChangeListener<ObservableList<Weather>>() {
				@Override
				public void changed(
						ObservableValue<? extends ObservableList<Weather>> observable,
						ObservableList<Weather> oldValue,
						ObservableList<Weather> newValue) 
				{
//					if (oldValue == null)
					if (newValue != null)
						getViewComponent().setWaiting(false);		
				}
			});
	}
	@Override
	public MeasurementsView getViewComponent() {
		return (MeasurementsView) super.getViewComponent();
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
    	case ApplicationConstants.DEFAULT_WEATHER_SOURCE:
    		getViewComponent().setWaiting(true);
    		WeatherProxy proxy = (WeatherProxy) getFacade().retrieveProxy(CyclistNames.WEATHER_PROXY);
    		getViewComponent().dataProperty().bind(proxy.getWeather());
    		break;
    	};
	}
}
