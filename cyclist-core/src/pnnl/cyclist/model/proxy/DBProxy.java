package pnnl.cyclist.model.proxy;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.binding.When;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

import pnnl.cyclist.controller.ApplicationConstants;
import pnnl.cyclist.model.proxy.CyclistDataStream.State;
import pnnl.cyclist.model.vo.CyclistDataSource;


public class DBProxy extends Proxy implements CyclistDataStream {
	
	protected CyclistDataSource _ds;
	protected ObjectProperty<State> _stateProperty = new SimpleObjectProperty<State>(State.NOT_READY);
	
	public DBProxy(String name) {
		super(name);
	}
	
	public  ObjectProperty<State> stateProperty() {
		return _stateProperty;
	}
	
	public State getState() {
		return _stateProperty.get();
	}
	
	public void setState(State value) {
		_stateProperty.set(value);
	}
	
	public String getDataSourceName() {
		return _ds == null ? "" : _ds.getProperties().getProperty("name");
	}
	
	public boolean isReady() {
		return getState() == State.OK;
	}
	
	public CyclistDataStream getDataStream() {
		return this;
	}

	@Override
	public void setDataSource(CyclistDataSource ds) {
		_ds = ds;
		initds();
		
	}
	
	@Override
	public void onRegister() {	
	}


	protected void initds() {
		final Proxy self = this;
		
		stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (newValue == null) newValue = State.NOT_READY;
				switch (newValue) {
				case OK:
					self.sendNotification(ApplicationConstants.DATA_SOURCE_OK, self);
					break;
				case WARNING:
//					self.sendNotification(ApplicationConstants.DATA_SOURCE_WARNING, name);
					break;
				case ERROR:
//					self.sendNotification(ApplicationConstants.DATA_SOURCE_ERROR, name);
					break;
				case CONNECTING:
//					self.sendNotification(ApplicationConstants.DATA_SOURCE_CONNECTING, name);
					break;
				case NOT_READY:
					// ignore
					break;
				}
			}
		});
		
		Task<State> task = new Task<State>() {
			@Override
			protected pnnl.cyclist.model.proxy.CyclistDataStream.State call() throws Exception {
				try (Connection conn = _ds.getConnection();
						Statement stmt = conn.createStatement())
				{
					return pnnl.cyclist.model.proxy.CyclistDataStream.State.OK;
						
				} catch (SQLException e)  {				
					// report error
					e.printStackTrace();
					return pnnl.cyclist.model.proxy.CyclistDataStream.State.ERROR;
				} 
			}
		};
		
		stateProperty().bind(new When(task.valueProperty().isNull())
			.then(new SimpleObjectProperty<State>(State.CONNECTING))
			.otherwise(task.valueProperty()));
	
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
}
