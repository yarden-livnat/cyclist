package pnnl.cyclist.view.tool;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import pnnl.cyclist.view.component.View;

import javafx.scene.image.Image;

public interface Tool {

	Image getIcon();
	String getName();
	View getView();
	Mediator getMediator();
}
