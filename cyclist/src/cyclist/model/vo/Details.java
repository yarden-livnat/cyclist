package cyclist.model.vo;

import cyclist.model.filter.Param;

public class Details {
	public enum Type {GENERAL, SRC, DEST, NONE};
	
	public Type type;
	public Param.Type param;
	
	public Details(Type type, Param.Type param) {
		this.type = type;
		this.param = param;
	}
}
