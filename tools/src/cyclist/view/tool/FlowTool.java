package cyclist.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import cyclist.view.component.View;
import cyclist.view.tool.Tool;
import cyclist.view.tool.mediator.FlowMediator;
import cyclist.view.tool.view.FlowView;

public class FlowTool implements Tool {

	@Override
	public Image getIcon() {
		return Resources.getIcon("chart_bar");
	}

	@Override
	public String getName() {
		return "Flow";
	}

	@Override
	public View getView() {
		View view = new FlowView();
		view.setParam("Material Flow");
		return view;
	}

	@Override
	public Mediator getMediator() {
		return new FlowMediator();
	}

}
