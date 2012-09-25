package pnnl.cyclist.model.vo;

public enum WeatherField {
	
	DRY_BULB ("Dry Bulb"),
	DEW_POINT("Dew Point"),
	HUMIDITY("Relative Humidity"),
	ATMOSPHERIC("Atmospheric Station"),
	RADIATION_HI("Radiation: H I"),
	RADIATION_DN("Radiation: D N"),
	RADIATION_DH("Radiation: D H"),
	WIND_DIRECTION("Wind Direction"),
	WIND_SPEED("Wind Speed"),
	COVER("Sky Cover");
	
	private String _title;
	private WeatherField(String text) {
		_title = text;
	}
	
	public String getTitle() {
		return _title;
	}
}
