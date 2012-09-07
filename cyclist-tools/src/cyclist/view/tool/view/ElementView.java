package cyclist.view.tool.view;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.FlowPaneBuilder;
import cyclist.Resources;
import cyclist.model.filter.Param;
import cyclist.model.vo.DnD;
import cyclist.model.vo.Element;
import cyclist.view.component.View;

public class ElementView extends View {
	
	public ElementView() {
		super();
		init();
	}
	
	private void init() {
		prefWidth(300);
		prefHeight(20);
		setMaxHeight(100);
		setStyle("-fx-background-color: #ffffd0");
		FlowPane pane = FlowPaneBuilder.create()
				.hgap(5)
				.vgap(5)
				.prefWrapLength(250)
//				.style("-fx-background-color: #66faed")
				.build();
		
		for (final Element e : Element.values()) {
			final String name = e.toString();
			final Label entry = LabelBuilder.create()
					.text(name)
					.styleClass("element-label")
					.build();
			pane.getChildren().add(entry);
			
			entry.setOnDragDetected(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					Dragboard db = entry.startDragAndDrop(TransferMode.COPY);
					
					ClipboardContent content = new ClipboardContent();
					
					content.put( DnD.TYPE_FORMAT, Param.Type.ELEMENT);
					content.put( DnD.VALUE_FORMAT, e.number());
					content.put( DnD.NAME_FORMAT, e.toString());
					content.putImage(Resources.getIcon("filter"));
					db.setContent(content);
					
					event.consume();
				}
			});
		}
		
		setContent(pane);
	}
}
