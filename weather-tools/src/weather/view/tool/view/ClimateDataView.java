package weather.view.tool.view;

import pnnl.cyclist.model.filter.Param;
import pnnl.cyclist.view.component.View;
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

public class ClimateDataView extends View {

//	private TableView<ClimateRowData> _table;
//	
//	public ClimateDataView() {
//		super();
//		init();
//	}
//	
//	public ObjectProperty<ObservableList<ClimateRowData>> dataProperty() {
//		return _table.itemsProperty();
//	}
//	
//	public ObservableList<ClimateRowData> getData() {
//		return dataProperty().get();
//	}
//	
//	public void setData(ObservableList<ClimateRowData> list) {
//		dataProperty().set(list);
//	}
//	
//	@SuppressWarnings("unchecked")
//	private void init() {
//		_table = TableViewBuilder.<ClimateRowData>create()
//				.columnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY)
//				.columns(
//						this.<Integer>createColumn("Year", Param.Type.NONE, "year"),
//						this.<Integer>createColumn("Month", Param.Type.NONE, "month"),
//						this.<Integer>createColumn("Day", Param.Type.NONE, "day"),
//						this.<Integer>createColumn("Hour", Param.Type.NONE, "hour")
//				)
//				.build();
//		
//		setContent(_table);
//		VBox.setVgrow(_table, Priority.NEVER);
//	}
//
//	private <T> TableColumn<ClimateRowData, T> createColumn(String title, final Param.Type type, final String field) {
//		return TableColumnBuilder.<ClimateRowData, T>create()
//				.text(title)
//				.cellValueFactory(new PropertyValueFactory<ClimateRowData, T>(field))
//				.cellFactory(new Callback<TableColumn<ClimateRowData,T>, TableCell<ClimateRowData,T>>() {
//					@Override
//					public TableCell<ClimateRowData, T> call(TableColumn<ClimateRowData, T> item) {
//						return new ClimateRowDataCell<T>(type, field);
//					}
//			
//				})
//				.build();
//	}
//	
//	
//	class ClimateRowDataCell<T> extends TableCell<ClimateRowData, T> {
//		
//		private Label _label; 
//		private String _field;
//		private boolean first = true;
//		
//		ClimateRowDataCell(Param.Type type, String field) {
//			_field = field;
//			_label = new Label("");
//			_label.setAlignment(Pos.CENTER);
//			setGraphic(_label);
//			
			
//			_label.setOnDragDetected(new EventHandler<MouseEvent>() {
//				public void handle(MouseEvent event) {
//					Dragboard db = _label.startDragAndDrop(TransferMode.COPY);
//					
//					Param.Type ptype = Type.ClimateRowData_TYPE;
////						Object value = getItem();
//					
//					ClimateRowData ClimateRowData = getTableView().getItems().get(getIndex());
//					
//					switch (ClimateRowData.getClimateRowData()) {
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
//		}
//		
//		@Override
//		protected void updateItem(T item, boolean empty){
//			super.updateItem(item,empty);
//			if (first) {
//				first = false;
//				_label.prefWidthProperty().bind(getTableColumn().widthProperty());
//			}
//			_label.setText(item != null ? item.toString() : "");
//		}
//	}
}
