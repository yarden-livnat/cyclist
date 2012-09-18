package cyclist.model.proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

import com.csvreader.CsvReader;

import cyclist.model.vo.ClimateRowData;
import cyclist.model.vo.Table;
import cyclist.model.vo.Table.TableRow;
import cyclist.model.vo.Weather;

public class WeatherProxy extends Proxy {

	private String _directory;
	private ReadOnlyObjectProperty<ObservableList<Weather>> _data;
	private Map<String, ReadOnlyObjectProperty<ObservableList<ClimateRowData>>> _climateData = new HashMap<>();
	private Map<String, ReadOnlyObjectProperty<Table>> _climateTableData = new HashMap<>();
	
	public WeatherProxy(String name) {
		super(name);
	}
	
	public void setDataSource(String path) {
		if (path == _directory) return;
		if (path != null && path.equals(_directory)) return;
		
		_directory = path;
		_data = null;
	}
	
	public ReadOnlyObjectProperty<ObservableList<Weather>> getWeather() {
		if (_data == null) {
			_data = readData(_directory+"/Grid_MetaMapping.csv");
		}
		return _data;
	}
	
	public ReadOnlyObjectProperty<ObservableList<ClimateRowData>> getClimateData(String name) {
		ReadOnlyObjectProperty<ObservableList<ClimateRowData>> data = _climateData.get(name);
		if (data == null) {
			data = readClimateData(name, _directory+"/weather_epw/"+name+".csv");
			if (data != null) {
				_climateData.put(name, data);
			}
		}
		return data;
	}
	
	public ReadOnlyObjectProperty<Table> getClimateTable(String name) {
		ReadOnlyObjectProperty<Table> table = _climateTableData.get(name);
		if (table == null) {
			table = readTable(name, _directory+"/weather_epw/"+name+".csv");
			if (table != null) {
				_climateTableData.put(name, table);
			}
		}
		return table;
	}
	
	private ReadOnlyObjectProperty<ObservableList<Weather>> readData(final String filename) {
		Task<ObservableList<Weather>> task = new Task<ObservableList<Weather>>() {
		    @Override protected ObservableList<Weather> call() throws Exception {
		    	List<Weather> list = new ArrayList<Weather>();
				try {
					CsvReader reader = new CsvReader(filename);
					reader.readHeaders();
					while (reader.readRecord()) {
						Weather w = new Weather();
						w.name = reader.get("Station");
						w.stationId = reader.get("Assignment");
						w.newID = Integer.parseInt(reader.get("newID"));
						w.stationX = Float.parseFloat(reader.get("Station.x"));
						w.stationY = Float.parseFloat(reader.get("Station.y"));
						w.x = Float.parseFloat(reader.get("x"));
						w.y = Float.parseFloat(reader.get("y"));
						w.stateName = reader.get("stateNAMES");
						w.stateFIPS = Integer.parseInt(reader.get("stateFIPS"));
						w.countyName = reader.get("CntyName");
						w.countyFIPS = Integer.parseInt(reader.get("countyFIPS"));
					
//						w.from = 2005;
//						w.to = 2100;
//						w.measurements = new int[w.to-w.from+1];
//						int index = reader.getIndex("p"+w.from);
//						int last = reader.getHeaderCount();
//						for (int i=0; index<last; i++, index++) {
//							w.measurements[i] = Integer.parseInt(reader.get(index));
//						}				
						
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
	
	private ReadOnlyObjectProperty<ObservableList<ClimateRowData>> readClimateData(final String name, final String path) {
		Task<ObservableList<ClimateRowData>> task = new Task<ObservableList<ClimateRowData>>() {
		    @Override protected ObservableList<ClimateRowData> call() throws Exception {
		    	System.out.println("readClimateData from "+path+" ["+name+"]");
		    	List<ClimateRowData> list = new ArrayList<>();
				try {
					CsvReader reader = new CsvReader(path);
					reader.readHeaders();
					int n=1;
					while (reader.readRecord()) {
						ClimateRowData data = new ClimateRowData();
						data.year = Integer.parseInt(reader.get("Year"));
						data.month = Integer.parseInt(reader.get("Month"));
						data.day = Integer.parseInt(reader.get("Day"));
						data.hour = Integer.parseInt(reader.get("Hour"));

						data.value = new double[18];
						for (int i=0; i<18; i++) {
							data.value[i] = Double.parseDouble(reader.get(i+5));
//							System.out.print(data.value[i]);
						}
						list.add(data);
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
	
	
	private ReadOnlyObjectProperty<Table> readTable(final String name, final String path) {
		Task<Table> task = new Task<Table>() {
		    @Override protected Table call() throws Exception {
		    	Table table = new Table(name);
				try {
					CsvReader reader = new CsvReader(path);
					reader.readHeaders();
					int cols = reader.getHeaderCount();
					table.setHeaders(reader.getHeaders());
					
					Table.COL_TYPE types[] = new Table.COL_TYPE[cols];
					for (int i=0; i<5; i++) {
						types[i] = Table.COL_TYPE.INT;
					}
					for (int i=5 ; i<cols; i++) {
						types[i] = Table.COL_TYPE.REAL;
					}
					table.setTypes(types);
					
					int n=1;
					while (reader.readRecord()) {
						TableRow row = table.new TableRow(cols);
						
						for (int i=0; i<cols; i++) {
							switch (table.getType(i)) {
							case STRING:
								row.value[i] = reader.get(i);
								break;
							case INT:
								row.value[i] = Integer.parseInt(reader.get(i));
								break;
							case REAL:
								row.value[i] = Double.parseDouble(reader.get(i));
								break;
							}	
						}
						
						table.addRow(row);
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				System.out.println(table.getRows().size()+" rows");
				return table;
		    }
		};
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		
		return task.valueProperty(); 
	}
}
