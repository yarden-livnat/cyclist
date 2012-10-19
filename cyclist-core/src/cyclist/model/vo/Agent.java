package cyclist.model.vo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Agent {

	private IntegerProperty id;
	private StringProperty name;
	private StringProperty type;
	private StringProperty model;
	private StringProperty prototype;
	private IntegerProperty enter;
	private IntegerProperty leave;
	
	
	public Integer getId() {
		return id.get();
	}

	public String getName() {
		return name.get();
	}

	public String getType() {
		return type.get();
	}


	public String getModel() {
		return model.get();
	}

	public String getPrototype() {
		return prototype.get();
	}

	public Integer getEnter() {
		return enter.get();
	}

	

	public Integer getLeave() {
		return leave.get();
	}

	public Agent(int id, String name, String type, String model, String prototype,
			int enter, int leave) {
		this.id = new SimpleIntegerProperty(this, "id", id);
		this.name = new SimpleStringProperty(this, "name", name);
		this.type = new SimpleStringProperty(this, "type", type);
		this.model = new SimpleStringProperty(this, "model", model);
		this.prototype = new SimpleStringProperty(this, "prototype", prototype);
		this.enter = new SimpleIntegerProperty(this, "enter", enter);
		this.leave = new SimpleIntegerProperty(this, "leave", leave);
	}
}
