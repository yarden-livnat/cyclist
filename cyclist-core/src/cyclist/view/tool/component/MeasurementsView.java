package cyclist.view.tool.component;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.TableViewBuilder;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import cyclist.model.filter.Param;
import cyclist.model.vo.Weather;
import cyclist.view.component.View;

public class MeasurementsView extends View {
	
	private TableView<Weather> _table;
	
	public MeasurementsView() {
		super();
		init();
	}
	
	public ObjectProperty<ObservableList<Weather>> dataProperty() {
		return _table.itemsProperty();
	}
	
	public ObservableList<Weather> getData() {
		return dataProperty().get();
	}
	
	public void setData(ObservableList<Weather> list) {
		dataProperty().set(list);
	}
	
	@SuppressWarnings("unchecked")
	private void init() {
		_table = TableViewBuilder.<Weather>create()
				.columnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY)
				.columns(
						this.<String>createColumn("Station", Param.Type.NONE, "name"),
						this.<Float>createColumn("Station.x", Param.Type.NONE, "stationX"),
						this.<Float>createColumn("Station.y", Param.Type.NONE, "stationY"),
						this.<Float>createColumn("X", Param.Type.NONE, "x"),
						this.<Float>createColumn("Y", Param.Type.NONE, "y"),
						this.<String>createColumn("State", Param.Type.NONE, "stateName"),
						this.<String>createColumn("County", Param.Type.NONE, "countyName")
				)
				.build();
		
		setContent(_table);
		VBox.setVgrow(_table, Priority.NEVER);
	}

	private <T> TableColumn<Weather, T> createColumn(String title, final Param.Type type, final String field) {
		return TableColumnBuilder.<Weather, T>create()
				.text(title)
				.cellValueFactory(new PropertyValueFactory<Weather, T>(field))
				.cellFactory(new Callback<TableColumn<Weather,T>, TableCell<Weather,T>>() {
					@Override
					public TableCell<Weather, T> call(TableColumn<Weather, T> item) {
						return new WeatherCell<T>(type, field);
					}
			
				})
				.build();
	}
	
	class WeatherCell<T> extends TableCell<Weather, T> {
		
		private Label _label; 
		private String _field;
		private boolean first = true;
		
		WeatherCell(Param.Type type, String field) {
			_field = field;
			_label = new Label("");
			_label.setAlignment(Pos.CENTER);
			setGraphic(_label);
			
			
//			_label.setOnDragDetected(new EventHandler<MouseEvent>() {
//				public void handle(MouseEvent event) {
//					Dragboard db = _label.startDragAndDrop(TransferMode.COPY);
//					
//					Param.Type ptype = Type.Weather_TYPE;
////						Object value = getItem();
//					
//					Weather Weather = getTableView().getItems().get(getIndex());
//					
//					switch (Weather.getWeather()) {
//					case "Facility":
//						if ("id".equals(_field)) ptype = Type.FACILITY;
//						else if ("Type".equals(_field)) ptype = Type.FACILITY_TYPE;
//						break;		
//					case "Institute":
//						if ("id".equals(_field)) ptype = Type.INSTITUTE;
//						else if ("Type".equals(_field)) ptype = Type.INSTITUTE_TYPE;
//						break;
//					case "Region":
//						if ("id".equals(_field)) ptype = Type.REGION;
//						else if ("Type".equals(_field)) ptype = Type.REGION_TYPE;
//						break;
//					case "Market":
//						if ("id".equals(_field)) ptype = Type.MARKET;
//						else if ("Type".equals(_field)) ptype = Type.MARKET_TYPE;
//						break;
//					}
//					
//					ClipboardContent content = new ClipboardContent();
//					content.put( DnD.TYPE_FORMAT, ptype);
//					content.put( DnD.FIELD_FORMAT, _field);
//					content.put( DnD.VALUE_FORMAT, getItem());
//					content.put( DnD.NAME_FORMAT, getItem().toString());
//					content.putImage(Resources.getIcon("filter.png"));
//					db.setContent(content);
//					
//					event.consume();
//				}
//			});
		}
		
		@Override
		protected void updateItem(T item, boolean empty){
			super.updateItem(item,empty);
			if (first) {
				first = false;
				_label.prefWidthProperty().bind(getTableColumn().widthProperty());
			}
			_label.setText(item != null ? item.toString() : "");
		}
	}

}
