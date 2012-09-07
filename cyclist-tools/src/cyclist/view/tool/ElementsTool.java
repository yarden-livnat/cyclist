package cyclist.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import cyclist.view.component.View;
import cyclist.view.mediator.CyclistMediator;
import cyclist.view.tool.Tool;
import cyclist.view.tool.view.ElementView;

public class ElementsTool implements Tool {

	@Override
	public Image getIcon() {
		return Resources.getIcon("unknown");
	}

	@Override
	public String getName() {
		return "Elements";
	}

	@Override
	public View getView() {
		View view = new ElementView();
		view.setParam("Elements");
		return view;
	}

	@Override
	public Mediator getMediator() {
		return new CyclistMediator();
	}

}
