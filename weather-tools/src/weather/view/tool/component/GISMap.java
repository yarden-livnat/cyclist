package weather.view.tool.component;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class GISMap extends Pane {

	private List<Layer> _layers = new ArrayList<>();
	private List<AffineTransform> _transforms = new ArrayList<>();
	private double _mouseX;
	private double _mouseY;
	
//	private boolean _valid = true;
	
	public GISMap() {
		init();
	}
	
	public List<Layer> getLayers() {
		return _layers;
	}
	

	public void addLayer(Layer l) {	
		_layers.add(l);
		getChildren().add(l);
		l.resize(getWidth(), getHeight());
	}
	
	public void resize(double w, double h) {
		super.resize(w, h);
		for (Layer l : _layers) {
			l.resize(w, h);
		}
		
		redraw();
//		invalidate();
	}
	
	public void redraw() {
		AffineTransform a = getAffineTransform();
		for (Layer l : _layers) {
			l.redraw(a);
		}
	}
	
	private AffineTransform getAffineTransform() {
		AffineTransform a = AffineTransform.getTranslateInstance(0, getHeight());
		for (AffineTransform t : _transforms) {
			a.preConcatenate(t);
		}
		
		return a;
	}

//	private void invalidate() {
//		if (_valid) {
//			_valid = false;
//			Platform.runLater(new Runnable() {
//				@Override
//				public void run() {
//					validate();
//				}
//			});
//		}
//	}
//	
//	private void validate() {
//		redraw();
//		_valid = true;
//	}
	
	private void init() {
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				_mouseX = e.getX();
				_mouseY = e.getY();	
				
				_transforms.add(new AffineTransform());
			}
		});
	
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				System.out.println("mouse drag");
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
		
		boundsInLocalProperty().addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable arg0) {
				System.out.println("bounds invalidated");
			}
		});
	}
}
