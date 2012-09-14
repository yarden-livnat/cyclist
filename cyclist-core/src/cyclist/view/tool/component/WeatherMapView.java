package cyclist.view.tool.component;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import cyclist.model.vo.Weather;
import cyclist.view.component.View;

public class WeatherMapView extends View {

	private ObjectProperty<ObservableList<Weather>> _dataProperty = new SimpleObjectProperty<>();
	private Canvas _canvas;
	private GISMap _map;

	private double _minX, _maxX;
	private double _minY, _maxY;
	private double _mouseX = 0;
	private double _mouseY = 0;
	
	List<AffineTransform> _transforms = new ArrayList<>();
	
	private Map<String, Station> _stations = new HashMap<>();
	
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
		_map = new GISMap();
		setContent(_map);
		_canvas = _map.getCanvas();
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
					drawData();
				}
			}
		});
		
		_canvas.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {				
				ObservableList<Weather> list = _dataProperty.get();
				if (list == null || list.size() == 0) return;
				
				_transforms.set(0, initialTransform());
				drawData();
			}
			
		});
		
		_canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				_mouseX = e.getX();
				_mouseY = e.getY();	
				
				_transforms.add(new AffineTransform());
			}
		});
		
		_canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (e.getButton() == MouseButton.SECONDARY || (e.getButton() == MouseButton.PRIMARY && e.isControlDown())) {
					double dy = (e.getY() - _mouseY)/30;
					double s = dy > 0 ? 1+dy : 1/ (1-dy);
					AffineTransform a = AffineTransform.getTranslateInstance(_mouseX, _mouseY);
					a.scale(s, s);
					a.translate(-_mouseX, -_mouseY);
							
					_transforms.set(_transforms.size()-1, a);
				} else if (e.getButton() == MouseButton.PRIMARY) {
					double dx = e.getX() - _mouseX;
					double dy = e.getY() - _mouseY;
					_transforms.set(_transforms.size()-1, AffineTransform.getTranslateInstance(dx, dy));
					
				} 
				drawData();
			}
		});
	}
	
	private void analyzeData() {
		ObservableList<Weather> list = _dataProperty.get();
		if (list == null || list.size() == 0) return;
		
		_minX = _maxX = list.get(0).x;
		_minY = _maxY = list.get(0).y;
		_stations = new HashMap<>();
		
		// compute bbox and stations
		for (Weather w : list) {
			if (w.x < _minX) _minX = w.x;
			else if (w.x > _maxX) _maxX = w.x;
			
			if (w.y < _minY) _minY = w.y;
			else if (w.y > _maxY) _maxY = w.y;
			
			if (!_stations.containsKey(w.name)) {
				Station s = new Station();
				s.name = w.name;
				s.x = w.stationX;
				s.y = w.stationY;
				s.color = Color.color(Math.random(), Math.random(), Math.random());
				_stations.put(w.name, s);
			}
		}
				
		_transforms.clear();
		_transforms.add(initialTransform());
	}
	
	private void drawData() {
		GraphicsContext gc = _canvas.getGraphicsContext2D();
		
		// clear screen
		gc.setTransform(new Affine());

		gc.setFill(Color.LIGHTBLUE);
		gc.clearRect(0, 0, _canvas.getWidth(), _canvas.getHeight());
		
		// get data
		ObservableList<Weather> list = _dataProperty.get();
		if (list == null || list.size() == 0) return;
		
		AffineTransform a = new AffineTransform();
		for (AffineTransform t : _transforms) {
			a.preConcatenate(t);
		}
		Affine af = convert(a);
		gc.setTransform(af);
		
		
		// draw
		gc.setFill(Color.GRAY);
		gc.setStroke(Color.BLACK);
		
		double unit = 1/a.getScaleX();
		gc.setLineWidth(unit);
		
		// draw stations
		double d = 0.13/2;
		
		
		for (Weather w : list) {
			gc.setFill(_stations.get(w.name).color);
			gc.fillRect(w.x-d, w.y-d, 2*d, 2*d);
		}
	
		gc.setFill(Color.BLACK);
		for (Station station : _stations.values()) {
			gc.fillOval(station.x, station.y, 5*unit, 5*unit);
		}
		
	}	
	
	private AffineTransform initialTransform() {
		double dx = _maxX - _minX;
		double dy = _maxY - _minY;
		double aspectRatio = dy/dx;
		double sx = _canvas.getWidth()/dx;
		double sy = _canvas.getHeight()/dy;
		double s = _canvas.getHeight()/_canvas.getWidth() < aspectRatio ? sy : sx;
		
		
		AffineTransform a = AffineTransform.getTranslateInstance(-_minX, -_minY);
		a.preConcatenate(AffineTransform.getScaleInstance(s, -s));
		a.preConcatenate(AffineTransform.getTranslateInstance(0, _canvas.getHeight()));
		
		return a;
	}
	
	private Affine convert(AffineTransform t) {
		Affine a = new Affine();
		
		a.setMxx(t.getScaleX());
		a.setMxy(t.getShearX());
		a.setMxz(0);
		a.setTx(t.getTranslateX());
		
		a.setMyx(t.getShearY());
		a.setMyy(t.getScaleY());
		a.setMyz(0);
		a.setTy(t.getTranslateY());
		
		a.setMzx(0);
		a.setMzy(0);
		a.setMzz(1);
		a.setTz(0);
		
		return a;
	}
	
	class Station {
		String name;
		Color color;
		double x;
		double y;
	}
}
