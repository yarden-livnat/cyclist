package cyclist.model.proxy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

import com.csvreader.CsvReader;

import cyclist.model.vo.Agent;
import cyclist.model.vo.Weather;

public class WeatherProxy extends Proxy {

	private String _filename;
	private ReadOnlyObjectProperty<ObservableList<Weather>> _data;
	
	public WeatherProxy(String name) {
		super(name);
	}
	
	public void setDataSource(String name) {
		if (name == _filename) return;
		if (name != null && name.equals(_filename)) return;
		
		_filename = name;
		_data = null;
	}
	
	public ReadOnlyObjectProperty<ObservableList<Weather>> getWeather() {
		if (_data == null) {
			_data = readData();
		}
		return _data;
	}
	
	private ReadOnlyObjectProperty<ObservableList<Weather>> readData() {
		Task<ObservableList<Weather>> task = new Task<ObservableList<Weather>>() {
		    @Override protected ObservableList<Weather> call() throws Exception {
		    	List<Weather> list = new ArrayList<Weather>();
				try {
					CsvReader reader = new CsvReader(_filename);
					reader.readHeaders();
					while (reader.readRecord()) {
						Weather w = new Weather();
						w.name = reader.get("Station");
						w.newID = Integer.parseInt(reader.get("newID"));
						w.stationX = Float.parseFloat(reader.get("Station.x"));
						w.stationY = Float.parseFloat(reader.get("Station.y"));
						w.x = Float.parseFloat(reader.get("x"));
						w.y = Float.parseFloat(reader.get("y"));
						w.stateName = reader.get("stateNAMES");
						w.stateFIPS = Integer.parseInt(reader.get("stateFIPS"));
						w.countyName = reader.get("CntyName");
						w.countyFIPS = Integer.parseInt(reader.get("countyFIPS"));
					
						w.from = 2005;
						w.to = 2100;
						w.measurements = new int[w.to-w.from+1];
						int index = reader.getIndex("p"+w.from);
						int last = reader.getHeaderCount();
						for (int i=0; index<last; i++, index++) {
							w.measurements[i] = Integer.parseInt(reader.get(index));
						}				
						
						list.add(w);
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return FXCollections.observableList(list);
		    }
		};
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		
		return task.valueProperty(); 
	}
}
