/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pnnl.cyclist.controller;

import javafx.scene.Node;
import org.puremvc.java.multicore.patterns.facade.Facade;

import pnnl.cyclist.controller.command.StartupCommand;

/**
 * Cyclist facade
 * Issue the START notification when the application is ready  
 * @author yarden
 */
public class ApplicationFacade extends Facade {
    
    /**
     * Key of this facade.
     */
    public static final String NAME = "ApplicationFacade";
        
    private static ApplicationFacade instance = null;
    
    /**
    * Constructor.
    */
    protected ApplicationFacade() {
    	super(NAME);
    }
    
    /**
    * get the instance.
    * @return the singleton
    */
    public static ApplicationFacade getInstance() {
        if (instance == null) {
            instance = new ApplicationFacade();
        }
        return instance;
    }

    /**
     * Start the application.
     */
    public final void startup(Node root) {
        registerCommand(ApplicationConstants.STARTUP, new StartupCommand());
        sendNotification(ApplicationConstants.STARTUP, root);    
    }
    
}
