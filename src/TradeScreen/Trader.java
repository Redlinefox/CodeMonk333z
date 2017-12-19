package TradeScreen;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.net.ServerSocketFactory;

import OrderManager.Order;
import TradeScreen.TradeScreen;

public class Trader extends Thread implements TradeScreen{
	// maps an order id to an Order object
	private Map<Integer,Order> orders=new HashMap<Integer,Order>();
	// Socket used to connect clients and traders
	private static Socket omConn;
	// port where information will be accepted
	private int port;
	// Determines when Trader thread stops running
	private boolean runner = true;

	/**
	 * Initialize Trader class
	 * @param name
	 * @param port
	 */
	public Trader(String name,int port){
		this.setName(name);
		this.port=port;
	}

	public static Socket getOmConn() {
		return omConn;
	}

	public void setOmConn(int port) throws IOException {
		omConn = ServerSocketFactory.getDefault().createServerSocket(port).accept();
	}

	public boolean isRunner() {
		return runner;
	}

	public void setRunner(boolean runner) {
		this.runner = runner;
	}

	public Map<Integer, Order> getOrders() {
		return orders;
	}

	public void setOrders(Map<Integer, Order> orders) {
		this.orders = orders;
	}
	
	ObjectInputStream  is;
	ObjectOutputStream os;
	public void run(){
		//OM will connect to us
		try {
			// set OmConn to server factory socket in specified port
			setOmConn(port);
			
			//is=new ObjectInputStream( omConn.getInputStream());
			InputStream s=omConn.getInputStream(); //if i try to create an objectinputstream before we have data it will block
			// Created by EZ
			OutputStream so=omConn.getOutputStream();
			// run until told not to
			while(isRunner()){
				// check if Input Streams available
				if(0<s.available()){
					// Create ObjectInputStream
					is=new ObjectInputStream(s);  //TODO check if we need to create each time. this will block if no data, but maybe we can still try to create it once instead of repeatedly
					// Create ObjectOutputStream
					os=new ObjectOutputStream(so);
					// Read object and make sure it matches either {newOrder, price, fill, cross}
					api method=(api)is.readObject();
					// Print out what is going on in Trader
					System.out.println(Thread.currentThread().getName()+" calling: "+method);
					// Call respective method depending on given input stream enum
					switch(method) {
						case newOrder:newOrder(is.readLong(),(Order)is.readObject());break;
						case price:price(is.readInt(),(Order)is.readObject());break;
						// These two not completed
						case cross:is.readInt();is.readObject();break; //TODO
						case fill:is.readInt();is.readObject();break; //TODO
					}
				}else{
					//System.out.println("TradeScreen.Trader Waiting for data to be available - sleep 1s");
					Thread.sleep(1000);
				}
			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void newOrder(int id,Order order) throws IOException, InterruptedException {
		//TODO the order should go in a visual grid, but not needed for test purposes
		Thread.sleep(2134);
		// put order into List of orders
		orders.put(id, order);
		
		acceptOrder(id);
	}

	/**
	 * Method called by newOrder
	 * @param id
	 * @throws IOException
	 */
	public void acceptOrder(int id) throws IOException {
		// ObjectOutputStream writes to specified output stream, parameter.
		os = new ObjectOutputStream(omConn.getOutputStream());
		// writes "acceptOrder" to output stream os
		os.writeObject("acceptOrder");
		// writes id to output stream os
		os.writeInt(id);
		// flushes everything written to the outputstream, flushing the buffer holding data to be sent
		os.flush();
	}

	@Override
	public void newOrder(long id, Order order) throws IOException, InterruptedException {
		newOrder((int) id, order);
	}

	@Override
	public void acceptOrder(long id) throws IOException {
		acceptOrder((int) id);
	}

	/**
	 * 
	 * @param id
	 * @param sliceSize
	 * @throws IOException
	 */
	@Override
	public void sliceOrder(long id, long sliceSize) throws IOException {
		// ObjectOutputStream writes to specified output stream, parameter.
		os=new ObjectOutputStream(omConn.getOutputStream());
		// writes "sliceOrder" to output stream os
		os.writeObject("sliceOrder");
		// writes id to output stream os
		os.writeLong(id);
		// writes sliceSize to output stream os
		os.writeLong(sliceSize);
		// flushes everything written to the outputstream, flushing the buffer holding data to be sent
		os.flush();
	}

	/**
	 * Links the order with specified id using sliceOrder
	 * @param id
	 * @param o
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Override
	public void price(long id, Order o) throws InterruptedException, IOException {
		//TODO should update the trade screen
		Thread.sleep(2134);
		// delegates work to sliceOrder
		sliceOrder(id,orders.get(id).sizeRemaining()/2);
	}
}
