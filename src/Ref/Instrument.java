package Ref;

import java.io.Serializable;
import java.util.Date;

public class Instrument implements Serializable{
	long id;
	private String name;
	private Ric ric;
	private String isin;
	private String sedol;
	private String bbid;
	public Instrument(Ric ric){
		this.ric=ric;
	}
	public String toString(){
		return ric.getRic();
	}
}
class EqInstrument extends Instrument{
	Date exDividend;

	public EqInstrument(Ric ric){
		super(ric);
	}
}
class FutInstrument extends Instrument{
	Date expiry;
	Instrument underlier;

	public FutInstrument(Ric ric){
		super(ric);
	}
}
/*TODO
Index
bond
methods
*/