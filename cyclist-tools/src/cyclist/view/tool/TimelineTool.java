package cyclist.view.tool;

import javafx.scene.image.Image;
import org.puremvc.java.multicore.patterns.mediator.Mediator;
import cyclist.view.component.View;
import cyclist.view.tool.view.TimelineView;
import cyclist.view.tool.mediator.TimelineMediator;

public class TimelineTool implements Tool {

	@Override
	public Image getIcon() {
		return Resources.getIcon("chart_bar");
	}

	@Override
	public String getName() {
		return "Timeline";
	}

	@Override
	public View getView() {
		View view = new TimelineView();
		return view;
	}

	@Override
	public Mediator getMediator() {
		return new TimelineMediator();
	}

}
