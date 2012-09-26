package pnnl.cyclist.controller.command;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import pnnl.cyclist.CyclistNames;
import pnnl.cyclist.model.proxy.CyclistDataStream;
import pnnl.cyclist.model.proxy.DataSourcesProxy;


public class DefaultDataSourceommand extends SimpleCommand {

	@Override
    public void execute(INotification notification) {
		DataSourcesProxy proxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
		
		proxy.setDefaultDataStream((CyclistDataStream) notification.getBody());
	}
}
