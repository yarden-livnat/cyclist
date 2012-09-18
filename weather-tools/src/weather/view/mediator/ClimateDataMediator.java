package weather.view.mediator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import org.puremvc.java.multicore.interfaces.INotification;

import weather.view.tool.component.GenericTableView;
import cyclist.CyclistNames;
import cyclist.controller.ApplicationConstants;
import cyclist.model.proxy.WeatherProxy;
import cyclist.model.vo.Table;
import cyclist.model.vo.Weather;
import cyclist.view.mediator.CyclistMediator;
import cyclist.model.vo.ClimateRowData;


public class ClimateDataMediator extends CyclistMediator {

	private String _type;
	
	public ClimateDataMediator(String type) {
		super(CyclistNames.CLIMATE_DATA_MEDIATOR+"_"+type, null);
		_type = type;
	}
	
	@Override
	public void setViewComponent(Object view) {
		super.setViewComponent(view);
		if (view != null)
			getViewComponent().dataProperty().addListener(new ChangeListener<Table>() {
				@Override
				public void changed(
						ObservableValue<? extends Table> observable,
						Table oldValue,
						Table newValue) 
				{
//					if (oldValue == null)
					if (newValue != null)
						getViewComponent().setWaiting(false);		
				}
			});
	}
	@Override
	public GenericTableView getViewComponent() {
		return (GenericTableView) super.getViewComponent();
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
//    		getViewComponent().dataProperty().bind(proxy.getClimateData(_type));
    		getViewComponent().dataProperty().bind(proxy.getClimateTable(_type));
    		break;
    	};
	}
}
