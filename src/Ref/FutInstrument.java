package Ref;

import java.io.Serializable;
import java.util.Date;

class FutInstrument extends Instrument implements Serializable{
	private Date expiry;
	private Instrument underlier;
	private long id;
	private String name;
	private Ric ric;
	private String isin;
	private String sedol;
	private String bbid;

	public FutInstrument(long id){
		super(id);
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public Instrument getUnderlier() {
		return underlier;
	}

	public void setUnderlier(Instrument underlier) {
		this.underlier = underlier;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Ric getRic() {
		return ric;
	}

	@Override
	public void setRic(Ric ric) {
		this.ric = ric;
	}

	@Override
	public String getIsin() {
		return isin;
	}

	@Override
	public void setIsin(String isin) {
		this.isin = isin;
	}

	@Override
	public String getSedol() {
		return sedol;
	}

	@Override
	public void setSedol(String sedol) {
		this.sedol = sedol;
	}

	@Override
	public String getBbid() {
		return bbid;
	}

	@Override
	public void setBbid(String bbid) {
		this.bbid = bbid;
	}
}
