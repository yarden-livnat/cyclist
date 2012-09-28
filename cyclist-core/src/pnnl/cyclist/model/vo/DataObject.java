package pnnl.cyclist.model.vo;

public class DataObject {
	public Object[] attribute;
	
	public DataObject() {
		this(0);
	}
	
	public DataObject(int size) {
		resize(size);
	}
	
	public void resize(int size) {
		attribute = new Object[size];
	}
	
	public int getSize() {
		return attribute.length;
	}
}
