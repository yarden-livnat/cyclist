package pnnl.cyclist.model.vo;

import java.util.Map;
import java.util.TreeMap;

public class DataCollection {
	private Map<String, Data> _collection = new TreeMap<>();
	
	public DataCollection() {
		
	}
	
	public Map<String, Data> getCollection() {
		return _collection;
	}
	
	public int size() {
		return _collection.size();
	}
	
	public Data get(String key) {
		return _collection.get(key);
	}
	
	
}
