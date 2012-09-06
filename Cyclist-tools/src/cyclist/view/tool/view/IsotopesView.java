package cyclist.view.tool.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import cyclist.Resources;
import cyclist.model.filter.Param;
import cyclist.model.vo.DnD;
import cyclist.model.vo.Isotope;
import cyclist.view.component.View;

public class IsotopesView extends View {

	private VBox _vbox;
	private ObjectProperty<ObservableList<Isotope>> _itemsProperty = new SimpleObjectProperty<>();
	
	public IsotopesView() {
		super();
		init();
	}
	
	public ObjectProperty<ObservableList<Isotope>> itemsProperty() {
		return _itemsProperty;
	}
	
	public ObservableList<Isotope> getItems() {
		return _itemsProperty.get();
	}
	
	public void setItems(ObservableList<Isotope> list) {
		_itemsProperty.set(list);
	}
	
	private void init() {
		setPrefWidth(100);
		setPrefHeight(50);
		_vbox = VBoxBuilder.create()
				.build();
		
		setContent(_vbox);
		
		final VBox vbox = _vbox;
		_itemsProperty.addListener(new ChangeListener<ObservableList<Isotope>>() {

			@Override
			public void changed(
					ObservableValue<? extends ObservableList<Isotope>> observable,
					ObservableList<Isotope> oldValue,
					ObservableList<Isotope> newValue) 
			{
				_vbox.getChildren().clear();
				if (newValue != null) {
					for (final Isotope iso : newValue) {
						final Label label = LabelBuilder.create().text(iso.toString()).build();
						vbox.getChildren().add(label);
						label.setOnDragDetected(new EventHandler<MouseEvent>() {
							public void handle(MouseEvent event) {
								Dragboard db = label.startDragAndDrop(TransferMode.COPY);
								ClipboardContent content = new ClipboardContent();
								content.put( DnD.TYPE_FORMAT, Param.Type.ISOTOPE);
								content.put( DnD.VALUE_FORMAT, iso.getCode());
								content.put( DnD.NAME_FORMAT, iso.toString());
								content.putImage(Resources.getIcon("filter.png"));
								db.setContent(content);
								
								event.consume();
							}
						});
					}
				}
			}

		});
	}
}
