package cyclist.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import cyclist.view.component.View;
import cyclist.view.tool.mediator.AgentMediator;
import cyclist.view.tool.view.AgentView;

public class AgentsTool implements Tool {

	@Override
	public Image getIcon() {
		return Resources.getIcon("table");
	}

	@Override
	public String getName() {
		return "Agents";
	}

	@Override
	public View getView() {
		View view = new AgentView();
		view.setParam("Agents");
		return view;
	}

	@Override
	public Mediator getMediator() {
		return new AgentMediator();
	}

}
