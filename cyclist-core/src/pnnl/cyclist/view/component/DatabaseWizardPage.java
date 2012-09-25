package pnnl.cyclist.view.component;

import pnnl.cyclist.model.vo.CyclistDataSource;
import javafx.scene.Node;

public interface DatabaseWizardPage {

	String getURL();
	CyclistDataSource getDataSource();
	Node getNode();
}
