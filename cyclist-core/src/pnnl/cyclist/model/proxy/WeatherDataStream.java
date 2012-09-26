package pnnl.cyclist.model.proxy;

import java.util.Collection;

import pnnl.cyclist.model.vo.MonthDegreeDay;
import pnnl.cyclist.model.vo.Weather;
import pnnl.cyclist.model.vo.World;
import javafx.beans.property.ReadOnlyObjectProperty;

public interface WeatherDataStream {
	ReadOnlyObjectProperty<World> getWorld();
	ReadOnlyObjectProperty<Weather> getWeather(int timeId);
	ReadOnlyObjectProperty<Collection<MonthDegreeDay>> getDegreeDays();
}
