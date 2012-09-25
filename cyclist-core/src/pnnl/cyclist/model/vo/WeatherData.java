package pnnl.cyclist.model.vo;

import java.sql.Date;

public class WeatherData {
	
	private final Date _time;
	private final Station _station;
	private final NodeType _type;
	private final String _timezone;
	
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
	
	public WeatherData( Date time, Station station, NodeType type, String timezone,  
			double dryBulb, double dewPoint, int relativeHumidity, int atmosphericStation,
			int radiationHI, int radiationDN, int radiationDH, int windDirection, double winSpeed,
			int opaqueSkyCover) 
	{
		this._time = time;
		this._station = station;
		this._timezone = timezone;
		this._type = type;
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
	public Station getStation() { return _station; }
	public NodeType getType() { return _type; }
	public String getTimezone() { return _timezone; }
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
