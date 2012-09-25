package pnnl.cyclist.model.filter;

import java.util.EnumSet;

public class Param {
	public enum Type {
		AGENT, AGENT_TYPE, 
		FACILITY, FACILITY_TYPE, 
		INSTITUTE, INSTITUTE_TYPE, 
		REGION, REGION_TYPE, 
		MARKET, MARKET_TYPE,
		ELEMENT, 
		ISOTOPE,
		NONE
	};
	
	public static final EnumSet<Type> SRC_DEST = EnumSet.of(
		Type.FACILITY, Type.FACILITY_TYPE, 
		Type.INSTITUTE, Type.INSTITUTE_TYPE, 
		Type.REGION, Type.REGION_TYPE	
	);
	
	public static final EnumSet<Type> GENERAL = EnumSet.of(
		Type.MARKET, Type.MARKET_TYPE,
		Type.ELEMENT, 
		Type.ISOTOPE	
	);
	
	public Type type;
	public Object value;

	public Param(Type type, Object value) {
		this.type = type;
		this.value = value;
	}
}
