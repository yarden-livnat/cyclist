package weather.view.gis.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WeatherStationFeature implements Feature {

	public WeatherStation station;
	public Color color;
	
	public WeatherStationFeature(WeatherStation station) {
		this.station = station;
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(color);
		gc.fillOval(station.x, station.y, 5, 5);
		
	}
}
