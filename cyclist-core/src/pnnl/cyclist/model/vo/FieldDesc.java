package pnnl.cyclist.model.vo;

import java.util.HashMap;
import java.util.Map;

public class FieldDesc {

	private String _name;
	private Map<String, String> _properties = new HashMap<>();
	
	
	public FieldDesc(String name) {
		this._name = name;
	}
	
	public String getName() {
		return _name;
	}
	
	
	public String getDataType() {
		return _properties.get(FieldDescProperties.DATA_TYPE);
	}
	
	public void set(String property, String value) {
		_properties.put(property, value);
	}
	
	public String get(String property) {
		return _properties.get(property);
	}
}
