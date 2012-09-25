package pnnl.cyclist.model.vo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javafx.geometry.BoundingBox;

public class World {

	private Map<Integer, Station> _stations;
	private Map<Integer, Node> _nodes;
	private Map<Integer, NodeType> _nodeTypes;
	private BoundingBox _bbox = null;
	
	public World() {
	}
	
	/*
	 * Stations
	 */
	
	public void setStations(Map<Integer, Station> stations) {
		_stations = stations;
	}
	
	public Collection<Station> getStations() {
		return _stations.values();
	}
	
	public Station getStation(int stationId) {
		return _stations.get(stationId);
	}
	
	public Map<Integer, Station> getStationsMap() {
		return _stations;
	}
	
	
	/*
	 * Nodes
	 */
	
	public void setNodes(Map<Integer, Node> nodes) {
		_nodes = nodes;
	}
	
	public Collection<Node> getNodes() {
		return _nodes.values();
	}
	
	public Node getNode(int nodeId) {
		return _nodes.get(nodeId);
	}
	
	public Map<Integer, Node> getNodesMap() {
		return _nodes;
	}
	
	
	/*
	 * NodeType
	 */
	public void setNodeTypes(Map<Integer, NodeType> map) {
		_nodeTypes = map;
	}
	
	public NodeType getNodeType(int typeId) {
		return _nodeTypes.get(typeId);
	}
	
	public BoundingBox getBBox() {
		if (_bbox == null) {
			Node first = _nodes.values().iterator().next();
			double minX = first.getX();
			double minY = first.getY();
			double maxX = minX;
			double maxY = minY;
		
			for (Node node : _nodes.values()) {
				if (node.getX() < minX) minX = node.getX();
				else if (node.getX() > maxX) maxX = node.getX();
			
				if (node.getY() < minY) minY = node.getY();
				else if (node.getY() > maxY) maxY = node.getY();
			}
			
			_bbox = new BoundingBox(minX, minY, maxX-minX, maxY - minY);
		}
		return _bbox;
	}
}
