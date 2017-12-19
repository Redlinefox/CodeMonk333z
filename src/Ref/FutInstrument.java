package Ref;

import java.util.Date;

class FutInstrument extends Instrument{
	private Date expiry;
	private Instrument underlier;

	public FutInstrument(Ric ric){
		super(ric);
	}
}
