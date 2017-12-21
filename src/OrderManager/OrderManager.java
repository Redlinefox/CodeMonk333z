package OrderManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Map;

import Database.Database;
import LiveMarketData.LiveMarketData;
import MockClient.Mock;
import OrderClient.NewOrderSingle;
import OrderRouter.Router;
import OrderRouter.Router.api;
import TradeScreen.TradeScreen;
import org.apache.log4j.Logger;

public class OrderManager {
	private static LiveMarketData liveMarketData;
	private Map<Integer, Order> orders = new HashMap<>();
	private int id;
	private Socket[] orderRouters;
	private Socket[] clients;
	private Socket trader;
	private int clientId;
	private int routerId;
	private Socket client;
	private Socket router;
	boolean condition = true;
	private Logger log = Logger.getLogger(OrderManager.class.getName());

	//@param args the command line arguments
	public OrderManager(InetSocketAddress[] orderRouters, InetSocketAddress[] clients,InetSocketAddress trader,
						LiveMarketData liveMarketData)throws IOException, ClassNotFoundException, InterruptedException{

		//talks to live market data and trader right here
		this.liveMarketData = liveMarketData;
		this.trader=connect(trader);
		//for the router connections, copy the input array into our object field.
		//but rather than taking the address we create a socket+ephemeral port and connect it to the address
		this.orderRouters=new Socket[orderRouters.length];
		int i=0; //need a counter for the the output array
		for(InetSocketAddress location:orderRouters){
			this.orderRouters[i]=connect(location);
			i++;
		}

		//repeat for the client connections
		this.clients=new Socket[clients.length];
		i=0;
		for(InetSocketAddress location:clients){
			this.clients[i]=connect(location);
			i++;
		}


		//main loop, wait for a message, then process it
		//
		while(condition){
			//we want to use the arrayindex as the clientId, so use traditional for loop instead of foreach
			for(clientId=0; clientId <this.clients.length ; clientId++){ //check if we have data on any of the sockets
				client=this.clients[clientId];
				if(0<client.getInputStream().available()){ //if we have part of a message ready to read, assuming this doesn't fragment messages
					ObjectInputStream is=new ObjectInputStream(client.getInputStream()); //create an object inputstream, this is a pretty stupid way of doing it, why not create it once rather than every time around the loop
					String method=(String)is.readObject();
					log.info(Thread.currentThread().getName()+" calling "+method);
					switch(method){ //determine the type of message and process it
						//call the newOrder message with the clientId and the message (clientMessageId,NewOrderSingle)
						case "newOrderSingle": 
							newOrder(clientId, is.readInt(), (NewOrderSingle)is.readObject());
							break;
						case "fill":
							newFill(is.readLong(), is.readLong(), is.readLong(), is.readDouble());
							break;
						case "cancel":
							cancelOrder(is.readLong(), is.readLong());
							break;
						//TODO create a default case which errors with "Unknown message type"+...
						default:
							log.error("Unknown message type for " + method);
							break;
					}
				}
			}

			//top half is getting information to print out
			//bottom half is just initializing different classes based on switch statement
			for(routerId=0; routerId < this.orderRouters.length ; routerId++){ //check if we have data on any of the sockets
				router=this.orderRouters[routerId];
				if(0<router.getInputStream().available()){ //if we have part of a message ready to read, assuming this doesn't fragment messages
					ObjectInputStream is=new ObjectInputStream(router.getInputStream()); //create an object inputstream, this is a pretty stupid way of doing it, why not create it once rather than every time around the loop
					String method=(String)is.readObject();
					log.info(Thread.currentThread().getName()+" calling "+method);
					switch(method){ //determine the type of message and process it
						case "bestPrice":
							int OrderId=is.readInt();
							int SliceId=is.readInt();
							Order slice=orders.get(OrderId).getSlices().get(SliceId);
							slice.getBestPrices()[routerId]=is.readDouble();
							slice.setBestPriceCount(slice.getBestPriceCount()+1);
							if(slice.getBestPriceCount()==slice.getBestPrices().length)
								reallyRouteOrder(SliceId, slice);
							break;
						case "newFill":
							newFill(is.readInt(),is.readInt(),is.readInt(),is.readDouble());
							break;
						default:
							log.error("Unknown message type for " + method);
							break;
					}
				}
			}

			//prints out information from the input stream
			//calls either acceptOrder or sliceOrder depending on switch
			if(0<this.trader.getInputStream().available()){
				ObjectInputStream is=new ObjectInputStream(this.trader.getInputStream());
				String method=(String)is.readObject();
				log.info(Thread.currentThread().getName()+" calling "+method);
				switch(method){
					case "acceptOrder":
						acceptOrder(is.readInt());
						break;
					case "sliceOrder":
						sliceOrder(is.readInt(), is.readInt());
				}
			}
		}
	}

