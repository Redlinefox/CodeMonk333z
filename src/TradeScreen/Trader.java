package TradeScreen;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ServerSocketFactory;

import OrderManager.Fill;
import OrderManager.Order;
import org.apache.log4j.Logger;

public class Trader extends Thread implements TradeScreen {
    // Socket used to connect clients and traders
    private Socket omConn;
    ObjectInputStream is;
    ObjectOutputStream os;
    // maps an order id to an Order object
    private Map<Integer, Order> orders = new HashMap<Integer, Order>();
    // port where information will be accepted
    private int port;
    // Determines when Trader thread stops running
    private boolean runner = true;
    private Logger log = Logger.getLogger(Trader.class.getName());

	private ArrayList<Order> slices;
	private ArrayList<Fill>fills;

    /**
     * Initialize Trader class
     *
     * @param name
     * @param port
     */
    public Trader(String name, int port) {
        this.setName(name);
        this.port = port;
    }

    public Socket getOmConn() {
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

	public void run(){
		//OM will connect to us
		try {
			// set OmConn to server factory socket in specified port
			setOmConn(port);
			
			//is=new ObjectInputStream( omConn.getInputStream());
			InputStream s=omConn.getInputStream(); //if i try to create an objectinputstream before we have data it will block
			// run until told not to
			while(isRunner()){
				// check if Input Streams available
				if(0<s.available()){
					// Create ObjectInputStream
					is=new ObjectInputStream(s);  //TODO check if we need to create each time. this will block if no data, but maybe we can still try to create it once instead of repeatedly
					// Create ObjectOutputStream
					//os=new ObjectOutputStream(so);
					// Read object and make sure it matches either {newOrder, price, fill, cross}
					api method=(api)is.readObject();
					// Print out what is going on in Trader
					log.info(Thread.currentThread().getName()+" calling: "+method);
					// Call respective method depending on given input stream enum
					switch(method) {
						case newOrder:
						    newOrder(is.readLong(),(Order)is.readObject());
							String orderSNO = "";
							for(Map.Entry<Integer, Order> ord: orders.entrySet()) {
								orderSNO += "\n\t\t" + ord.getValue().toString();
							}
							log.info("Trader's order list (post newOrder): " + orderSNO);
						    break;
						case price:
							try{
								price(is.readLong(),(Order)is.readObject());
								} catch (OptionalDataException e)
								{
									log.error("Error description",e);
								}
								break;
						case cross:
						    is.readInt();is.readObject();
						    break; //TODO
						case fill:
							fill(is.readInt(), is.readLong(), is.readLong(), is.readDouble()); 	//is.readInt();is.readObject();
							String orderSF = "";
							for(Map.Entry<Integer, Order> ord: orders.entrySet()) {
								orderSF += "\n\t\t" + ord.getValue().toString();
							}
							log.info("Trader's order list (post fill): " + orderSF);
							break;
						case cancel:
							cancelOrder(is.readInt(), is.readInt()); 	//is.readInt();is.readObject();
							String orderSC = "";
							for(Map.Entry<Integer, Order> ord: orders.entrySet()) {
								orderSC += "\n\t\t" + ord.getValue().toString();
							}
							log.info("Trader's order list (post cancel): " + orderSC);
							break;
					}
				}
			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			log.error("Error description",e);
		}
	}


	public void fill(long id, long sliceId, long size, double price) {
		Order order;
		try {
			order = orders.get((int) id);
			try {
				order = order.getSlices().get((int) sliceId);
			} catch (IndexOutOfBoundsException | NullPointerException e) {
				log.info("Slice " + sliceId + " does not exist in list of orders");
			} finally {
				order.createFill(size, price);
			}
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.info("Order id " + id + " does not exist.");
		}
	}


	public void cancelOrder(long id, long sliceId) {
		Order order;
    	try {
			order = orders.get((int) id);
			try {
				order = order.getSlices().get((int) sliceId);
			} catch (IndexOutOfBoundsException | NullPointerException e) {
				log.info("Slice " + sliceId + " does not exist in list of orders");
			} finally {
				orders.remove((int) order.getId());
			}
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.info("Order id " + id + " does not exist.");
		}
	}

	public void newOrder(int id,Order order) throws IOException, InterruptedException {
		// put order into List of orders
		orders.put(id, order);
	}

    /**
     * Method called by newOrder
     *
     * @param id
     * @throws IOException
     */
    public void acceptOrder(int id) throws IOException {
        // Guard to catch null pointer for omConn
        if (omConn == null) {
            return;
        }
        // ObjectOutputStream writes to specified output stream, parameter.
        os = new ObjectOutputStream(omConn.getOutputStream());
        // writes "acceptOrder" to output stream os
        os.writeObject("acceptOrder");
        // writes id to output stream os
        os.writeInt(id);
        // flushes everything written to the output stream, flushing the buffer holding data to be sent
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
     * @param id
     * @param sliceSize
     * @throws IOException
     */
    @Override
    public void sliceOrder(long id, long sliceSize) throws IOException {
        // ObjectOutputStream writes to specified output stream, parameter.
        os = new ObjectOutputStream(omConn.getOutputStream());
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
		// delegates work to sliceOrder
		try {
			if(orders.get(id) != null) {
				sliceOrder(id, orders.get(id).sizeRemaining() / 2);
			}
		} catch (NullPointerException e)
		{
			log.error("Error description",e);
		}
	}


}
