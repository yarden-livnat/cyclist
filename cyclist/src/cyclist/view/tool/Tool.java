package cyclist.view.tool;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import javafx.scene.image.Image;
import cyclist.view.component.View;

public interface Tool {

	Image getIcon();
	String getName();
	View getView();
	Mediator getMediator();
}
