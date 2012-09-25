package pnnl.cyclist.model.vo;

public class Population {

	private final int _year;
	private final int _pop;
	
	public Population(int year, int pop) {
		this._year = year;
		this._pop = pop;
	}
		
	public int getYear() { return _year; }
	
	public int getPopulation() { return _pop; }
}
