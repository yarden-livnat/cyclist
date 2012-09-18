package weather.view.tool.component;

import java.awt.geom.AffineTransform;

import weather.utils.AffineUtils;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Layer extends Canvas {

	private String _name;
	public GraphicsContext gc;
	
	public Layer(String name) {
		_name = name;
		init();
		clear();
	}
	
	public String getName() {
		return _name;
	}
	
	public void resize(double w, double h) {
		setWidth(w);
		setHeight(h);
	}
	
	public void clear() {
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, getWidth(), getHeight());
	}
	
	public void redraw() {
		redraw(new AffineTransform());
	}
	
	public void redraw(AffineTransform t) {
		clear();
		
		setAffineTransform(t);
		
		gc.setFill(Color.GRAY);
		gc.fillOval(50, 50, 20, 10);
	}
	
	private void init() {
		gc = getGraphicsContext2D();
	}
	
	private void setAffineTransform(AffineTransform t) {
		AffineTransform a = AffineTransform.getScaleInstance(1, -1);
		a.preConcatenate(t);
		
		gc.setTransform(AffineUtils.convert(a));

	}
}
