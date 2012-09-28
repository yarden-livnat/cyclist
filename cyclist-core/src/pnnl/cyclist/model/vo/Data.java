package pnnl.cyclist.model.vo;

import java.util.ArrayList;
import java.util.List;

public class Data {
	private String _attributeName[];
	private List<DataObject> _items = new ArrayList<>();
	
	public Data(String attributeName[]) {
		_attributeName = attributeName;
	}
	
	public String[] getAttributeNames() {
		return _attributeName;
	}
	
	public List<DataObject> getItems() {
		return _items;
	}
	
	public int size() {
		return _items.size();
	}
}
