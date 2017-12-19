package Ref;

import java.io.Serializable;
import java.util.Date;

public class Instrument implements Serializable{
	private long id;
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

/*TODO
Index
bond
methods
*/