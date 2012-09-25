package pnnl.cyclist.model.vo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Transaction {
	private IntegerProperty _time;
	private IntegerProperty _source;
	private IntegerProperty _dest;
	
	public Transaction(int time, int sender, int receiver) {
		_time = new SimpleIntegerProperty(time);
		_source = new SimpleIntegerProperty(sender);
		_dest = new SimpleIntegerProperty(receiver);
	}
	
	public int getTime() {
		return _time.get();
	}
	
	public int getSource() {
		return _source.get();
	}
	
	public int getDest() {
		return _dest.get();
	}
}
