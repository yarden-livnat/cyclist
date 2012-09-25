package weather.view.tool.view;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButtonBuilder;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.TextBuilder;
import javafx.scene.transform.Affine;
import javafx.util.Pair;
import pnnl.cyclist.model.vo.Node;
import pnnl.cyclist.model.vo.Station;
import pnnl.cyclist.model.vo.Weather;
import pnnl.cyclist.model.vo.WeatherData;
import pnnl.cyclist.model.vo.World;
import pnnl.cyclist.view.component.View;
import pnnl.cyclist.view.event.TimeEvent;
import weather.view.tool.component.WeatherPane;

public class WeatherMapView extends View {

	private enum Mode {STATIONS, WEATHER};
	
	private enum WeatherField {
		DRY_BULB ("Dry Bulb"),
		DEW_POINT("Dew Point"),
		HUMIDITY("Relative Humidity"),
		ATMOSPHERIC("Atmospheric Station"),
		RADIATION_HI("Radiation: H I"),
		RADIATION_DN("Radiation: D N"),
		RADIATION_DH("Radiation: D H"),
		WIND_DIRECTION("Wind Direction"),
		WIND_SPEED("Wind Speed"),
		COVER("Sky Cover");
		
		private String _title;
		private WeatherField(String text) {
			_title = text;
		}
	}
	
	private ObjectProperty<World> _worldProperty = new SimpleObjectProperty<>();
	private ObjectProperty<Weather> _weatherProperty = new SimpleObjectProperty<>();
	ObjectProperty<EventHandler<TimeEvent>> _actionProperty = new SimpleObjectProperty<>();
	
	private Canvas _canvas;
	private WeatherPane _pane;
	private Mode _mode = Mode.STATIONS;
	private WeatherField _field;


	private double _mouseX = 0;
	private double _mouseY = 0;
	
	private List<AffineTransform> _transforms = new ArrayList<>();
	
	public WeatherMapView() {
		super();
		init();
	}
	
	public ObjectProperty<World> dataProperty() {
		return _worldProperty;
	}
	
	public void setData(World map) {
		dataProperty().set(map);
	}
	
	public World getData() {
		return dataProperty().get();
	}
	
	
	public ObjectProperty<Weather> weatherProperty() {
		return _weatherProperty;
	}
	
	public void setWeather(Weather weather) {
		_weatherProperty.set(weather);
	}
	
	public Weather getWeather() {
		return _weatherProperty.get();
	}
	
	public ObjectProperty<EventHandler<TimeEvent>> onActionProperty() {
		return _actionProperty;
	}
	
	public EventHandler<TimeEvent> getOnAction() {
		return _actionProperty.get();
	}
	
	public void setOnAction(EventHandler<TimeEvent> handler) {
		_actionProperty.set(handler);
	}
	
