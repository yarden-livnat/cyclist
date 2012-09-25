package pnnl.cyclist.view.component;

import java.util.HashMap;
import java.util.Map;

import pnnl.cyclist.Resources;
import pnnl.cyclist.model.proxy.CyclistDataStream.State;
import pnnl.cyclist.model.vo.CyclistDataSource;
import pnnl.cyclist.view.event.CyclistDataSourceEvent;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleButtonBuilder;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Window;

public class DataPane extends TitledPane {
	private VBox _vbox;
	private Info _current;
	private ToggleGroup _group = new ToggleGroup();
	ObjectProperty<EventHandler<CyclistDataSourceEvent>> _actionProperty = new SimpleObjectProperty<>();
	
	private Map<String, Info> _info = new HashMap<>();
	private Map<State, Image> _icons = new HashMap<>();
	
	public ObjectProperty<EventHandler<CyclistDataSourceEvent>> onActionProperty() {
		return _actionProperty;
	}
	
	public EventHandler<CyclistDataSourceEvent> getOnAction() {
		return _actionProperty.get();
	}
	
	public void setOnAction(EventHandler<CyclistDataSourceEvent> handler) {
		_actionProperty.set(handler);
	}
	/**
	 * Constructor
	 */
	public DataPane() {
		setText("Data Sources");
		
		_vbox = VBoxBuilder.create()
				.styleClass("tools-pane")
				.build();
		setContent(_vbox);
		
		_icons.put(State.NOT_READY, Resources.getIcon("database"));
		_icons.put(State.CONNECTING, Resources.getIcon("connecting"));
		_icons.put(State.OK, Resources.getIcon("ok"));
		_icons.put(State.WARNING, Resources.getIcon("warning"));
		_icons.put(State.ERROR, Resources.getIcon("error"));
		
	}
	
	public String getCurrent() {
		return _current != null ? _current.name : "";
	}
	
	public ObjectProperty<State> getStatus() {
		return getStatus(getCurrent());
	}
	
	public ObjectProperty<State> getStatus(String name) {
		Info info = _info.get(name);
		return info.state;
	}
	
	public void addItems(CyclistDataSource[] items) {
		for (CyclistDataSource item : items) {
			final ToggleButton button = ToggleButtonBuilder.create()
					.styleClass("flat-toggle-button")
					.text(item.getName())
					.toggleGroup(_group)
					.graphic(new ImageView(Resources.getIcon("database")))
					.build();
			button.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.getButton() != MouseButton.PRIMARY) return;
					
					Info info = _info.get(button.getText());
					if (_current != info) {
						_current = info;
						if (getOnAction() != null) {
						 	getOnAction().handle(new CyclistDataSourceEvent(CyclistDataSourceEvent.SELECT, _current.name));
						}	
					}
				}
			});
			
			button.setContextMenu(createContextMenu(item.getName()));
			
			_vbox.getChildren().add(button);
			Info info = new Info(item.getName(), button, item);
			info.state.addListener(new ChangeListener<State>() {

				@Override
				public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
					ImageView iv = (ImageView) button.getGraphic();
					iv.setImage(_icons.get(newValue));
				}
			});
			_info.put(item.getName(), info);	
		}
	}

	public void removeItem(String item) {
		Info info = _info.get(item);
		_info.remove(item);
		if (_current == info)
			_current = null;
		
		_vbox.getChildren().remove(info.button);
		
		if (getOnAction() != null) {
		 	getOnAction().handle(new CyclistDataSourceEvent(CyclistDataSourceEvent.REMOVE, info.name));
		}
	}
	
	
	private void editItem(final String item, Window window) {
		Info info = _info.get(item);
		ObjectProperty<CyclistDataSource> ds = DatabaseWizard.showWizard(window, info.ds); 
		ds.addListener(new ChangeListener<CyclistDataSource>() {
			@Override
			public void changed(
					ObservableValue<? extends CyclistDataSource> observable,
					CyclistDataSource oldValue, CyclistDataSource newValue) 
			{
				if (newValue != null) {
					getOnAction().handle(new CyclistDataSourceEvent(CyclistDataSourceEvent.UPDATED, newValue));
				}
			}
		});	
	}
	private ContextMenu createContextMenu(final String item) {
		final ContextMenu menu = new ContextMenu();
		
		MenuItem edit = new MenuItem("Edit");
		edit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MenuItem mi = (MenuItem) event.getSource();
				editItem(item, mi.getParentPopup().getOwnerWindow());
			}
		});
		menu.getItems().add(edit);
		
		MenuItem delete = new MenuItem("Delete");
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				removeItem(item);
			}
		});
		menu.getItems().add(delete);
		
		
		return menu;
	}
	
	class Info {
		String name;
		CyclistDataSource ds;
		ObjectProperty<State> state = new SimpleObjectProperty<State>(State.NOT_READY);
		ToggleButton button;
		
		public Info(String name, ToggleButton button, CyclistDataSource ds) {
			this.name = name;
			this.button = button;
			this.ds = ds;
		}
	}
}
