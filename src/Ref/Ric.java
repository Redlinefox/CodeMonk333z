package Ref;

import java.io.Serializable;

public class Ric implements Serializable{
	public String ric;

	/**
	 * Creates a ric object and specifies string associated with ric
	 * @param ric
	 */
	public Ric(String ric){
		this.ric=ric;
	}

	/**
	 * Gets ric
	 * @return
	 */
	public String getRic() {
		return ric;
	}

	/**
	 * Sets ric
	 * @param ric
	 */
	public void setRic(String ric) {
		this.ric = ric;
	}

	/**
	 * Gets ex, 2nd element of string
	 * @return
	 */
	public String getEx(){
		return ric.split("\\.")[1];
	}

	/**
	 * Gets company, 1st element of string
	 * @return
	 */
	public String getCompany(){
		return ric.split("\\.")[0];
	}
}