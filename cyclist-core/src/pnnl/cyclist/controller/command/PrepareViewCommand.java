/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pnnl.cyclist.controller.command;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;
import org.puremvc.java.multicore.patterns.facade.Facade;

import pnnl.cyclist.view.component.MainScreen;
import pnnl.cyclist.view.mediator.ApplicationMediator;
import pnnl.cyclist.view.mediator.DataPaneMediator;
import pnnl.cyclist.view.mediator.WorkspaceMediator;


/**
 *
 * @author yarden
 */
public class PrepareViewCommand extends SimpleCommand {
   /**
    * Fulfill the use-case initiated by the given <code>INotification</code>.
    *
    * @param notification
    *   the <code>INotification</code> to handle.
    */
    @Override
    public void execute(INotification notification) {
        MainScreen view = (MainScreen) notification.getBody();
        
        // Mediate initial view components
        Facade facade = getFacade();
        facade.registerMediator( new ApplicationMediator(view));
        getFacade().registerMediator(new WorkspaceMediator(view.getWorkspace()));
		getFacade().registerMediator(new DataPaneMediator(view.getDataPane()));
    }   
}
