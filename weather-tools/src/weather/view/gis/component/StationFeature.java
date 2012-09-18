package weather.view.gis.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class StationFeature implements Feature {

	private double _x;
	private double _y;
	private Color _color;
	
	public StationFeature(double x, double y, Color color) {
		_x = x;
		_y = y;
		_color = color;
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(_color);
		gc.setFill(Color.RED);
		gc.fillOval(_x, _y, 10, 10);
	}

}
