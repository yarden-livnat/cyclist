package cyclist.view.tool.component;

import java.util.EnumSet;
import java.util.List;
import java.util.Map.Entry;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import cyclist.model.filter.Param;
import cyclist.model.vo.DnD;
import cyclist.model.vo.FilterSet;

public class FilterSetView extends HBox {
	
	private FilterSet _set ;
	private EnumSet<Param.Type> _acceptableValues;
	private ObjectProperty<EventHandler<ActionEvent>> actionPropery = new SimpleObjectProperty<EventHandler<ActionEvent>>();
	 
	public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
		return actionPropery;
	}
	
	public EventHandler<ActionEvent> getOnAction() {
		return actionPropery.get();
	}
	
	public void setOnAction(EventHandler<ActionEvent> handler) {
		actionPropery.set(handler);
	}	
	
	/**
	 * Constructor
	 * @param type
	 * @param acceptableValue
	 */
	public FilterSetView(String type, EnumSet<Param.Type> acceptableValue) {
		_set = new FilterSet(type);
		_acceptableValues = acceptableValue;
		init();
	}
	
	public void setFilterSet(FilterSet set) {
		_set = set;
	}
	
	public FilterSet getFilterSet() {
		return _set;
	}
	
	private void init() {
		getStyleClass().add("filter-area");
		setSpacing(0);
		setPadding(new Insets(2));
		
		setOnDragEntered(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {	
				setStyle("-fx-border-color: #c0c0c0");
				event.consume();
			}
		});
		
		setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				if (event.getDragboard().getContent(DnD.TYPE_FORMAT) != null
					&& _acceptableValues.contains((Param.Type)event.getDragboard().getContent(DnD.TYPE_FORMAT))) 
				{
					event.acceptTransferModes(TransferMode.COPY);
				} 
				
				event.consume();
			}
		});	
		
		setOnDragExited(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				setEffect(null);
				setStyle("-fx-border-color: -fx-body-color");
				event.consume();
			}
		});
		
		setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				Param.Type type = (Param.Type) event.getDragboard().getContent(DnD.TYPE_FORMAT);
				String name = (String) event.getDragboard().getContent(DnD.NAME_FORMAT);
				Object value = event.getDragboard().getContent(DnD.VALUE_FORMAT);
				String field = (String) event.getDragboard().getContent(DnD.FIELD_FORMAT);
				if (_set.add(type, name, value)) {
					update();
				}

				event.setDropCompleted(true);				
				event.consume();
			}
		});
	}
	
	private void update() {
		getChildren().clear();
		
		boolean f = true;
		
		for (Entry<Param.Type, List<FilterSet.FilterEntry>> entry : _set.getEntries()) {
			if (f) f = false;
			else getChildren().add(new Text(" or "));
			
			if (entry.getValue().size() == 1) {
				Text type = new Text(entry.getKey()+"=");
				Label value = createFilterLabel(entry.getKey(), entry.getValue().get(0).name); 
				getChildren().addAll(type, value);
			} else {
				Text type = new Text(entry.getKey()+" in (");
				getChildren().add(type);
				boolean first = true;
				for (FilterSet.FilterEntry item : entry.getValue()) {
					if (first) first = false;
					else {
						getChildren().add(new Text(", "));
					}
					Label value = createFilterLabel(entry.getKey(), item.name); //new Text(item);
					getChildren().add(value);
				}
				getChildren().add(new Text(")"));
			}
		}
		
		if (getOnAction() != null) {
		 	getOnAction().handle(new ActionEvent());
		}
	}
	
	private Label createFilterLabel(final Param.Type key, final String name) {
		final Label label = new Label(name);
//		label.getStsyleClass().add("filter-label");
		
		label.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				label.setStyle("-fx-background-color: #d0d0d0");			
			}
			
		});
		
		label.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				label.setStyle("-fx-background-color: #f0f0f0");			
			}
			
		});
		
		label.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				_set.remove(key, name);
				update();
			}
		});
			
		return label;
	}
}
