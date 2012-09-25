package pnnl.cyclist.view.component;

import java.util.Properties;

import pnnl.cyclist.model.vo.CyclistDataSource;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.PasswordFieldBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.text.TextBuilder;

public class DatabasePage extends GridPane implements DatabaseWizardPage {

	protected TextField _user;
	protected TextField _password;
	protected TextField _host;
	protected TextField _port;
	protected TextField _schema;
	
	protected String _driver = "";
	protected String _type = "";
	
	protected CyclistDataSource _ds;
	
	public DatabasePage(CyclistDataSource ds) {
		_ds = ds;
		build();
		init();
	}
	
	public String getURL() {
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:").append(_driver).append("://")
			.append(_host.getText()).append(":").append(_port.getText())
			.append("/").append(_schema.getText());
		
		return sb.toString();
	}
	
	public CyclistDataSource getDataSource() {
		_ds.setURL(getURL());
		Properties p = _ds.getProperties();
		p.setProperty("user", _user.getText());
		p.setProperty("password", _password.getText());
		p.setProperty("host", _host.getText());
		p.setProperty("port", _port.getText());
		p.setProperty("schema", _schema.getText());
		p.setProperty("driver", _driver);
		p.setProperty("type", _type);
		_ds.setProperties(p);
		
		return _ds;
	}
	
	public Node getNode() {
		return this;
	}
	
	protected void build() {
		 GridPaneBuilder.create()
				.vgap(10)
				.hgap(5)
				.padding(new Insets(10,3,10,3))
				.applyTo(this);
		
		add(TextBuilder.create().text("User:").build(), 0, 0);
		add(_user = TextFieldBuilder.create().build(), 1, 0);

		add(TextBuilder.create().text("Password:").build(), 0, 1);
		add(_password = PasswordFieldBuilder.create().build(), 1, 1);
		
		add(TextBuilder.create().text("Host:").build(), 0, 2);
		add(_host = TextFieldBuilder.create().build(), 1, 2);

		add(TextBuilder.create().text("Port:").build(), 2, 2);
		add(_port = TextFieldBuilder.create().build(), 3, 2);
		
		add(TextBuilder.create().text("Schema:").build(), 0, 3);
		add(_schema = TextFieldBuilder.create().build(), 1, 3);
	}
	
	protected void init() {
		Properties p = _ds.getProperties();
		_user.setText(check(p.getProperty("user")));
		_password.setText(check(p.getProperty("password")));
		_host.setText(check(p.getProperty("host")));
		_port.setText(check(p.getProperty("port")));
		_schema.setText(check(p.getProperty("schema")));
	}
	
	protected String check(String value) {
		return value == null ? "" : value;
	}
}
