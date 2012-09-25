package pnnl.cyclist.model.vo;

public class DnDContext {
	
	public enum ContextType {Agent, Material};
	
	public ContextType contextType;
	
	public DnDContext(ContextType type) {
		contextType = type;
	}
}
