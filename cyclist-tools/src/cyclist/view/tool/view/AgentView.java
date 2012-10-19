
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
import cyclist.model.filter.Param.Type;
import cyclist.model.vo.Agent;
import cyclist.model.vo.DnD;
import cyclist.view.component.View;

public class AgentView extends View {
	
	private TableView<Agent> _table;
	
	public ObjectProperty<ObservableList<Agent>> agentsProperty() {
		return _table.itemsProperty();
	}
	
	public AgentView() {
		super();
		init();
	}
	
	@SuppressWarnings("unchecked")
	private void init() {	
		_table = TableViewBuilder.<Agent>create()
				.columnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY)
				.columns(
						this.<Integer>createColumn("ID", Param.Type.AGENT, "id"),
						this.<String>createColumn("Type", Param.Type.AGENT_TYPE, "Type"),
						this.<String>createColumn("Model", Param.Type.AGENT_MODEL, "Model"),
						this.<String>createColumn("Prototype", Param.Type.AGENT_PROTOTYPE, "Prototype"),
						TableColumnBuilder.<Agent, Integer>create().text("Enter").cellValueFactory(new PropertyValueFactory<Agent, Integer>("enter")).build(),
						TableColumnBuilder.<Agent, Integer>create().text("Leave").cellValueFactory(new PropertyValueFactory<Agent, Integer>("leave")).build()
				)
				.build();

		setContent(_table);
		VBox.setVgrow(_table, Priority.NEVER);
	}
	
	
	private <T> TableColumn<Agent, T> createColumn(String title, final Param.Type type, final String field) {
		return TableColumnBuilder.<Agent, T>create()
				.text(title)
				.cellValueFactory(new PropertyValueFactory<Agent, T>(field))
				.cellFactory(new Callback<TableColumn<Agent,T>, TableCell<Agent,T>>() {
					@Override
					public TableCell<Agent, T> call(TableColumn<Agent, T> item) {
						return new AgentCell<T>(type, field);
					}
			
				})
				.build();
	}
	
	class AgentCell<T> extends TableCell<Agent, T> {
		
		private Label _label; 
		private String _field;
		private boolean first = true;
		
		AgentCell(Param.Type type, String field) {
			_field = field;
			_label = new Label("");
			_label.setAlignment(Pos.CENTER);
			setGraphic(_label);
			
			
			_label.setOnDragDetected(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					Dragboard db = _label.startDragAndDrop(TransferMode.COPY);
					
					Param.Type ptype = Type.AGENT_TYPE;
//						Object value = getItem();
					
					Agent agent = getTableView().getItems().get(getIndex());
					
					switch (agent.getType()) {
					case "Facility":
						if ("id".equals(_field)) ptype = Type.FACILITY;
						else if ("Type".equals(_field)) ptype = Type.FACILITY_TYPE;
						break;		
					case "Institute":
						if ("id".equals(_field)) ptype = Type.INSTITUTE;
						else if ("Type".equals(_field)) ptype = Type.INSTITUTE_TYPE;
						break;
					case "Region":
						if ("id".equals(_field)) ptype = Type.REGION;
						else if ("Type".equals(_field)) ptype = Type.REGION_TYPE;
						break;
					case "Market":
						if ("id".equals(_field)) ptype = Type.MARKET;
						else if ("Type".equals(_field)) ptype = Type.MARKET_TYPE;
						break;
					}
					
					ClipboardContent content = new ClipboardContent();
					content.put( DnD.TYPE_FORMAT, ptype);
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

}
	
	
	
	
