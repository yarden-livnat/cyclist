package pnnl.cyclist.model.vo;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class Weather {

	private Date _time;
	private int _timeId;
	private Map<Integer, WeatherData> _data = new HashMap<>(); // Station.id
	
	public Weather() {
	}
	
	public void setTime(Date time) {
		_time = time;
	}
	
	public Date getTime() {
		return _time;
	}
	
	public void setTimeId(int id) {
		_timeId = id;
	}
	
	public int getTimeId() {
		return _timeId;
	}
	
	public void setData(Map<Integer, WeatherData> data) {
		_data = data;
	}
	
	public Map<Integer, WeatherData> getData() {
		return _data;
	}
	
	public void addData(WeatherData data) {
		_data.put(data.getStation().getId(), data);
	}
}
