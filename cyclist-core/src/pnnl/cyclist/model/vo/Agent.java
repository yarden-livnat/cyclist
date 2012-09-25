package pnnl.cyclist.model.vo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Agent {

	private IntegerProperty id;
	private StringProperty name;
	private StringProperty type;
	private StringProperty agent;
	private IntegerProperty start;
	
	public Integer getId() {
		return id.get();
	}

	public String getName() {
		return name.get();
	}

	public String getType() {
		return type.get();
	}


	public String getAgent() {
		return agent.get();
	}

	

	public Integer getStart() {
		return start.get();
	}

	private IntegerProperty end;

	public Integer getEnd() {
		return end.get();
	}

	public Agent(int id, String name, String type, String agent,
			int constructed, int decomissioned) {
		this.id = new SimpleIntegerProperty(this, "id", id);
		this.name = new SimpleStringProperty(this, "name", name);
		this.type = new SimpleStringProperty(this, "type", type);
		this.agent = new SimpleStringProperty(this, "agent", agent);
		this.start = new SimpleIntegerProperty(this, "start", constructed);
		this.end = new SimpleIntegerProperty(this, "end", decomissioned);
	}
}
