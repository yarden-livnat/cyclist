package pnnl.cyclist.model.vo;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.BoundingBox;
import javafx.scene.paint.Color;

public class Station {

	private int _id;
	private String _name;
	private String _state;
	private double _lat;
	private double _lon;
	private int _elevation;
	private Node _localNode;
	
	private Color _color = Color.color(Math.random(), Math.random(), Math.random());
	private List<Node> _nodes = new ArrayList<>();
	private BoundingBox _bbox = null;
	
	public Station() {
	}
	
	public void setId(int id) {
		_id = id;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public void setState(String state) {
		_state = state;
	}
	
	public void setLat(double lat) {
		_lat = lat;
	}
	
	public void setLon(double lon) {
		_lon = lon;
	}
	
	public void setElevation(int elevation) {
		_elevation = elevation;
	}
	
	public void setLocalNode(Node node) {
		_localNode = node;
	}
	
	public void setColor(Color color) {
		_color = color;
	}
	
	public void setNodes(List<Node> nodes) {
		_nodes = nodes;
		_bbox = null;
	}
	
	public void addNode(Node node) {
		_nodes.add(node);
		_bbox = null;
	}
	
	public BoundingBox getBBox() {
		if (_bbox == null) {
			if (_nodes != null && _nodes.size() > 0) {
				double minX = _nodes.get(0).getX();
				double minY = _nodes.get(0).getY();
				double maxX = minX;
				double maxY = minY;
				
				for (Node node : _nodes) {
					if (node.getX() < minX) minX = node.getX();
					else if (node.getX() > maxX) maxX = node.getX();
					
					if (node.getY() < minY) minY = node.getY();
					else if (node.getY() > maxY) maxY = node.getY();
				}
				
				_bbox = new BoundingBox(minX-Node.NODE_SIZE/2, minY-Node.NODE_SIZE/2, maxX-minX+Node.NODE_SIZE, maxY-minY+Node.NODE_SIZE);
			} else {
				_bbox = new BoundingBox(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0, 0);
			}
		}
		return _bbox;
	}
	
	public int getId() { return _id; }
	
	public String getName() { return _name; }
	
	public String getState() { return _state; }
	
	public double getLat() { return _lat; }
	
	public double getLon() { return _lon; }
	
	public int getElevation() { return _elevation; }
	
	public Node getLocalNode() { return _localNode; }
	
	public Color getColor() { return _color; }
	
	public List<Node> getNodes() {
		return _nodes;
	}
	

}