	private void init() {
		HBox hbox = HBoxBuilder.create()
				.children(
						_pane = new WeatherPane(),
						createWeatherSelector()
				)
				.build();
		setContent(hbox);
		HBox.setHgrow(_pane, Priority.ALWAYS);
		_canvas = _pane.getCanvas();
		
		_worldProperty.addListener(new ChangeListener<World>() {

			@Override
			public void changed(ObservableValue<? extends World> observable, World oldValue, World newValue) 
			{
				if (newValue == null) {
					clearCanvas();
				}
				else {
					analyzeData();
					redraw();
				}
			}
		});
		
		_weatherProperty.addListener(new ChangeListener<Weather>() {

			@Override
			public void changed(ObservableValue<? extends Weather> observable, Weather oldValue, Weather newValue) {
				if (newValue != null)
					redraw();	
			}
		
		});
		
		_canvas.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {				
				World map = _worldProperty.get();
				if (map == null) return;
				
//				_transforms.set(0, initialTransform());
				redraw();
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
				redraw();
			}
		});
	}
	
	private void clearCanvas() {
		GraphicsContext gc = _canvas.getGraphicsContext2D();
		gc.setTransform(new Affine());
		gc.clearRect(0, 0, _canvas.getWidth(), _canvas.getHeight());
	}
	
	private void analyzeData() {
		World world = _worldProperty.get();
		if (world == null) return;
	
//		BoundingBox bbox = world.getBBox();
//		System.out.println("data min/max: x: ("+bbox.getMinX()+", "+bbox.getMaxX()+")  y: ("+bbox.getMinY()+", "+bbox.getMaxY()+")");
		_transforms.clear();
		_transforms.add(initialTransform());
	}

	
	private void redraw() {
		GraphicsContext gc = _canvas.getGraphicsContext2D();
		
		// clear screen
		gc.setTransform(new Affine());
		gc.clearRect(0, 0, _canvas.getWidth(), _canvas.getHeight());
		
		// get data
		World map = _worldProperty.get();
		if (map == null) return;	
		
		AffineTransform a = new AffineTransform();
		for (AffineTransform t : _transforms) {
			a.preConcatenate(t);
		}
		Affine af = convert(a);
		gc.setTransform(af);
		
		Point2D screenMin = new Point2D.Double(0,0);
		Point2D screenMax = new Point2D.Double(_canvas.getWidth(),_canvas.getHeight());

		Point2D worldBL = new Point2D.Double(0,0);
		Point2D worldTR = new Point2D.Double(0,0);
		
		try {
			a.inverseTransform(screenMin, worldBL);
			a.inverseTransform(screenMax, worldTR);
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		BoundingBox worldBBox = new BoundingBox(worldBL.getX(), worldTR.getY(), 
								worldTR.getX()-worldBL.getX(), worldBL.getY()-worldTR.getY());
		
//		System.out.println("world: "+worldBBox.getMinX()+" "+worldBBox.getMaxX()+"  y:"+worldBBox.getMinY()+" "+worldBBox.getMaxY());
		double unit = 1/a.getScaleX();
		gc.setLineWidth(unit);
		
		// draw
		gc.setFill(Color.GRAY);
		gc.setStroke(Color.BLACK);
		
		Mode drawMode = _mode;
		if (drawMode == Mode.WEATHER && _weatherProperty.get() == null)
			drawMode = Mode.STATIONS;
		
		switch (drawMode) {
		case STATIONS:
			drawStations(worldBBox, unit);
			break;
		case WEATHER:
			drawWeather(worldBBox, unit);
			break;
		}
		
		
		
	}	
	
	private void drawStations(BoundingBox worldBBox, double unit) {
		GraphicsContext gc = _canvas.getGraphicsContext2D();
		World world = getData();
		
		for (Station station : world.getStations()) {
			if (station.getBBox().intersects(worldBBox)) {
				gc.setFill(station.getColor());
				for (Node node : station.getNodes()) {
					gc.fillRect(node.getX()-Node.NODE_SIZE/2, node.getY()-Node.NODE_SIZE, Node.NODE_SIZE, Node.NODE_SIZE);
				}
				
				gc.setFill(Color.BLACK);
				gc.fillOval(station.getLon(), station.getLat(), 2*unit, 2*unit);
			} 
		}
	}
	
	private void drawWeather(BoundingBox worldBBox, double unit) {
		
		if (getWeather() == null) return;
		
		GraphicsContext gc = _canvas.getGraphicsContext2D();
		World world = getData();
		
		for (Station station : world.getStations()) {
			if (station.getBBox().intersects(worldBBox)) {
				WeatherData data = getWeather().getData().get(station.getId());
				if (data != null) {
					double value = data.getDryBulb();
					gc.setFill(getColor(value));
					for (Node node : station.getNodes()) {
						gc.fillRect(node.getX()-Node.NODE_SIZE/2, node.getY()-Node.NODE_SIZE, Node.NODE_SIZE, Node.NODE_SIZE);
					}
				}
			}
		}
	}
	
	private Color getColor(double value) {
		double f = Math.max(-1, Math.min(1, value/45));
		
		return value > 0 ? Color.WHITE.interpolate(Color.RED, f)
					: Color.WHITE.interpolate(Color.BLUE, -f);
	}
	
	private AffineTransform initialTransform() {
		BoundingBox bbox = getData().getBBox();
		double dx = bbox.getWidth(); 
		double dy = bbox.getHeight();
		double aspectRatio = dy/dx;
		double sx = _canvas.getWidth()/dx;
		double sy = _canvas.getHeight()/dy;
//		double s = _canvas.getHeight()/_canvas.getWidth() < aspectRatio ? sy : sx;
		double s = sx;
		
		AffineTransform a = AffineTransform.getTranslateInstance(-bbox.getMinX(), -bbox.getMinY());
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
	
	private Parent createWeatherSelector() {
		ToggleGroup group = new ToggleGroup();
		
		VBox vbox = VBoxBuilder.create()
				.spacing(5)
				.children(
					TextBuilder.create().text("Weather").build(),
					ToggleButtonBuilder.create()
						.text("Stations")
						.toggleGroup(group)
						.onAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									_mode = Mode.STATIONS;
									redraw();
								}
							})
						.build(),
					ToggleButtonBuilder.create()
						.text("Dry Bulb")
						.toggleGroup(group)
						.onAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									_mode = Mode.WEATHER;
									if (getWeather() == null) {
										onActionProperty().get().handle(new TimeEvent(TimeEvent.ANY, 1));
									} else {
										redraw();
									}
								}
							})
						.build()
					)
				.build();
		return vbox;
	}
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
