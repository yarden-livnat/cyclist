package pnnl.cyclist.model.vo;

public enum Element {

	Ac ( 89, "Ac"),
	Th ( 90, "Th"),
	Pa ( 91, "Pa"),
	U  ( 92, "U"),
	Np ( 93, "Np"),
	Pu ( 94, "Pu"),
	Am ( 95, "Am"),
	Cm ( 96, "Cm"), 
	Bk ( 97, "Bk"), 
	Cf ( 98, "Cf"), 
	Es ( 99, "Es"),
	Fm (100, "Fm"),
	Md (101, "Md"),
	No (102, "No"),
	Lr (103, "Lr");
	
	private final int	_atomicNumber;	
	private final String _symbol;
	
	private static final Element[] asArray = {
		Ac, Th, Pa, U, Np, Pu, Am, Cm, Bk, Cf, Es, Fm, Md, No, Lr
	};
	
	Element(int num, String sym) {
		_atomicNumber = num;
		_symbol = sym;
	}
	
	public int number() { return _atomicNumber; }
	
	public String symbol() { return _symbol; }
	
	static public Element valueOf(int v) {
		if (v < Ac._atomicNumber || v > Lr._atomicNumber) throw new ArrayIndexOutOfBoundsException(v);
		
		return asArray[v-Ac._atomicNumber];
	}
}
