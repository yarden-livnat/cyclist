package cyclist.model.vo;

public class Weather {

	public String name;
	public float stationX;
	public float stationY;
	public int newID;
	public float x;
	public float y;
	public String stateName;
	public int stateFIPS;
	public String countyName;
	public int countyFIPS;
	
	public int from;
	public int to;
	public int measurements[];
	
	
	public Weather() {	
	}
	
	public String getName() {
		return name;
	}
	
	public float getStationX() {
		return stationX;
	}

	public float getStationY() {
		return stationY;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public String getStateName() {
		return stateName;
	}
	
	public int getStateFIPS() {
		return stateFIPS;
	}

	public String getCountyName() {
		return countyName;
	}
	
	public int getCountyFIPS() {
		return countyFIPS;
	}
	
	public int[] getMeasurements() {
		return measurements;
	}
}
