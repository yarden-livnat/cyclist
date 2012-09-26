package pnnl.cyclist.model.vo;

public class MonthDegreeDay {
	private Intersect _intersect;
	
	private double _hddPerMonth[] = new double[12];
	private double _cddPerMonth[] = new double[12];

	private double _hdd = 0;
	private double _cdd = 0;
	
	private int _hddCountPerMonth[] = new int[12];
	private int _cddCountPerMonth[] = new int[12];
	
	private int _hddCount = 0;
	private int _cddCount = 0;
 
	public MonthDegreeDay() {		
	}
	
	public void setIntersect(Intersect value) { _intersect = value; }
	public Intersect getIntersect() { return _intersect; }
	
	public void setHDD(double value, int month) { _hddPerMonth[month] = value; _hdd += value; }
	public double getHDD() { return _hdd; }
	public double[] getHDDPerMonth() { return _hddPerMonth; }
	
	public void setCDD(double value, int month) { _cddPerMonth[month] = value; _cdd += value; }
	public double getCDD() { return _cdd; }
	public double[] getCDDPerMonth() { return _cddPerMonth; }
	
	public void setHDDCount(int value, int month) { _hddCountPerMonth[month] = value; _hddCount += value; }
	public int getHDDCount() { return _hddCount; }
	public int[] getHDDCountPerMonth() { return _hddCountPerMonth; }
	
	public void setCDDCount(int value, int month) { _cddCountPerMonth[month] = value; _cddCount += value; }
	public int getCDDCount() { return _cddCount; }
	public int[] getCDDCountPerMonth() { return _cddCountPerMonth; }
}
