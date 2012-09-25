package cyclist.model.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cyclist.model.filter.Param;

public class FilterSet {

	public class FilterEntry {
		public String name;
		public Object value;
		
		public FilterEntry(String name, Object value) {
			this.name = name;
			this.value = value;
		}
	}
	
	private String _type;
	private Map<Param.Type, List<FilterEntry>> _map = new HashMap<>();
	
	public FilterSet(String type) {
		_type = type;
	}
	
	public String getType() {
		return _type;
	}
	
	public boolean add(Param.Type type, String name, Object value) {
		return add(type, new FilterEntry(name, value));
	}
	
	public boolean add(Param.Type type, FilterEntry entry) {
		List<FilterEntry> list = _map.get(type);
		if (list == null) {
			list = new ArrayList<FilterEntry>();
			_map.put(type, list);
		}
		
		for (FilterEntry item : list) {
			if (item.name.equals(entry.name))
				return false;
		}

		list.add(entry);
		return true;
	}
	
	public void remove(Param.Type key, String name) {
		List<FilterEntry> items = _map.get(key);
		if (items != null) {
			for (FilterEntry entry : items) {
				if (entry.name.equals(name)) {
					items.remove(entry);
					if (items.size() == 0) 
						_map.remove(key);
					return;
				}
			}
		}
	}
	
	public void remove(Param.Type key, FilterEntry entry) {
		List<FilterEntry> items = _map.get(key);
		if (items != null) {
			if (items.remove(entry)) {
				if (items.size() == 0)
					_map.remove(key);
			}
		}
	}
	
	public Set<Entry<Param.Type, List<FilterEntry>>> getEntries() {
		return _map.entrySet();
	}
	
	public Set<Param.Type> getKeys() {
		return _map.keySet();
	}
	
	public List<FilterEntry> getItems(Param.Type key) {
		return _map.get(key);
	}
	
	public boolean containsKey(Param.Type key) {
		return _map.containsKey(key);
	}
}
