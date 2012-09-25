package pnnl.cyclist.model.vo;

import java.sql.Date;

public class WeatherData {
	
	private final Date _time;
	private final int _timeId;
	private final Intersect _intersect;
	
	private final double _dryBulb;
	private final double _dewPoint;
	private final int _relativeHumidity;
	private final int _atmosphericStation;
	private final int _radiationHI;
	private final int _radiationDN;
	private final int _radiationDH;
	private final int _windDirection;
	private final double _windSpeed;
	private final int _opaqueSkyCover;
	
	public WeatherData( Date time, int timeId, Intersect intersect,  
			double dryBulb, double dewPoint, int relativeHumidity, int atmosphericStation,
			int radiationHI, int radiationDN, int radiationDH, int windDirection, double winSpeed,
			int opaqueSkyCover) 
	{
		this._time = time;
		this._timeId = timeId;
		this._intersect = intersect;
		this._dryBulb = dryBulb;
		this._dewPoint = dewPoint;
		this._relativeHumidity = relativeHumidity;
		this._atmosphericStation = atmosphericStation;
		this._radiationHI = radiationHI;
		this._radiationDN = radiationDN;
		this._radiationDH = radiationDH;
		this._windDirection = windDirection;
		this._windSpeed = _windDirection;
		this._opaqueSkyCover = opaqueSkyCover;
	}
	
	public Date getTime() { return _time; }
	public int getTimeId() { return _timeId; }
	public Intersect getIntersect() { return _intersect; }

	public double getDryBulb() { return _dryBulb; }
	public double getDewPoint() { return _dewPoint; }
	public int getRelativeHumidity() { return _relativeHumidity; }
	public int getAtmosphericStation() { return _atmosphericStation; }
	public int getRadiationHI() { return _radiationHI; }
	public int getRadiationDN() { return _radiationDN; }
	public int getRadiationDH() { return _radiationDH; }
	public int getWindDirection() { return _windDirection; }
	public double getWindSpeed() { return _windSpeed; }
	public int getOpaqueSkyCover() { return _opaqueSkyCover; }
}
