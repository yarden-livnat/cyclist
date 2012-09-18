package weather.view.tool.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import weather.view.gis.component.StationFeature;
import weather.view.gis.component.WeatherStation;
import weather.view.gis.component.WeatherStationFeature;
//import weather.view.tool.component.WeatherMapView.Station;
import cyclist.model.vo.Weather;
import cyclist.view.component.View;

public class GISMapView extends View {

	private GISMap _map;
	private FeaturesLayer _stationsLayer;
	
	private ObjectProperty<ObservableList<Weather>> _dataProperty = new SimpleObjectProperty<>();
	private Map<String, WeatherStation> _stations = new HashMap<String, WeatherStation>();
	
	
	public GISMapView() {
		super();
		init();
	}
	
	public ObjectProperty<ObservableList<Weather>> dataProperty() {
		return _dataProperty;
	}
	
	public void setData(ObservableList<Weather> list) {
		dataProperty().set(list);
	}
	
	public ObservableList<Weather> getData() {
		return dataProperty().get();
	}

	private void init() {
		minWidth(300);
		minHeight(200);
		prefWidth(300);
		prefHeight(200);
		
		_map = new GISMap();

		_stationsLayer = new FeaturesLayer();		
		_map.addLayer(_stationsLayer);
		
		setContent(_map);
		
		_dataProperty.addListener(new ChangeListener<List<Weather>>() {

			@Override
			public void changed(
					ObservableValue<? extends List<Weather>> observable,
					List<Weather> oldValue, List<Weather> newValue) 
			{
				if (newValue == null || newValue.size() == 0) {
					// clear canvas
				}
				else {
					analyzeData();
					_map.redraw();
				}
			}
		});
	}
	
	private void analyzeData() {
		ObservableList<Weather> list = _dataProperty.get();
		if (list == null || list.size() == 0) return;
		
		_stations = new HashMap<>();
		for (Weather w : list) {
			if (!_stations.containsKey(w.name)) {
				WeatherStation s = new WeatherStation();
				s.name = w.name;
				s.x = w.stationX;
				s.y = w.stationY;
				s.color = Color.color(Math.random(), Math.random(), Math.random());
				_stations.put(w.name, s);
				
				WeatherStationFeature feature = new WeatherStationFeature(s);
				feature.color = Color.GRAY;
				_stationsLayer.getFeatures().add(feature);
			}
		}
		
		
	}
}
