package pnnl.cyclist.view.component;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class Spring extends Region {

	public Spring() {
		super();
		
		setPrefWidth(10);
		setMinWidth(1);
		HBox.setHgrow(this, Priority.ALWAYS);
	}
}
