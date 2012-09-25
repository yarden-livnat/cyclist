package pnnl.cyclist.model.proxy;

import pnnl.cyclist.model.vo.Weather;
import pnnl.cyclist.model.vo.World;
import javafx.beans.property.ReadOnlyObjectProperty;

public interface WeatherDataStream {
	ReadOnlyObjectProperty<World> getWorld();
	ReadOnlyObjectProperty<Weather> getWeather(int timeId);
}
