package OrderManager;

import java.io.Serializable;
import java.util.ArrayList;

import Ref.Instrument;

public class Order implements Serializable{

	private long id;
	private long size;
	private long bestPriceCount;
	private long clientid;
	private short orderRouter;
	private long clientOrderID;
	private double[]bestPrices;
	private Instrument instrument;
	private double initialMarketPrice;
	
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
	
	public long sliceSizes(){
		int totalSizeOfSlices=0;
		for(Order c:slices)totalSizeOfSlices+=c.size;
		return totalSizeOfSlices;
	}
	
	public long newSlice(long sliceSize){
		slices.add(new Order(id,clientOrderID,instrument,sliceSize));
		return slices.size()-1;
	}
	
	public int sizeFilled(){
		int filledSoFar=0;
		for(Fill f:fills){
			filledSoFar+=f.getPrice();
		}
		for(Order c:slices){
			filledSoFar+=c.sizeFilled();
		}
		return filledSoFar;
	}
	
	public long sizeRemaining(){
		return size-sizeFilled();
	}
	
	private char OrdStatus='A'; //OrdStatus is Fix 39, 'A' is 'Pending New'
	//Status state;
	private float price(){
		//TODO this is buggy as it doesn't take account of slices. Let them fix it
		float sum=0;
		for(Fill fill:fills){
			sum+=fill.getPrice();
		}
		return sum/fills.size();
	}
	
	public void createFill(long size,double price){
		fills.add(new Fill(size,price));
		if(sizeRemaining()==0){
			OrdStatus='2';
		}else{
			OrdStatus='1';
		}
	}
	
	public void cross(Order matchingOrder){
		//pair slices first and then parent
		for(Order slice:slices){
			if(slice.sizeRemaining()==0)continue;
			//TODO could optimise this to not start at the beginning every time
			for(Order matchingSlice:matchingOrder.slices){
				long msze=matchingSlice.sizeRemaining();
				if(msze==0)continue;
				long sze=slice.sizeRemaining();
				if(sze<=msze){
					 slice.createFill(sze,initialMarketPrice);
					 matchingSlice.createFill(sze, initialMarketPrice);
					 break;
				}
				//sze>msze
				slice.createFill(msze,initialMarketPrice);
				matchingSlice.createFill(msze, initialMarketPrice);
			}
			long sze=slice.sizeRemaining();
			long mParent=matchingOrder.sizeRemaining()-matchingOrder.sliceSizes();
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
		if(sizeRemaining()>0){
			for(Order matchingSlice:matchingOrder.slices){
				long msze=matchingSlice.sizeRemaining();
				if(msze==0)continue;
				long sze=sizeRemaining();
				if(sze<=msze){
					 createFill(sze,initialMarketPrice);
					 matchingSlice.createFill(sze, initialMarketPrice);
					 break;
				}
				//sze>msze
				createFill(msze,initialMarketPrice);
				matchingSlice.createFill(msze, initialMarketPrice);
			}
			long sze=sizeRemaining();
			long mParent=matchingOrder.sizeRemaining()-matchingOrder.sliceSizes();
			if(sze>0 && mParent>0){
				if(sze>=mParent){
					createFill(sze,initialMarketPrice);
					matchingOrder.createFill(sze, initialMarketPrice);
				}else{
					createFill(mParent,initialMarketPrice);
					matchingOrder.createFill(mParent, initialMarketPrice);					
				}
			}
		}
	}
	
	private void cancel(){
		//state=cancelled
	}

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

	public void setFills(ArrayList<Fill> fills) {
		this.fills = fills;
	}

	public char getOrdStatus() {
		return OrdStatus;
	}

	public void setOrdStatus(char ordStatus) {
		OrdStatus = ordStatus;
	}
}

