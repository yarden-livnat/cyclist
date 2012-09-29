package weather.view.tool.view;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.util.StringConverter;
import pnnl.cyclist.model.vo.Data;
import pnnl.cyclist.model.vo.DataCollection;
import pnnl.cyclist.model.vo.DataObject;
import pnnl.cyclist.model.vo.World;
import pnnl.cyclist.view.component.View;

public class ChartView extends View {

	public static final String DEFUALT_DATA = "default";
	
	/*
	 * input
	 */
	private ObjectProperty<World> _worldProperty = new SimpleObjectProperty<>();
	private ObjectProperty<Data> _dataProperty = new SimpleObjectProperty<>();
	
	/*
	 * local data
	 */
	
	Data _transformedData;
	Data _filteredData;
	DataCollection _splitData;
	
	/*
	 * Mapping functions
	 */
	
	private MapFunction _mapX = new DayOfWeekFunction();
	private MapFunction _mapY = new WeatherVariableFunction();
	private MeasureFunction _createSeries = new AvgFunction();
	
	/*
	 * UI
	 */
	
	private XYChart<Number, Number> _chart;
	
	public ChartView() {
		super();
		init();
	}
	
	public ObjectProperty<World> worldProperty() {
		return _worldProperty;
	}
	
	public void setWorld(World map) {
		worldProperty().set(map);
	}
	
	public World getWorld() {
		return worldProperty().get();
	}
	
	
	public ObjectProperty<Data> dataProperty() {
		return _dataProperty;
	}
	
	public void setData(Data data) {
		dataProperty().set(data);
	}
	
	public Data getData() {
		return dataProperty().get();
	}
	
	
	/*
	 * Private functions
	 */
	
	private void setXFunction(MapFunction func) {
		NumberAxis xAxis = (NumberAxis) _chart.getXAxis();
		xAxis.setLabel(func.getTitle());
		_mapX = func;
		
		xAxis.setTickLabelFormatter(func.getAxisFormater(xAxis));
	}
	
	private void setYFunction(MapFunction func) {
		NumberAxis yAxis = (NumberAxis) _chart.getYAxis();
		yAxis.setLabel(func.getTitle());
		_mapY = func;
		
		yAxis.setTickLabelFormatter(func.getAxisFormater(yAxis));
	}
	
	private void processData() {
		if (getData() == null) {
			return;
		}
		
		transformData();
		filterData();
		splitData();
		mapData();
	}
	
	private void transformData() {
		// no op for now
		_transformedData = getData();
		
	}
	
	private void filterData() {
		// no op for now
		_filteredData = _transformedData;
	}
	
	private void splitData() {
		// no op for now
		DataCollection collection = new DataCollection();
		collection.getCollection().put(DEFUALT_DATA, _filteredData);
		_splitData = collection;
	}
	
	private void mapData() {
		_chart.getData().clear();
		
		String names[] = _splitData.getCollection().keySet().toArray(new String[0]);
		Arrays.sort(names);
		
		for (String name : names) {
			Data data = _splitData.get(name);
			List<DataObject> items = data.getItems();
			
			XYChart.Series<Number, Number> series = _createSeries.map(items);
			series.setName(name);
		
			_chart.getData().add(series);
		}
	}
	
	private void init() {
		prefWidth(400);
		prefHeight(400);
		
		// create ui
		VBox vbox = VBoxBuilder.create()
						.children(
							createLineChart()
							)
						.build();
		setContent(vbox);
		
		// setup listeners
		_dataProperty.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				processData();
			}
		});
	}
	
	private XYChart<Number, Number> createLineChart() {
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		
		_chart = new LineChart<Number, Number>(xAxis, yAxis);
		_chart.setTitle("Weather Chart");
		_chart.setLegendVisible(false);
		
		setXFunction(new DayOfWeekFunction());
		setYFunction(new WeatherVariableFunction());
		
		xAxis.setAutoRanging(false);
		xAxis.setLowerBound(-0.5);
		xAxis.setUpperBound(6.5);
		xAxis.setTickUnit(1);
		xAxis.setMinorTickCount(0);
		
		return _chart;
	}
	
	/*
	 * Functions
	 */
	interface MapFunction {
		String getTitle();
		StringConverter<Number> getAxisFormater(NumberAxis axis);
		Number map(DataObject obj);
	}
	
	interface MeasureFunction {
		XYChart.Series<Number, Number> map(List<DataObject> items);
	}
	
	class DayOfWeekFunction implements MapFunction {
		final Calendar calendar = Calendar.getInstance();
		
		@Override
		public String getTitle() {
			return "Day of Week";
		}
		
		@Override
		public StringConverter<Number> getAxisFormater(NumberAxis axis) {
			return new NumberAxis.DefaultFormatter(axis) {
				@Override
				public String toString(Number object) {
					String day;
					switch (object.intValue()) {
						case 0: day = "Sunday"; break;
						case 1: day = "Monday"; break;
						case 2: day = "Tuesday"; break;
						case 3: day = "Wednesday"; break;
						case 4: day = "Thursday"; break;
						case 5: day = "Friday"; break;
						case 6: day = "Saturday"; break;
						default: day = ""; break;
					}
				    return day;
				}
			};
		}
		
		@Override
		public Number map(DataObject obj) {
			calendar.setTime((Date)obj.attribute[0]);
			return calendar.get(Calendar.DAY_OF_WEEK)-1;
		}
	}
	
	class WeatherVariableFunction implements MapFunction {
		@Override
		public String getTitle() {
			return "Dry Bulb";
		}
		
		@Override
		public StringConverter<Number> getAxisFormater(NumberAxis axis) {
			return new NumberAxis.DefaultFormatter(axis) {
				@Override
				public String toString(Number object) {
					return String.format("%4.1f", object);
				}
			};
		}
		
		@Override
		public Number map(DataObject obj) {
			return (Number)obj.attribute[1];
		}
	}
	
	class NullFunction implements MeasureFunction {
		@Override
		public Series<Number, Number> map(List<DataObject> items) {
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			
			for (DataObject obj : items) {
				Number x = _mapX.map(obj);
				Number y = _mapY.map(obj);
				series.getData().add(new XYChart.Data<Number, Number>(x, y));
			}
			
			return series;
		}
	}
		
	class AvgFunction implements MeasureFunction {

		@Override
		public Series<Number, Number> map(List<DataObject> items) {
			Map<Number, Count> values = new HashMap<>();
			
			for (DataObject obj : items) {
				Number x = _mapX.map(obj);
				Number y = _mapY.map(obj);
				
				Count value = values.get(x);
				if (value == null) {
					value = new Count();
					values.put(x, value);
				}
				value.count++;
				value.num = value.num.doubleValue() + y.doubleValue();
			}
			
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			for (Number x : values.keySet()) {
				Count v = values.get(x);

				series.getData().add(new XYChart.Data<Number, Number>(x, v.num.doubleValue()/v.count));
			}
			
			return series;
		}
		
		class Count {
			public Number num = 0;
			public Integer count = 0;
		}
	}
}
