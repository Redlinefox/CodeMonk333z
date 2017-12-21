package OrderClient;

import java.io.Serializable;

import Ref.Instrument;

public class NewOrderSingle implements Serializable{
	private int size;
	private float price;
	private Instrument instrument;
	private String MsgType;

	
	public NewOrderSingle(int size, float price, Instrument instrument){
		this.size=size;
		this.price=price;
		this.instrument=instrument;
	}
	
	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	
	public int getSize() {
		return size;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
}