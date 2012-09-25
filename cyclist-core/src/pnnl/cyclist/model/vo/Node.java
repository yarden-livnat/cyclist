package pnnl.cyclist.model.vo;

public class Node {

	public static final double NODE_SIZE = 0.125;
	
	private int _id;
	private double _x;
	private double _y;
	private String _timezone;
	private NodeType _type;
	private String _state;
	private String _county;
	private Station _station;
	
	public Node() {
	}
	
	public void setId(int id) {
		this._id = id;
	}
	
	public void setX(double x) {
		_x = x;
	}
	
	public void setY(double y) {
		_y = y;
	}
	
	public void setTimezone(String timezone) {
		_timezone = timezone;
	}
	
	public void setType(NodeType type) {
		_type = type;
	}
	
	public void setState(String state) {
		_state = state;
	}
	
	public void setCounty(String county) {
		_county = county;
	}
	
	public void setStation(Station station) {
		_station = station;
	}
	
	public int getId() { return _id; }
	public double getX() { return _x; }
	public double getY() { return _y; }
	public String getTimezone() { return _timezone; }
	public NodeType getType() { return _type; }
	public String getState() { return _state; }
	public String getCounty() { return _county; }
	public Station getStation() { return _station; }
}
