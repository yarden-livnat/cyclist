package pnnl.cyclist.model.vo;

import java.util.ArrayList;
import java.util.List;

public class Table {

	public enum COL_TYPE {INT, REAL, STRING};
	
	private String _name;
	private String _header[];
	private COL_TYPE _types[];
//	private List<Object[]> _rows = new ArrayList<>();	
	private List<TableRow> _rows = new ArrayList<>();
	
	public Table() {
		this("");
	}
	public Table(String name) {
		_name = name;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setHeaders(String header[]) {
		_header = header;
	}
	
	public String[] getHeader() {
		return _header;
	}
	
	public String getHeader(int index) {
		return _header[index];
	}
	
	public int getNumColumns() {
		return _header.length;
	}
	
	
	public void setTypes(COL_TYPE types[]) {
		_types = types;
	}
	
	public COL_TYPE[] getTypes() {
		return _types;
	}
	
	public COL_TYPE getType(int index) {
		return _types[index];
	}
	
	public void addRow(TableRow row) {
		_rows.add(row);
	}
	
	public List<TableRow> getRows() {
		return _rows;
	}
	
	public TableRow getRow(int index) {
		return _rows.get(index);
	}
	
	public class TableRow  {
		public Object[] value;
		
		public TableRow(int size) {
			value = new Object[size];
		}
	}
}
