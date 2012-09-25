package pnnl.cyclist.controller.command;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import pnnl.cyclist.view.component.View;
import pnnl.cyclist.view.component.Workspace;


public class RemoveViewCommand extends SimpleCommand {
	
	public void execute(INotification notification) {
		String mediatorName = (String) notification.getBody();
		
		Mediator mediator = (Mediator) getFacade().retrieveMediator(mediatorName);
		View view = (View) mediator.getViewComponent();
		Workspace workspace = (Workspace) view.getParent();
		
		workspace.removeView(view);
		mediator.setViewComponent(null);
		getFacade().removeMediator(mediatorName);
	}
}
