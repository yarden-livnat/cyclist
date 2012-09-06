package cyclist.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import cyclist.view.component.View;
import cyclist.view.tool.Tool;
import cyclist.view.tool.mediator.IsotopesMediator;
import cyclist.view.tool.view.IsotopesView;

public class IsotopesTool implements Tool {

	@Override
	public Image getIcon() {
		return Resources.getIcon("unknown");
	}

	@Override
	public String getName() {
		return "Isotopes";
	}

	@Override
	public View getView() {
		View view = new IsotopesView();
		view.setParam("Active Isotopes");
		return view;
	}

	@Override
	public Mediator getMediator() {
		return new IsotopesMediator();
	}

}
