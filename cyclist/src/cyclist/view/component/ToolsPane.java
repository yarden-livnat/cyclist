package cyclist.view.component;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import cyclist.Resources;
import cyclist.model.vo.DnD;
import cyclist.model.vo.ToolInfo;

public class ToolsPane extends TitledPane {
	
	private VBox _vbox;
	
	public ToolsPane() {
		init();
	}
	
	public void setItems(ObservableList<ToolInfo> items) { 
		for (final ToolInfo info: items) {
			ImageView icon = new ImageView(Resources.getIcon(info.icon));
			final Label title = new Label(info.title, icon);
			title.getStyleClass().add("tools-entry");
			
			title.setOnDragDetected(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					Dragboard db = title.startDragAndDrop(TransferMode.COPY);
					
					ClipboardContent content = new ClipboardContent();
					content.putString(info.view);
					
					content.put( DnD.TOOL_FORMAT, info);
					content.putImage(Resources.getIcon(info.icon));
					db.setContent(content);
					
					event.consume();
				}
			});
			
			_vbox.getChildren().add(title);
		}
	}
	
	private void init() {
		
		setText("Tools");
		
		_vbox = VBoxBuilder.create()
				.styleClass("tools-pane")
				.build();
		setContent(_vbox);
	}
}
