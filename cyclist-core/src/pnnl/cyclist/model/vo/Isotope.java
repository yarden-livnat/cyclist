package cyclist.model.vo;

public class Isotope {

	private Element _elem;
	private int _atomicNumber;
	
	public Isotope(Element elem, int num) {
		_elem = elem;
		_atomicNumber = num;
	}
	
	public Isotope(int code) {
		_elem = Element.valueOf(code/10000);
		_atomicNumber = code/10 % 1000;
	}
	
	public int getCode() {
		return _elem.number()*10000+_atomicNumber*10;
	}
	
	public String toString() {
		return _elem.toString()+_atomicNumber;
	}
 }
