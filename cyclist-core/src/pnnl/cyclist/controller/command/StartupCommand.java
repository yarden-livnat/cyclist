/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pnnl.cyclist.controller.command;


import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.MacroCommand;

import pnnl.cyclist.controller.ApplicationConstants;

/**
 *
 * @author yarden
 */
public class StartupCommand extends MacroCommand {
    /**
     * Register mediator and proxy.
     * @param notification notification
     */
    @Override
    protected void initializeMacroCommand() {
        
        addSubCommand(new PrepareControllerCommand());
        addSubCommand(new PrepareModelCommand());
        addSubCommand(new PrepareViewCommand());
        
        // Remove the command because it never be called more than once
      //  getFacade().removeCommand(ApplicationConstants.STARTUP);
    }
    
    @Override
    public void execute(INotification notification) {
    	super.execute(notification);
    	 sendNotification(ApplicationConstants.READY);
    }
}
