package pnnl.cyclist.model.vo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Facility {
	private IntegerProperty id;
	public Integer getId() { return id.get(); }
	
	private StringProperty name;
	public String getName() { return name.get(); }
	
	private StringProperty type;
	public String getType() { return type.get(); }
	
	private StringProperty institute;
	public String getInstitute() { return institute.get(); }
	
	private StringProperty region;
	public String getRegion() { return region.get(); }
	
	private IntegerProperty start;
	public Integer getStart() { return start.get(); }
	
	private IntegerProperty end;
	public Integer getEnd() { return end.get(); }
	
	public Facility(int id, String name, String type, String institute, String region, int start, int end) {
		this.id = new SimpleIntegerProperty(this, "id", id);
		this.name = new SimpleStringProperty(this, "name", name);
		this.type = new SimpleStringProperty(this, "type", type);
		this.institute = new SimpleStringProperty(this, "institute", institute);
		this.region = new SimpleStringProperty(this, "region", region);
		this.start = new SimpleIntegerProperty(this, "start", start);
		this.end = new SimpleIntegerProperty(this, "end", end);
	}
}
