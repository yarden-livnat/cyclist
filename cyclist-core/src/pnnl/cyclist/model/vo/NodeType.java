package pnnl.cyclist.model.vo;

public class NodeType {
	private int _id;
	private String _censusReg;
	private int _EIAZone;
	private int _promod;
	
	
	public NodeType() {
	}
	
	public void setId(int id) {
		_id = id;
	}
	
	public void setCensusReg(String census) {
		_censusReg = census;
	}
	
	public void setEIAZone(int zone) {
		_EIAZone = zone;
	}
	
	public void setPromod(int promod) {
		_promod = promod;
	}

	public int getId() { return _id; }
	public String getCensusReg() { return _censusReg; }
	public int getEIAZone() { return _EIAZone; }
	public int getPromod() { return _promod; }
	
}
