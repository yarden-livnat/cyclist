package pnnl.cyclist.model.vo;

public class Node {

	public static final double NODE_SIZE = 0.125;
	
	private int _id;
	private double _x;
	private double _y;
	private Intersect _intersect;
	private String _state;
	private String _county;
	
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
	
	public void setState(String state) {
		_state = state;
	}
	
	public void setCounty(String county) {
		_county = county;
	}
	
	public void setIntersect(Intersect intersect) {
		_intersect = intersect;
	}
	
	public int getId() { return _id; }
	public double getX() { return _x; }
	public double getY() { return _y; }
	public Intersect getIntersect() { return _intersect; }
	public String getState() { return _state; }
	public String getCounty() { return _county; }
}
