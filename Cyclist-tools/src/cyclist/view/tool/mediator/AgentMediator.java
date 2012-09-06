package cyclist.view.tool.mediator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import org.puremvc.java.multicore.interfaces.INotification;

import cyclist.CyclistNames;
import cyclist.controller.ApplicationConstants;
import cyclist.model.proxy.DataSourcesProxy;
import cyclist.model.vo.Agent;
import cyclist.model.vo.SimulationDataStream;
import cyclist.view.mediator.CyclistMediator;
import cyclist.view.tool.view.AgentView;

public class AgentMediator extends CyclistMediator {
	
	private String _agentType;
	private SimulationDataStream _ds;
	
	public AgentMediator() {
		super(CyclistNames.AGENT_MEDIATOR, null);
	}
	
	@Override
	public void setParam(String param) {
		_agentType = param;
	}
	
	@Override
	public void setViewComponent(Object view) {
		super.setViewComponent(view);
		if (view != null)
			getViewComponent().agentsProperty().addListener(new ChangeListener<ObservableList<Agent>>() {

				@Override
				public void changed(
						ObservableValue<? extends ObservableList<Agent>> arg0,
						ObservableList<Agent> oldValue, ObservableList<Agent> newValue) 
				{
					if (oldValue == null)
						getViewComponent().setWaiting(false);
				}
				
			});
	}
	
	@Override
	public AgentView getViewComponent() {
		return (AgentView) super.getViewComponent();
	}
	
	@Override
    public String[] listNotificationInterests() {
		return new String[]{ 
				ApplicationConstants.MEDIATOR_INIT,
				ApplicationConstants.DEFAULT_SIMULATION_SOURCE,
		};
	}
	
	@Override
    public void handleNotification(INotification notification) {
    	switch (notification.getName()) {
    	case ApplicationConstants.MEDIATOR_INIT:
    		DataSourcesProxy proxy = (DataSourcesProxy) getFacade().retrieveProxy(CyclistNames.DATA_SOURCES_PROXY);
    		_ds = proxy.getDefaultSimulationDataStream();
    		fetchData();
    		break;
    	case ApplicationConstants.DEFAULT_SIMULATION_SOURCE:
    		_ds = (SimulationDataStream) notification.getBody();
    		fetchData();
    		break;
    	};
	}
	
	private void fetchData() {
		
		if (_ds != null) {
			getViewComponent().setWaiting(true);
    		getViewComponent().agentsProperty().bind(_ds.getAgents(_agentType));
		}
	}
}