	//makes a socket connection
	private Socket connect(InetSocketAddress location) throws InterruptedException{
		boolean connected=false;
		int tryCounter=0;
		while(!connected&&tryCounter<600){
			try{
				Socket s=new Socket(location.getAddress(),location.getPort());
				s.setKeepAlive(true);
				return s;
			}catch (IOException e) {
				Thread.sleep(1000);
				tryCounter++;
			}
		}
		log.info("Failed to connect to "+location.toString());
		return null;
	}
	
	//orders is a hashmap, object is being assigned info
	//regular code for a object stream, write/flush etc
	private void newOrder(int clientId, int clientOrderId, NewOrderSingle nos) throws IOException{
		orders.put(id, new Order(clientId, clientOrderId, nos.getInstrument(), nos.getSize()));
		orders.get(id).setOrdStatus('A');
		//send a message to the client with 39=A; //OrdStatus is Fix 39 (TAG), 'A' is 'Pending New' (VALUE)
		ObjectOutputStream os=new ObjectOutputStream(clients[clientId].getOutputStream());
		os.writeObject(orders.get(id));
		os.flush();
		String orderS = "";
		for(Map.Entry<Integer, Order> ord: orders.entrySet()) {
			orderS += "\n\t\t" + ord.getValue().toString();
		}
		log.info("Order Manager's order list (post new order): " + orderS);
		sendOrderToTrader(id,orders.get(id),TradeScreen.api.newOrder);		//send the new order to the trading screen
		id++;
	}
	
	//regular code for a object stream, write/flush etc
	private void sendOrderToTrader(long id,Order o,Object method) throws IOException{
		ObjectOutputStream ost=new ObjectOutputStream(trader.getOutputStream());
		ost.writeObject(method);
		ost.writeLong(id);
		ost.writeObject(o);
		ost.flush();
	}

	private void newFill(long id,long sliceId,long size,double price) throws IOException{
		Order o;
		try {
			o = orders.get((int) id);
			try {
				o = o.getSlices().get((int) sliceId);
			} catch (IndexOutOfBoundsException | NullPointerException e) {
				log.info("Slice " + sliceId + " does not exist in list of orders");
			} finally {
				o.createFill(size, price);
				o.setOrdStatus('P');
				if(o.sizeRemaining()==0){
					o.setOrdStatus('F');
				}
				ObjectOutputStream os=new ObjectOutputStream(clients[clientId].getOutputStream());
				os.writeObject(o);
				os.flush();

				String orderS = "";
				for(Map.Entry<Integer, Order> ord: orders.entrySet()) {
					orderS += "\n\t\t" + ord.getValue().toString();
				}
				log.info("Order Manager's order list (post fill): " + orderS);
				sendFillToTrader(id, sliceId, size, price, TradeScreen.api.fill);
			}
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.info("Order id " + id + " does not exist.");
		}
	}

	private void sendFillToTrader(long id, long sliceId, long size, double price, Object method) throws IOException {
		ObjectOutputStream ost=new ObjectOutputStream(trader.getOutputStream());
		ost.writeObject(method);
		ost.writeLong(id);
		ost.writeLong(sliceId);
		ost.writeLong(size);
		ost.writeDouble(price);
		ost.flush();
	}

	private void cancelOrder(long id,long sliceId) throws IOException{
		Order o;
		try {
			o = orders.get((int) id);
			try {
				o = o.getSlices().get((int) sliceId);
			} catch (IndexOutOfBoundsException | NullPointerException e) {
				log.info("Slice " + sliceId + " does not exist in list of orders");
			} finally {
				orders.remove((int) o.getId());
				o.setOrdStatus('C');
				ObjectOutputStream os=new ObjectOutputStream(clients[clientId].getOutputStream());
				os.writeObject(o);
				os.flush();
				
				String orderS = "";
				for(Map.Entry<Integer, Order> ord: orders.entrySet()) {
					orderS += "\n\t\t" + ord.getValue().toString();
				}
				log.info("Order Manager's order list (post cancelling): " + orderS);
				sendCancelToTrader(id, sliceId, TradeScreen.api.cancel);
			}
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.info("Order id " + id + " does not exist.");
		} 
	}

