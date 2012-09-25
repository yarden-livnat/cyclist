package cyclist.model.vo;

import cyclist.model.vo.DnDContext;

public class AgentContext extends DnDContext {

	public String type;
	public String value;
	
	public AgentContext(String type, String value) {
		super(DnDContext.ContextType.Agent);
		
		this.type = type;
		this.value = value;
	}
}
