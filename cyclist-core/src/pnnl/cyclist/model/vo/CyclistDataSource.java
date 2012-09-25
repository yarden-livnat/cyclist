package pnnl.cyclist.model.vo;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class CyclistDataSource implements DataSource {
	private Properties _properties = new Properties();
	private transient PrintWriter _logger;
	private String _url;
	private boolean _ready = false;
	
	public CyclistDataSource() {
	}
	
	public String getName() {
		return _properties.getProperty("name");
	}
	
	public void setName(String name) {
		_properties.setProperty("name", name);
	}
	
	public void setProperties(Properties p) {
		_properties = p;
	}
	
	public Properties getProperties() {
		return _properties;
	}
	
	public void setURL(String url) {
		if (_url != url) {
			_url = url;
			_ready = false;
		}
	}
	
	public String getURL() {
		return _url;
	}
	
	
	public boolean isReady() {
		return _ready;
	}
	
	public void setReady(boolean value) {
		_ready = value;
	}
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return _logger;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		_logger = out;
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return (T) this;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return iface.isInstance(this);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return getConnection(null, null);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		if (username != null)
            _properties.put("user", username);
        if (password != null)
            _properties.put("pass", password);
        return DriverManager.getConnection(_url, _properties);
	}

}
