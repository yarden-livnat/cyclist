package cyclist.view.component;

import java.util.EnumSet;

import cyclist.model.filter.Param;
import cyclist.model.vo.Details;
import cyclist.model.vo.Details.Type;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.TitledPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.TextBuilder;
import cyclist.model.vo.DnD;


public class DetailsPane extends TitledPane {
	public static final String TITLE = "Details";
	
	private Details _details = new Details(Details.Type.NONE, Param.Type.NONE);
	private Label _main;
	private Label _src;
	private Label _dest;
	
	private ObjectProperty<EventHandler<ActionEvent>> actionPropery = new SimpleObjectProperty<EventHandler<ActionEvent>>();
	 
	
	public DetailsPane() {
		setText(TITLE);
		setPrefWidth(100);
		
		GridPane grid = GridPaneBuilder.create()
					.vgap(3)
					.build();
		
		grid.add(TextBuilder.create().text("").styleClass("filter-area-title").build(), 0, 1);
		grid.add(_main = createEntry(Param.GENERAL), 1, 1);
		
		// src
		grid.add(TextBuilder.create().text("src:").styleClass("filter-area-title").build(), 0, 2);
		grid.add(_src = createEntry(Param.SRC_DEST), 1, 2);
		
		// dest
		grid.add(TextBuilder.create().text("dest:").styleClass("filter-area-title").build(), 0, 3);
		grid.add(_dest = createEntry(Param.SRC_DEST), 1, 3);
		
		VBox vbox = VBoxBuilder.create()
				.styleClass("tools-pane")
				.children(
					grid
				)
				.build();
		setContent(vbox);
	}
	
	/**
	 * getDetails
	 * @return
	 */
	public Details getDetails() {
		return new Details(_details.type, _details.param);
	}
	
	/**
	 * ActionProperty
	 * @return
	 */
	public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
		return actionPropery;
	}
	
	public EventHandler<ActionEvent> getOnAction() {
		return actionPropery.get();
	}
	
	public void setOnAction(EventHandler<ActionEvent> handler) {
		actionPropery.set(handler);
	}
	
	/*
	 * createEntry
	 */
	private Label createEntry(final EnumSet<Param.Type> acceptableValues) {
		final Label label = LabelBuilder.create().styleClass("details-area").prefWidth(100).maxHeight(10).build();
		
		label.setOnDragEntered(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {	
				setStyle("-fx-border-style: solid");
				event.consume();
			}
		});
		
		label.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				if (event.getDragboard().getContent(DnD.DETAILS_FORMAT) != null
					&& acceptableValues.contains((Param.Type)event.getDragboard().getContent(DnD.DETAILS_FORMAT))) 
				{
					event.acceptTransferModes(TransferMode.COPY);
				}
				event.consume();
			}
		});	
		
		label.setOnDragExited(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				setStyle("-fx-border-style: none");
				event.consume();
			}
		});
		
		label.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				_details.param = (Param.Type) event.getDragboard().getContent(DnD.DETAILS_FORMAT);
				
				if (label == _src) 
					_details.type = Type.SRC;
				else if (label == _dest)
					_details.type = Type.DEST;
				else
					_details.type = Type.GENERAL;
			 
				update();
				
				event.setDropCompleted(true);				
				event.consume();
			}
		});
		
		label.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!(" ".equals(label.getText()))) {
					_details.type = Type.NONE;
					_details.param = Param.Type.NONE;
					update();
				}
			}
			
		});
		
		return label;
	}
	
	private void update() {
		String text = _details.param.toString();
		_main.setText( _details.type != Type.GENERAL ? "" : text);
		_src.setText( _details.type != Type.SRC ? "" : text);
		_dest.setText( _details.type != Type.DEST ? "" : text);
		
		if (getOnAction() != null) {
		 	getOnAction().handle(new ActionEvent());
		}
	}
}
