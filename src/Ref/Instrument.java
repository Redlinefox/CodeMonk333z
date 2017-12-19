package Ref;

/**
 * Holds data for Instrument objects, received from database
 * EqInstrument: Equity Instrument (what all current instruments are)
 * FutInstrument: Future Instrument (should be implemented but no current instruments are as such
 */
public abstract class  Instrument {
	// All columns of Instrument table storing data on an Instrument
	private long id;
	private String name;
	private Ric ric;
	private String isin;
	private String sedol;
	private String bbid;
	
	public Instrument(long id) {
		this.id = id;
	}
	
	public Instrument() {
		
	}

	public abstract Ric getRic();

	public abstract long getId();

	public abstract void setId(long id);

	public abstract String getName();

	public abstract void setName(String name);

	public abstract void setRic(Ric ric);

	public abstract String getIsin();

	public abstract void setIsin(String isin);

	public abstract String getSedol();

	public abstract void setSedol(String sedol);

	public abstract String getBbid();

	public abstract void setBbid(String bbid);
}
