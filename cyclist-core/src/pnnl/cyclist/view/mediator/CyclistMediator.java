package pnnl.cyclist.view.mediator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import pnnl.cyclist.controller.ApplicationConstants;
import pnnl.cyclist.view.component.View;


public class CyclistMediator extends Mediator {

	private static int _counter = 0;
	
	public CyclistMediator() {
		this("CyclistMediator", null);
	}
	
	public CyclistMediator(String mediatorName, Object viewComponent) {
		super(mediatorName+":"+_counter++, viewComponent);
	}
	
	public void setParam(String param) {
	}
	
	@Override
	public void setViewComponent(Object component) {
		
		if (component == getViewComponent()) return;
		
		super.setViewComponent(component);
		
		if (component != null) {
			final View view = (View) getViewComponent();
			view.setOnClose(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					sendNotification(ApplicationConstants.REMOVE_VIEW, getMediatorName());
				}
			});
		}
	}	
}
