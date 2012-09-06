/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cyclist.controller.command;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import cyclist.controller.ApplicationConstants;

/**
 *
 * @author yarden
 */
public class PrepareControllerCommand extends SimpleCommand {
    /**
    * Fulfill the use-case initiated by the given <code>INotification</code>.
    *
    * @param notification
    *   the <code>INotification</code> to handle.
    */
    public void execute(INotification notification) {
        
    	getFacade().registerCommand(ApplicationConstants.DEFAULT_SIMULATION_SOURCE, new DefaultSimulationCommand());
    	getFacade().registerCommand(ApplicationConstants.REMOVE_VIEW, new RemoveViewCommand());
    }    
}
