package pnnl.cyclist.view.component;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import pnnl.cyclist.Resources;
import pnnl.cyclist.model.vo.CyclistDataSource;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceBoxBuilder;
import javafx.scene.control.ProgressIndicatorBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.TextBuilder;
import javafx.stage.Popup;
import javafx.stage.Window;

public class DatabaseWizard extends BorderPane {

	private Map<String, DatabaseWizardPage> _panes;
	private DatabaseWizardPage _currentPage;
	private TextField _name;
	private ImageView _status;
	private ObjectProperty<CyclistDataSource> _dsProperty = new SimpleObjectProperty<CyclistDataSource>();
	
	public static ObjectProperty<CyclistDataSource> showWizard(Window window) {
		return showWizard(window, new CyclistDataSource());
	}
	
	public static ObjectProperty<CyclistDataSource> showWizard(Window window, CyclistDataSource ds) {
		Popup popup = new Popup();
		DatabaseWizard wizard = new DatabaseWizard(popup, ds);
		popup.getContent().add(wizard);
		popup.show(window);
		return wizard.dataSourceProperty();
	}
	
	public ObjectProperty<CyclistDataSource> dataSourceProperty() {
		return _dsProperty;
	}
	
	private DatabaseWizard(final Popup stage, final CyclistDataSource ds) {
		getStyleClass().add("database-pane");
		ChoiceBox<String> cb;
		final Pane pane = new Pane();
		pane.prefHeight(200);
		
		_panes = createPanes(ds);
		
		String dsName = ds.getName();
		if (dsName == null) dsName = "";
		
		VBox header = VBoxBuilder.create()
				.spacing(5)
				.children(
					HBoxBuilder.create()
						.spacing(5)
						.children(
							TextBuilder.create().text("Name").build(),
							_name = TextFieldBuilder.create().prefWidth(150).text(dsName).build()
						)
						.build(),
					cb = ChoiceBoxBuilder.<String>create()
						.build()
				)
				.build();
		
	
		cb.getSelectionModel().selectedItemProperty()
			.addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					DatabaseWizardPage page = _panes.get(newValue);
					if (page != null) {
						pane.getChildren().clear();
						pane.getChildren().add(page.getNode());
						_currentPage = page;
					}
				}
			});
		
		cb.setItems(FXCollections.observableArrayList(_panes.keySet()));
		String type = ds.getProperties().getProperty("type");
		if (type == null) type = "MySQL";
		
		cb.getSelectionModel().select(type);
		
		Button ok;
		
		HBox buttons = HBoxBuilder.create()
				.spacing(10)
				.padding(new Insets(5))
				.children(
					// Test Connection
					ButtonBuilder.create()
						.text("Test Connection")
						.onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								CyclistDataSource ds = _currentPage.getDataSource();
								testConnection(ds);
							};
						})
						.build(),
					_status = ImageViewBuilder.create().build(),
					ProgressIndicatorBuilder.create().progress(-1).maxWidth(8).maxHeight(8).visible(false).build(),	
					new Spring(),
						
					// Cancel
					ButtonBuilder.create()
						.text("Cancel")
						.onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								stage.hide();
							};
						})
						.build(),
					
					// OK
					ok = ButtonBuilder.create()
						.text("Ok")
						.onAction(new EventHandler<ActionEvent>() {	
							@Override
							public void handle(ActionEvent arg0) {
								CyclistDataSource ds = _currentPage.getDataSource();
								ds.getProperties().setProperty("name", _name.getText());
								_dsProperty.setValue(ds);
								stage.hide();
							};
						})
						.build()	
				)
				.build();
		
		ok.disableProperty().bind(_name.textProperty().isNull().or(_name.textProperty().isEqualTo("")));
		setTop(header);
		setCenter(pane);
		setBottom(buttons);
	}
	
	private Map<String, DatabaseWizardPage> createPanes(CyclistDataSource ds) {
		Map<String, DatabaseWizardPage> panes = new HashMap<>();

		panes.put("MySQL", new MySQLPage(ds));
		panes.put("SQLite", new SQLitePage(ds));
		return panes;
	}
	
	private void testConnection(CyclistDataSource ds) {
//		_indicator.setVisible(true);
		try (Connection conn = ds.getConnection()) {
			System.out.println("connection ok");
			_status.setImage(Resources.getIcon("ok"));
		} catch (Exception e) {
			System.out.println("connection failed");
			_status.setImage(Resources.getIcon("error"));
		}
//		_indicator.setVisible(false);

	}
}
