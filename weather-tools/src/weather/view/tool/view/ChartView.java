package weather.view.tool.view;

import java.util.Arrays;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import pnnl.cyclist.model.vo.Data;
import pnnl.cyclist.model.vo.DataCollection;
import pnnl.cyclist.model.vo.DataObject;
import pnnl.cyclist.model.vo.World;
import pnnl.cyclist.view.component.View;

public class ChartView extends View {

	public static final String DEFUALT_DATA 	= "defaultData";
	
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
	
	private void processData() {
		transformData();
		filterData();
		splitData();
		mapData();
	}
	
	private void transformData() {
		_transformedData = getData();
		
	}
	
	private void filterData() {
		_filteredData = _transformedData;
	}
	
	private void splitData() {
		DataCollection collection = new DataCollection();
		collection.getCollection().put(DEFUALT_DATA, _filteredData);
		_splitData = collection;
	}
	
	private void mapData() {
		String[] names = _splitData.getCollection().keySet().toArray(new String[0]);
		Arrays.sort(names);
		
		for (String name : names) {
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			series.setName(name);
		
			for (DataObject obj : _splitData.get(name).getItems()) {
				series.getData().add(new XYChart.Data<Number, Number>())
			}
		
		}
	
	}
	
	private void init() {
		prefWidth(400);
		prefHeight(400);
		
		// create ui
		VBox vbox = VBoxBuilder.create()
						.children(
							_chart = createLineChart()
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
		
		LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis);
		chart.setTitle("Chart");
		chart.setLegendVisible(false);
		
		return chart;
	}
	
	interface MapFunction {
		 Number map(DataObject obj);
	}
	
	class DayOfWeek implements MapFunction {
		public Number map(DataObject obj) {
			return 1;
		}
	}
}
