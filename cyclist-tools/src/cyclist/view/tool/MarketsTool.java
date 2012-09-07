package cyclist.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import cyclist.view.component.View;
import cyclist.view.mediator.CyclistMediator;
import cyclist.view.tool.Tool;
import cyclist.view.tool.mediator.AgentMediator;
import cyclist.view.tool.view.AgentView;

public class MarketsTool implements Tool {

	@Override
	public Image getIcon() {
		return Resources.getIcon("table");
	}

	@Override
	public String getName() {
		return "Markets";
	}

	@Override
	public View getView() {
		View view = new AgentView();
		view.setParam("Markets");
		return view;
	}

	@Override
	public Mediator getMediator() {
		CyclistMediator m = new AgentMediator();
		m.setParam("Market");
		return m;
	}

}
