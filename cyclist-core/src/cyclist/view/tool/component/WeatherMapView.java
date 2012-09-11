package cyclist.view.tool.component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import cyclist.model.vo.Weather;
import cyclist.view.component.View;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class WeatherMapView extends View {

	private Canvas _canvas;
	private ObjectProperty<ObservableList<Weather>> _dataProperty = new SimpleObjectProperty<>();
	private float _minX, _maxX;
	private float _minY, _maxY;
	private float _scale;
	private Set<String> _stations; 
	
	public WeatherMapView() {
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
		_canvas = new Canvas(500, 500);
		setContent(_canvas);
		
		_dataProperty.addListener(new ChangeListener<List<Weather>>() {

			@Override
			public void changed(
					ObservableValue<? extends List<Weather>> observable,
					List<Weather> oldValue, List<Weather> newValue) 
			{
				System.out.println("new data");
				if (newValue == null || newValue.size() == 0) {
					// clear canvas
				}
				else {
					analyzeData();
					drawData();
				}
			}
			
		});
	}
	
	private void analyzeData() {
		ObservableList<Weather> list = _dataProperty.get();
		_minX = _maxX = list.get(0).x;
		_minY = _maxY = list.get(0).y;
		_stations = new HashSet<>();
		
		// compute bbox and stations
		for (Weather w : list) {
			if (w.x < _minX) _minX = w.x;
			else if (w.x > _maxX) _maxX = w.x;
			
			if (w.y < _minY) _minY = w.y;
			else if (w.y > _maxY) _maxY = w.y;
		}
		
		System.out.println("min/max: ("+_minX+", "+_minY+")  ("+_maxX+", "+_maxY+")");
		
		_scale = Math.min(500/(_maxX-_minX), 500/(_maxY-_minY));
		
		
//		_canvas.getTransforms().add(new Scale(d, d));
//		_canvas.getTransforms().add(new Translate(_minX, _minY));
	}
	
	private void drawData() {
		GraphicsContext gc = _canvas.getGraphicsContext2D();
		
		gc.setFill(Color.GRAY);
		gc.setStroke(Color.BLACK);
		
		// draw stations
		ObservableList<Weather> list = _dataProperty.get();
		for (Weather w : list) {
			if (!_stations.contains(w.name)) {
				_stations.add(w.name);
				gc.fillOval((w.stationX-_minX)*_scale, 500-(w.stationY-_minY)*_scale, 5, 5);
			}
		}
	}
	
}
