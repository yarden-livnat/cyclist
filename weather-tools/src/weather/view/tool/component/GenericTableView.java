package weather.view.tool.component;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.TableViewBuilder;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import cyclist.model.filter.Param;
import cyclist.model.vo.Table;
import cyclist.model.vo.Table.TableRow;
import cyclist.view.component.View;

public class GenericTableView extends View {

	private TableView<TableRow> _tableView;
	private ObjectProperty<Table> _tableProperty = new SimpleObjectProperty<>();
	
	public GenericTableView() {
		super();
		init();
	}
	
	public ObjectProperty<Table> dataProperty() {
		return _tableProperty;
	}
	
	public Table getData() {
		return _tableProperty.get();
	}
	
	public void setData(Table table) {
		dataProperty().set(table);
	}
	
	private void init() {
		_tableView = TableViewBuilder.<TableRow>create()
				.columnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY)
				.build();
		
		setContent(_tableView);
		VBox.setVgrow(_tableView, Priority.NEVER);
		
		_tableProperty.addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable observable) {
				adjustTable();
				if (_tableProperty.get() != null) {
					System.out.println(_tableProperty.get().getRows().size()+" rows");
					_tableView.setItems(FXCollections.observableArrayList(_tableProperty.get().getRows()));
				}
			}
		});
	}

	private void adjustTable() {
		System.out.println("adjust table");
		_tableView = TableViewBuilder.<TableRow>create()
				.columnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY)
				.build();
		
		Table table = _tableProperty.get();
		if (table != null) {
			System.out.println(table.getNumColumns()+" cols");
			for (int i=0; i<table.getNumColumns(); i++) {
				TableColumn<TableRow, ?> col;
				System.out.println("col ["+i+"]: "+table.getHeader(i)+"  type: "+table.getType(i));
				switch (table.getType(i)) {
				case INT:			
					 col = this.<Integer>createColumn(table.getHeader(i), Param.Type.NONE, i);
					break;
				case REAL:
					 col = this.<Double>createColumn(table.getHeader(i), Param.Type.NONE, i);
					break;
				default:
					 col = this.<String>createColumn(table.getHeader(i), Param.Type.NONE, i);
					break;
				}
				_tableView.getColumns().add(col);
			}
		} else {
			System.out.println("null table");
		}
		setContent(_tableView);		
	}
	
	private <T> TableColumn<TableRow, T> createColumn(String title, final Param.Type type, final int index) {
		return TableColumnBuilder.<TableRow, T>create()
				.text(title)
				.cellValueFactory(new Callback<CellDataFeatures<TableRow, T>, ObservableValue<T>>() {
				     public ObservableValue<T> call(CellDataFeatures<TableRow, T> item) {
				         // p.getValue() returns the Person instance for a particular TableView row
				         return new ReadOnlyObjectWrapper(item.getValue().value[index]);
				     }
				})
				.cellFactory(new Callback<TableColumn<TableRow,T>, TableCell<TableRow,T>>() {
					@Override
					public TableCell<TableRow, T> call(TableColumn<TableRow, T> item) {
						return new TableRowCell<T>(type, index);
					}
			
				})
				.build();
	}
	
	
	class TableRowCell<T> extends TableCell<TableRow, T> {
		
		private Label _label; 
		private int _index;
		private boolean first = true;
		
		TableRowCell(Param.Type type, int index) {
			_index = index;
			_label = new Label("");
			_label.setAlignment(Pos.CENTER);
			setGraphic(_label);
			
			
//			_label.setOnDragDetected(new EventHandler<MouseEvent>() {
//				public void handle(MouseEvent event) {
//					Dragboard db = _label.startDragAndDrop(TransferMode.COPY);
//					
//					Param.Type ptype = Type.TableRow_TYPE;
////						Object value = getItem();
//					
//					TableRow TableRow = getTableView().getItems().get(getIndex());
//					
//					switch (TableRow.getTableRow()) {
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
