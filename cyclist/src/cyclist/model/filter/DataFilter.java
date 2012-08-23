package cyclist.model.filter;

public class DataFilter {

	public enum Type {SRC, DEST};
	
	private Type _type;
	
	public DataFilter(Type type) {
		_type = type;
	}
	
	public Type getType() {
		return _type;
	}
}
