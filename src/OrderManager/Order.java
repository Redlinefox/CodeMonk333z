package OrderManager;

import java.io.Serializable;
import java.util.ArrayList;

import Ref.EqInstrument;
import Ref.Instrument;

public class Order implements Serializable{

	private long id;
	private long size;
	private long bestPriceCount;
	private long clientid;
	private long price;
	private short orderRouter;
	private long clientOrderID;
	private double[]bestPrices;
	private Instrument instrument;
	private double initialMarketPrice;
	private char OrdStatus='A'; //OrdStatus is Fix 39, 'A' is 'Pending New'
	private ArrayList<Order>slices;
	private ArrayList<Fill>fills;

	public Order(long clientId, long ClientOrderID, Instrument instrument, long size){
		this.clientOrderID=ClientOrderID;
		this.size=size;
		this.clientid=clientId;
		this.instrument=instrument;
		fills=new ArrayList<Fill>();
		slices=new ArrayList<Order>();
	}

    /**
     * Go through each order inside slices array and return size of all of these orders
     * @return
     */
	public long sliceSizes(){
		int totalSizeOfSlices=0;
		for(Order c:slices)
			totalSizeOfSlices+=c.size;
		return totalSizeOfSlices;
	}

    /**
     * Adds a new slice into array list of slices
     * @param sliceSize
     * @return index of newly added slice
     */
	public long newSlice(long sliceSize){
		slices.add(new Order(id,clientOrderID,instrument,sliceSize));
		return slices.size()-1;
	}

    /**
     * Gets size of each fill for every order in slices list
     * Essentially returns how filled the order is
     * @return
     */
	public int sizeFilled(){
		int filledSoFar=0;
		for(Fill f:fills){
			filledSoFar+=f.getSize();		//changed to getSize because it makes more sense, need size for filling
		}
		for(Order c:slices){
			filledSoFar+=c.sizeFilled();
		}
		return filledSoFar;
	}

    /**
     * Returns how much size you have left that is not filled
     * @return
     */
	public long sizeRemaining(){
		return size-sizeFilled();
	}

    /**
     * Returns the average price of fill for every order
     * @return
     */
	private float price(){
		//TODO this is buggy as it doesn't take account of slices. Let them fix it
        //^Potentially Fixed by Ezra and Denesh
		float sum=0;
		for(Fill fill:fills){
			sum+=fill.getPrice();
		}
        for(Order c:slices){
            sum+=c.price();
        }
		return sum/fills.size();
	}

    /**
     * Adds a new fill into the list of fills for an order
     * Changes OrdStatus based on how filled the order is
     * 1: partially filled / 2: completely filled
     * @param size
     * @param price
     */
	public void createFill(long size,double price){
		fills.add(new Fill(size,price));
		if(sizeRemaining()==0){
			OrdStatus='2';
		}else{
			OrdStatus='1';
		}
	}

	public void cross(Order matchingOrder) {
        for(Order slice:slices) {
            for (Order matchingSlice : matchingOrder.slices) {
                // Records how filled the matchingSlice is
                long msze = matchingSlice.sizeRemaining();
                long sze = slice.sizeRemaining();
                if(sze<=msze) {
                    slice.createFill(sze,initialMarketPrice);
                    matchingSlice.createFill(sze, initialMarketPrice);
                } else {
                    slice.createFill(msze,initialMarketPrice);
                    matchingSlice.createFill(msze, initialMarketPrice);
                }
            }
            // Records how many unfilled sizes you have
            long sze=slice.sizeRemaining();
            // Stores how unfilled matchingOrder is
            long mParent=matchingOrder.sliceSizes()-matchingOrder.sizeRemaining();

            if(sze>0 && mParent>0){
                if(sze>=mParent){
                    slice.createFill(sze,initialMarketPrice);
                    matchingOrder.createFill(sze, initialMarketPrice);
                }else{
                    slice.createFill(mParent,initialMarketPrice);
                    matchingOrder.createFill(mParent, initialMarketPrice);
                }
            }
            //no point continuing if we didn't fill this slice, as we must already have fully filled the matchingOrder
            if(slice.sizeRemaining()>0)break;

        }
    }

    //Getters and Setters
    public long getId() {
        return id;
    }

	public void setId(long id) {
		this.id = id;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getBestPriceCount() {
		return bestPriceCount;
	}

	public void setBestPriceCount(long bestPriceCount) {
		this.bestPriceCount = bestPriceCount;
	}

	public long getClientid() {
		return clientid;
	}

	public void setClientid(long clientid) {
		this.clientid = clientid;
	}

	public short getOrderRouter() {
		return orderRouter;
	}

	public void setOrderRouter(short orderRouter) {
		this.orderRouter = orderRouter;
	}

	public long getClientOrderID() {
		return clientOrderID;
	}

	public void setClientOrderID(long clientOrderID) {
		this.clientOrderID = clientOrderID;
	}

	public double[] getBestPrices() {
		return bestPrices;
	}

	public void setBestPrices(double[] bestPrices) {
		this.bestPrices = bestPrices;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public double getInitialMarketPrice() {
		return initialMarketPrice;
	}

	public void setInitialMarketPrice(double initialMarketPrice) {
		this.initialMarketPrice = initialMarketPrice;
	}

	public ArrayList<Order> getSlices() {
		return slices;
	}

	public void setSlices(ArrayList<Order> slices) {
		this.slices = slices;
	}

	public ArrayList<Fill> getFills() {
		return fills;
	}

	public char getOrdStatus() {
		return OrdStatus;
	}

	public void setOrdStatus(char ordStatus) {
		OrdStatus = ordStatus;
	}
}

