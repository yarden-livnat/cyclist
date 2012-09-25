/**
 * Cyclist
 * Copyright (c) Yarden Livnat 2012.
 * Scientific Computing and Imaging Institute
 * University of Utah
 */
package pnnl.cyclist.view.component;

import java.io.File;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.text.TextBuilder;
import javafx.stage.FileChooser;

import org.apache.log4j.Logger;

import pnnl.cyclist.model.vo.CyclistDataSource;


/**
 * @author yarden
 *
 */
public class SQLitePage extends GridPane implements DatabaseWizardPage {
	static Logger log = Logger.getLogger(SQLitePage.class);
	
	private TextField _path;
	private CyclistDataSource _ds;
	
	public SQLitePage(CyclistDataSource ds) {
		_ds = ds;
		build();
	}
	
	/** 
	 * @see pnnl.cyclist.view.component.DatabaseWizardPage#getURL()
	 */
	@Override
	public String getURL() {
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:sqlite:/").append(_path.getText());
		return sb.toString();
	}

	/** 
	 * @see pnnl.cyclist.view.component.DatabaseWizardPage#getDataSource()
	 */
	@Override
	public CyclistDataSource getDataSource() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			log.warn("Can not load sqlite driver", e);
		}
			
		_ds.setURL(getURL());
		Properties p = _ds.getProperties();
		p.setProperty("driver", "sqlite");
		p.setProperty("type", "SQLite");
		p.setProperty("path", _path.getText());
		String name = _path.getText();
		p.setProperty("name", name.substring(name.lastIndexOf("/")+1));
		
		return _ds;
	}

	/**
	 * @see pnnl.cyclist.view.component.DatabaseWizardPage#getNode()
	 */
	@Override
	public Node getNode() {
		return this;
	}

	
	private void build() {
		GridPaneBuilder.create()
			.vgap(10)
			.hgap(5)
			.padding(new Insets(10,3,10,3))
			.applyTo(this);

		String path = _ds.getProperties().getProperty("path");
		if (path == null) path = "";
		
		 _path = TextFieldBuilder.create().text(path).build();

		add(TextBuilder.create().text("File:").build(), 0, 0);
		add(_path, 1, 0);
		add(ButtonBuilder.create()
				.text("...")
				.onAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						FileChooser chooser = new FileChooser();
						chooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("SQLite files (*.sqlite)", "*.sqlite") );
						File file = chooser.showOpenDialog(null);
						if (file != null)
							_path.setText(file.getPath());
					}
				})
				.build(),
			2, 0);
	}
}