	private void sendCancelToTrader(long id, long sliceId, Object method) throws IOException {
		ObjectOutputStream ost=new ObjectOutputStream(trader.getOutputStream());
		ost.writeObject(method);
		ost.writeLong(id);
		ost.writeLong(sliceId);
		ost.flush();
	}
	
	//sends out info about an order with output stream
	public void acceptOrder(int id) throws IOException{
		Order o=orders.get(id);
		if(o.getOrdStatus()!='A'){ //Pending New
			log.info("error accepting order that has already been accepted");
			return;
		}
		o.setOrdStatus('0'); //New
		ObjectOutputStream os=new ObjectOutputStream(clients[Math.toIntExact(o.getClientid())].getOutputStream());
		os.writeObject("11="+o.getClientOrderID()+";35=A;39=0");
		os.flush();

		price(id,o);
	}

	public void sliceOrder(int id,int sliceSize) throws IOException{
		Order o=orders.get(id);
		//slice the order. We have to check this is a valid size.
		//Order has a list of slices, and a list of fills, each slice is a child order and each fill is associated with either a child order or the original order
		if(sliceSize>o.sizeRemaining()-o.sliceSizes()){
			log.info("error sliceSize is bigger than remaining size to be filled on the order");
			return;
		}
		long sliceId=o.newSlice(sliceSize);
		Order slice=o.getSlices().get((int) sliceId);
		internalCross(id,slice);
		int sizeRemaining=(int)o.sizeRemaining();
		if(sizeRemaining>0){
			routeOrder(id,sliceId,sizeRemaining,slice);
		}
	}

	private void internalCross(int id, Order o) throws IOException{
		for(Map.Entry<Integer, Order> entry:orders.entrySet()){
			if(entry.getKey().intValue()==id)
				continue;
			Order matchingOrder=entry.getValue();
			if(!(matchingOrder.getInstrument().equals(o.getInstrument())&&
					matchingOrder.getInitialMarketPrice()==o.getInitialMarketPrice()))
				continue;
			//TODO add support here and in Order for limit orders
			long sizeBefore=o.sizeRemaining();
			o.cross(matchingOrder);
			if(sizeBefore!=o.sizeRemaining()){
				sendOrderToTrader(id, o, TradeScreen.api.cross);
			}
		}
	}
	
	private void routeOrder(long id,long sliceId,long size,Order order) throws IOException{
		for(Socket r:orderRouters){
			ObjectOutputStream os=new ObjectOutputStream(r.getOutputStream());
			os.writeObject(Router.api.priceAtSize);
			os.writeLong(id);
			os.writeLong(sliceId);
			os.writeObject(order.getInstrument());
			os.writeLong(order.sizeRemaining());
			os.flush();
		}
		//need to wait for these prices to come back before routing
		order.setBestPrices(new double[orderRouters.length]);
		order.setBestPriceCount(0);
	}

	private void reallyRouteOrder(long sliceId,Order o) throws IOException{
		int minIndex=0;
		double min=o.getBestPrices()[0];
		for(int i=1;i<o.getBestPrices().length;i++){
			if(min>o.getBestPrices()[i]){
				minIndex=i;
				min=o.getBestPrices()[i];
			}
		}
		ObjectOutputStream os=new ObjectOutputStream(orderRouters[minIndex].getOutputStream());
		os.writeObject(Router.api.routeOrder);
		os.writeLong(o.getId());
		os.writeLong(sliceId);
		os.writeLong(o.sizeRemaining());
		os.writeObject(o.getInstrument());
		os.flush();
	}

	private void sendCancel(Order order,Router orderRouter){
		//orderRouter.sendCancel(order);
		//order.orderRouter.writeObject(order);
	}

	private void price(int id,Order o) throws IOException{
		liveMarketData.setPrice(o);
		sendOrderToTrader(id, o, TradeScreen.api.price);
	}
}