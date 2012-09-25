package cyclist.controller.command;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import cyclist.CyclistNames;
import cyclist.model.proxy.DataSourcesProxy;
import cyclist.model.vo.SimulationDataStream;

public class DefaultSimulationCommand extends SimpleCommand {

	@Override
    public void execute(INotification notification) {
		DataSourcesProxy proxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
		
		proxy.setDefaultSimulationDataStream((SimulationDataStream) notification.getBody());
	}
}
