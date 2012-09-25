package weather.view.tool.component;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.geotools.data.FileDataStore;
//import org.geotools.data.FileDataStoreFinder;
//import org.geotools.data.simple.SimpleFeatureCollection;
//import org.geotools.data.simple.SimpleFeatureIterator;
//import org.geotools.data.simple.SimpleFeatureSource;
//import org.opengis.feature.simple.SimpleFeature;
//
//import com.vividsolutions.jts.geom.Coordinate;
//import com.vividsolutions.jts.geom.Geometry;
//import com.vividsolutions.jts.geom.MultiPolygon;
//import com.vividsolutions.jts.geom.Point;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import cyclist.model.vo.Weather;
import cyclist.view.component.View;

public class WeatherMapView extends View {

//	private ObjectProperty<ObservableList<Weather>> _dataProperty = new SimpleObjectProperty<>();
//	private Canvas _canvas;
//	private WeatherMap _map;
//
//	private double _minX, _maxX;
//	private double _minY, _maxY;
//	private double _mouseX = 0;
//	private double _mouseY = 0;
//	
//	private List<AffineTransform> _transforms = new ArrayList<>();
//	
//	private Map<String, Station> _stations = new HashMap<>();
	
	public WeatherMapView() {
		super();
//		init();
	}
	
//	public ObjectProperty<ObservableList<Weather>> dataProperty() {
//		return _dataProperty;
//	}
//	
//	public void setData(ObservableList<Weather> list) {
//		dataProperty().set(list);
//	}
//	
//	public ObservableList<Weather> getData() {
//		return dataProperty().get();
//	}
//	
//	private void init() {
//		_map = new WeatherMap();
//		setContent(_map);
//		_canvas = _map.getCanvas();
//		_dataProperty.addListener(new ChangeListener<List<Weather>>() {
//
//			@Override
//			public void changed(
//					ObservableValue<? extends List<Weather>> observable,
//					List<Weather> oldValue, List<Weather> newValue) 
//			{
//				if (newValue == null || newValue.size() == 0) {
//					// clear canvas
//				}
//				else {
//					analyzeData();
//					redraw();
//				}
//			}
//		});
//		
//		_canvas.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {				
//				ObservableList<Weather> list = _dataProperty.get();
//				if (list == null || list.size() == 0) return;
//				
//				_transforms.set(0, initialTransform());
//				redraw();
//			}
//			
//		});
//		
//		_canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent e) {
//				_mouseX = e.getX();
//				_mouseY = e.getY();	
//				
//				_transforms.add(new AffineTransform());
//			}
//		});
//		
//		_canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent e) {
//				if (e.getButton() == MouseButton.SECONDARY || (e.getButton() == MouseButton.PRIMARY && e.isControlDown())) {
//					double dy = (e.getY() - _mouseY)/30;
//					double s = dy > 0 ? 1+dy : 1/ (1-dy);
//					AffineTransform a = AffineTransform.getTranslateInstance(_mouseX, _mouseY);
//					a.scale(s, s);
//					a.translate(-_mouseX, -_mouseY);
//							
//					_transforms.set(_transforms.size()-1, a);
//				} else if (e.getButton() == MouseButton.PRIMARY) {
//					double dx = e.getX() - _mouseX;
//					double dy = e.getY() - _mouseY;
//					_transforms.set(_transforms.size()-1, AffineTransform.getTranslateInstance(dx, dy));
//					
//				} 
//				redraw();
//			}
//		});
//	}
//	
//	private void analyzeData() {
//		ObservableList<Weather> list = _dataProperty.get();
//		if (list == null || list.size() == 0) return;
//		
//		_minX = _maxX = list.get(0).x;
//		_minY = _maxY = list.get(0).y;
//		_stations = new HashMap<>();
//		
//		// compute bbox and stations
//		for (Weather w : list) {
//			if (w.x < _minX) _minX = w.x;
//			else if (w.x > _maxX) _maxX = w.x;
//			
//			if (w.y < _minY) _minY = w.y;
//			else if (w.y > _maxY) _maxY = w.y;
//			
//			Station s = _stations.get(w.name);
//			if (s == null) {
//				s = new Station();
//				s.name = w.name;
//				s.x = w.stationX;
//				s.y = w.stationY;
//				s.color = Color.color(Math.random(), Math.random(), Math.random());
//				_stations.put(w.name, s);
//			}
//			s.add(w);
//		}
//				
//		System.out.println("data min/max: x: ("+_minX+", "+_maxX+")  y: ("+_minY+", "+_maxY+")");
//		_transforms.clear();
//		_transforms.add(initialTransform());
//	}
//
//	
//	private void redraw() {
//		GraphicsContext gc = _canvas.getGraphicsContext2D();
//		
//		// clear screen
//		gc.setTransform(new Affine());
//		gc.clearRect(0, 0, _canvas.getWidth(), _canvas.getHeight());
//		
//		// get data
//		ObservableList<Weather> list = _dataProperty.get();
//		if (list == null || list.size() == 0) return;	
//		
//		AffineTransform a = new AffineTransform();
//		for (AffineTransform t : _transforms) {
//			a.preConcatenate(t);
//		}
//		Affine af = convert(a);
//		gc.setTransform(af);
//		
//		Point2D screenMin = new Point2D.Double(0,0);
//		Point2D screenMax = new Point2D.Double(_canvas.getWidth(),_canvas.getHeight());
//
//		Point2D worldBL = new Point2D.Double(0,0);
//		Point2D worldTR = new Point2D.Double(0,0);
//		
//		try {
//			a.inverseTransform(screenMin, worldBL);
//			a.inverseTransform(screenMax, worldTR);
//		} catch (NoninvertibleTransformException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return;
//		}
//		
//		Bound world = new Bound();
//		world.minX = Math.min(worldBL.getX(), worldTR.getX());
//		world.minY = Math.min(worldBL.getY(), worldTR.getY());
//		world.maxX = Math.max(worldBL.getX(), worldTR.getX());
//		world.maxY = Math.max(worldBL.getY(), worldTR.getY());
//		
////		System.out.println("world: "+world.minX+" "+world.maxX+"  y:"+world.minY+" "+world.maxY);
//		double unit = 1/a.getScaleX();
//		gc.setLineWidth(unit);
//		
//		// draw
//		gc.setFill(Color.GRAY);
//		gc.setStroke(Color.BLACK);
//		
//		double d = 0.125/2;
//		int n=0;
//		int m=0;
//		for (Station station : _stations.values()) {
//			if (station.bounds.overlap(world)) {
//				n++;
//				gc.setFill(station.color);
//				for (Weather w : station.sites) {
//					m++;
//					gc.fillRect(w.x-d, w.y-d, 2*d, 2*d);
//				}
//				gc.setFill(Color.BLACK);
//				gc.fillOval(station.x, station.y, 5*unit, 5*unit);
//			}
//		}
////		System.out.println("n="+n+"/"+_stations.size()+"  m="+m);
//	}	
//	
//	private AffineTransform initialTransform() {
//		double dx = _maxX - _minX;
//		double dy = _maxY - _minY;
//		double aspectRatio = dy/dx;
//		double sx = _canvas.getWidth()/dx;
//		double sy = _canvas.getHeight()/dy;
//		double s = _canvas.getHeight()/_canvas.getWidth() < aspectRatio ? sy : sx;
//		
//		
//		AffineTransform a = AffineTransform.getTranslateInstance(-_minX, -_minY);
//		a.preConcatenate(AffineTransform.getScaleInstance(s, -s));
//		a.preConcatenate(AffineTransform.getTranslateInstance(0, _canvas.getHeight()));
//		
//		return a;
//	}
//	
//	private Affine convert(AffineTransform t) {
//		Affine a = new Affine();
//		
//		a.setMxx(t.getScaleX());
//		a.setMxy(t.getShearX());
//		a.setMxz(0);
//		a.setTx(t.getTranslateX());
//		
//		a.setMyx(t.getShearY());
//		a.setMyy(t.getScaleY());
//		a.setMyz(0);
//		a.setTy(t.getTranslateY());
//		
//		a.setMzx(0);
//		a.setMzy(0);
//		a.setMzz(1);
//		a.setTz(0);
//		
//		return a;
//	}
//	
//	class Station {
//		String name;
//		Color color;
//		double x;
//		double y;
//		Bound bounds = new Bound();
//		
//		List<Weather> sites = new ArrayList<Weather>();
//		
//		public void add(Weather site) {
//			sites.add(site);
//			bounds.add(site.x, site.y);
//		}
//	}
//	
//	class Bound {
//		double minX = Double.MAX_VALUE;
//		double minY = Double.MAX_VALUE;
//		double maxX = -Double.MAX_VALUE;
//		double maxY = -Double.MAX_VALUE;
//		
//		public void add(double x, double y) {
//			if(x < minX) minX = x;
//			else if(x > maxX) maxX = x;
//			
//			if (y < minY) minY = y;
//			else if (y > maxY) maxY = y;
//		}
//		
//		public boolean overlap(Bound other) {
//			return (maxX > other.minX && other.maxX > minX && maxY > other.minY && other.maxY > minY);
//		}
//	}
}
