package cyclist.view.component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import cyclist.model.vo.DnD;
import cyclist.view.tool.Tool;

public class ToolsPane extends TitledPane {
	
	private VBox _vbox;
	
	public ToolsPane() {
		init();
	}
	
	public void setTools(List<Tool> tools) {
		Collections.sort(tools, new Comparator<Tool>() {
			public int compare(Tool a, Tool b) {
				return a.getName().compareTo(b.getName());
			}
		});
		
		for (final Tool tool : tools) {
			final Image icon = tool.getIcon();
			final Label title = new Label(tool.getName(), new ImageView(icon));
			title.getStyleClass().add("tools-entry");
			
			title.setOnDragDetected(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					Dragboard db = title.startDragAndDrop(TransferMode.COPY);
					
					ClipboardContent content = new ClipboardContent();
					content.putString("foo");
					
					content.put( DnD.TOOL_FORMAT, tool.getName());
					content.putImage(icon);
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
