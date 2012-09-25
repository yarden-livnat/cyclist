package pnnl.cyclist.model.vo;

import java.util.ArrayList;
import java.util.List;

public class Intersect {
	private int _id;
	private Station _station;
	private String	_timezone;
	private NodeType _rbg;
	
	List<Node> _nodes = new ArrayList<>();
	
	public Intersect() {
		
	}
	
	public int getId() { return _id; }
	public void setId(int id) { _id = id; }
	
	public Station getStation() { return _station; }
	public void setStation(Station station) { _station = station; }
	
	public String getTimezone() { return _timezone; }
	public void setTimezone(String timezone) { _timezone = timezone; }
	
	public NodeType getNodeType() { return _rbg; }
	public void setNodeType(NodeType type) { _rbg = type; }
	
	public void addNode(Node node) { _nodes.add(node); }
	public List<Node> getNodes() { return _nodes; }
}
