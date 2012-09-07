package cyclist.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import cyclist.view.component.View;
import cyclist.view.mediator.CyclistMediator;
import cyclist.view.tool.Tool;
import cyclist.view.tool.mediator.FacilityMediator;
import cyclist.view.tool.view.FacilityView;

public class FacilityTool implements Tool {

	@Override
	public Image getIcon() {
		return Resources.getIcon("table");
	}

	@Override
	public String getName() {
		return "Facility";
	}

	@Override
	public View getView() {
		View view = new FacilityView();
		view.setParam("Facilities");
		return view;
	}

	@Override
	public Mediator getMediator() {
		CyclistMediator m = new FacilityMediator();
		m.setParam("Facility");
		return m;
	}

}
