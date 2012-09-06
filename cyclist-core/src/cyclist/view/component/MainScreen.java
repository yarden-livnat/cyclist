/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cyclist.view.component;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;
import cyclist.Resources;
import cyclist.model.proxy.ToolsService;
import cyclist.model.vo.CyclistDataSource;
import cyclist.view.event.CyclistDataSourceEvent;
import cyclist.view.event.CyclistInputEvent;

/**
 *
 * @author yarden
 */
public class MainScreen extends VBox {

	public static final String ID = "main-screen";
	
	private Label _label;
	private Node _toolsPane;
	private DataPane _dataPane;
	private Workspace _workspace;
	
	// -- Properties --
	private ObjectProperty<EventHandler<CyclistInputEvent>> _propertyOnOpen = new SimpleObjectProperty<EventHandler<CyclistInputEvent>>();
	private ObjectProperty<EventHandler<CyclistDataSourceEvent>> _onDataSourceActionProperty = new SimpleObjectProperty<EventHandler<CyclistDataSourceEvent>>();
	
	public final ObjectProperty<EventHandler<CyclistInputEvent>> onOpenPropery() {
		return _propertyOnOpen;
	}
	
	public final void setOnOpen(EventHandler<CyclistInputEvent> handler) {
		_propertyOnOpen.set(handler);
	}
	
	public final EventHandler<CyclistInputEvent> getOnOpen() {
		return _propertyOnOpen.get();
	}
	
	/*
	 * Data Source action
	 */
	
	public final ObjectProperty<EventHandler<CyclistDataSourceEvent>> onDataSourceActionProperty() {
		return _onDataSourceActionProperty;
	}
	
	public final EventHandler<CyclistDataSourceEvent> getOnDataSourceAction() {
		return onDataSourceActionProperty().get();
	}
	
	public void setOnDataSourceAction(EventHandler<CyclistDataSourceEvent> handler) {
		onDataSourceActionProperty().set(handler);
	}
	
	/*
	 * Data Source selection
	 */
	public final ObjectProperty<EventHandler<CyclistDataSourceEvent>> onDataSourceSelectionProperty() {
		return _dataPane.onActionProperty();
	}
	
	public EventHandler<CyclistDataSourceEvent> getOnDataSourceSelection() {
		return _dataPane.getOnAction();
	}
	
	public void setOnDataSourceSelection(EventHandler<CyclistDataSourceEvent> handler) {
		_dataPane.setOnAction(handler);
	}	 
	
	/**
	 * Constructor
	 */
	public MainScreen(Stage stage) {
		super();
		setId(ID);
		
		init(stage);
	}
	
	
	public Workspace getWorkspace() {
		return _workspace;
	}
	
	public DataPane getDataPane() {
		return _dataPane;
	}
	
	public void setLabel(String text) {
		_label.setText(text);
		ImageView icon = new ImageView(Resources.getIcon("database.png"));
		_label.setGraphic(icon);
	}
	
	private void init(Stage stage) {
		_workspace = new Workspace();
		_workspace.minHeight(400);
		_workspace.minWidth(300);
		
		ScrollPane sp = new ScrollPane();
		sp.setPrefSize(300, 200);
		sp.setFitToWidth(true);
		sp.setContent(_workspace);
		
		Region spring = new Region();
		spring.setPrefWidth(20);
		spring.setMinWidth(Region.USE_PREF_SIZE);
		VBox.setVgrow(spring, Priority.ALWAYS);
		
		HBox hbox = HBoxBuilder.create()
						.children(
								VBoxBuilder.create()
									.children(
										_dataPane = new DataPane(),
									    _toolsPane = createToolsBox(),
									   new ParamPane(),
									   spring
									 )
									 .spacing(8)
									 .padding(new Insets(5, 5, 5, 5))
									 .build(),
								 _workspace
						)
						.build();
		
		VBox.setVgrow(_toolsPane, Priority.SOMETIMES);
		HBox.setHgrow(_workspace, Priority.ALWAYS);
		VBox.setVgrow(hbox, Priority.ALWAYS);
		
		getChildren().addAll(createMenuBar(stage), hbox);

	}
	
	private Node createToolsBox() {
		ToolsPane pane = new ToolsPane();
//		pane.setItems(Resources.getToolsInfo());
		pane.setTools(ToolsService.getInstance().getTools());
		return pane;
	}
	
	private MenuBar createMenuBar(final Stage stage) {

		MenuBar menubar = new MenuBar();
		// -- File menu
//		Menu fileMenu = new Menu("File");
//		MenuItem openItem = new MenuItem("Open", new ImageView(Resources.getIcon("open.png")));
//		openItem.setOnAction(new EventHandler<ActionEvent>() {
//			public void handle(ActionEvent e) {
//				FileChooser chooser = new FileChooser();
//				chooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("SQLite files (*.sqlite)", "*.sqlite") );
//				File file = chooser.showOpenDialog(null);
//				if (file != null && onOpenPropery().get() != null) {
//					onOpenPropery().get().handle(new CyclusInputEvent(CyclusInputEvent.SELECT, file.getAbsolutePath()));
//				}
//			}
//		});
//		fileMenu.getItems().addAll(openItem);
//		menubar.getMenus().add(fileMenu);
		
		// -- Database menu
		Menu databaseMenu = new Menu("Database");
		MenuItem dbAdd = new MenuItem("Add", new ImageView(Resources.getIcon("open.png")));
		dbAdd.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				ObjectProperty<CyclistDataSource> ds = DatabaseWizard.showWizard(stage); 
				ds.addListener(new ChangeListener<CyclistDataSource>() {
					@Override
					public void changed(
							ObservableValue<? extends CyclistDataSource> observable,
							CyclistDataSource oldValue, CyclistDataSource newValue) 
					{
						if (newValue != null && getOnDataSourceAction() != null) {
							getOnDataSourceAction().handle(new CyclistDataSourceEvent(CyclistDataSourceEvent.CREATE, newValue));
						}
							
						
					}
				});				
			}
		});
		databaseMenu.getItems().add(dbAdd);
		
		// -- Help menu
//		Menu helpMenu = new Menu("Help");
		
		menubar.getMenus().add(databaseMenu);
		return menubar;
	}
}
