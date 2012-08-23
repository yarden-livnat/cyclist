package cyclist.view.component;

import javafx.scene.Node;
import cyclist.model.vo.CyclistDataSource;

public interface DatabaseWizardPage {

	String getURL();
	CyclistDataSource getDataSource();
	Node getNode();
}
