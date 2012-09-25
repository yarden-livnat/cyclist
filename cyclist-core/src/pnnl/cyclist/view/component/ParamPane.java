package pnnl.cyclist.view.component;

import pnnl.cyclist.Resources;
import pnnl.cyclist.model.filter.Param;
import pnnl.cyclist.model.filter.Param.Type;
import pnnl.cyclist.model.vo.DnD;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;

public class ParamPane extends TitledPane {
	
	private static final Param.Type[] types = {
		Type.FACILITY, Type.FACILITY_TYPE, 
		Type.INSTITUTE, Type.REGION, 
		Type.MARKET, Type.MARKET_TYPE,
		Type.ELEMENT, Type.ISOTOPE
	};
	
	private VBox _vbox;

	public ParamPane() {	
		setText("Details");
		_vbox = VBoxBuilder.create()
				.styleClass("tools-pane")
				.build();
		setContent(_vbox);
		
		for (final Param.Type type : types) {
			final Label label = LabelBuilder.create()
					.styleClass("tools-entry")
					.text(type.toString())
					.build();
			_vbox.getChildren().add(label);
			
			label.setOnDragDetected(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					Dragboard db = label.startDragAndDrop(TransferMode.COPY);
					
					ClipboardContent content = new ClipboardContent();
					
					content.put( DnD.DETAILS_FORMAT, type);
					content.putImage(Resources.getIcon("filter"));
					db.setContent(content);
					
					event.consume();
				}
			});
		}
		
	}
	
}
