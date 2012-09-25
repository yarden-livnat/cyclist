/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cyclist.controller.command;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import cyclist.CyclistNames;
import cyclist.model.proxy.DataSourcesProxy;
import cyclist.model.proxy.WeatherProxy;

/**
 *
 * @author yarden
 */
public class PrepareModelCommand extends SimpleCommand {
   /**
    * @param notification
    *   the <code>INotification</code> to handle.
    */
    @Override
    public void execute(INotification notification) {
        DataSourcesProxy dsp = new DataSourcesProxy();
        getFacade().registerProxy(dsp);
        
        WeatherProxy weather = new WeatherProxy(CyclistNames.WEATHER_PROXY);
        getFacade().registerProxy(weather);
    }    
}
