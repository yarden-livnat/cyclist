package weather.view.tool.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class WeatherPane extends Pane {

	private Canvas _canvas;
	
	public WeatherPane() {
		_canvas = new Canvas(400, 300);
		getChildren().add(_canvas);
		minWidth(300);
		minHeight(200);
		prefWidth(600);
		prefHeight(400);
	}
	
	@Override
	public boolean isResizable() {
		return true;
	}
	
	public void resize(double w, double h) {
		super.resize(w, h);
		_canvas.setWidth(w);
		_canvas.setHeight(h);
	}
	
	public Canvas getCanvas() {
		return _canvas;
	}
	
}
