
	package cyclist.view.tool.view;

	import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.TableViewBuilder;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import cyclist.Resources;
import cyclist.model.filter.Param;
import cyclist.model.vo.DnD;
import cyclist.model.vo.Facility;
import cyclist.view.component.View;

	public class FacilityView extends View {
		
		private TableView<Facility> _table;
			
		public ObjectProperty<ObservableList<Facility>> facilityProperty() {
			return _table.itemsProperty();
		}
		
		public FacilityView() {
			super();
			init();
		}
		
		@SuppressWarnings("unchecked")
		private void init() {	
			_table = TableViewBuilder.<Facility>create()
					.columnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY)
					.columns(
							this.<Integer>createColumn("ID", Param.Type.FACILITY, "id"),
							this.<String>createColumn("Type", Param.Type.FACILITY_TYPE, "Type"),
							this.<Integer>createColumn("Institute", Param.Type.INSTITUTE, "Institute"),
							this.<Integer>createColumn("Region", Param.Type.REGION, "Region"),
							TableColumnBuilder.<Facility, Integer>create().text("Start").cellValueFactory(new PropertyValueFactory<Facility, Integer>("start")).build(),
							TableColumnBuilder.<Facility, Integer>create().text("End").cellValueFactory(new PropertyValueFactory<Facility, Integer>("end")).build()
					)
					.build();

			setContent(_table);
			VBox.setVgrow(_table, Priority.NEVER);
		}
		
		
		private <T> TableColumn<Facility, T> createColumn(String title, final Param.Type type, final String field) {
			return TableColumnBuilder.<Facility, T>create()
					.text(title)
					.cellValueFactory(new PropertyValueFactory<Facility, T>(field))
					.cellFactory(new Callback<TableColumn<Facility,T>, TableCell<Facility,T>>() {
						@Override
						public TableCell<Facility, T> call(TableColumn<Facility, T> item) {
							return new FacilityCell<T>(type, field);
						}
				
					})
					.build();
		}
		
			
	}
		
	class FacilityCell<T> extends TableCell<Facility, T> {
		
		private Label _label; 
		private String _field;
		private Param.Type _type;
		private boolean first = true;
		
		FacilityCell(Param.Type type, String field) {
			_type = type;
			_field = field;
			_label = new Label("");
			_label.setAlignment(Pos.CENTER);
			setGraphic(_label);
			
			
			_label.setOnDragDetected(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					Dragboard db = _label.startDragAndDrop(TransferMode.COPY);
					
					ClipboardContent content = new ClipboardContent();
					content.put( DnD.TYPE_FORMAT, _type);
					content.put( DnD.FIELD_FORMAT, _field);
					content.put( DnD.VALUE_FORMAT, getItem());
					content.put( DnD.NAME_FORMAT, getItem().toString());
					content.putImage(Resources.getIcon("filter.png"));
					db.setContent(content);
					
					event.consume();
				}
			});
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

