package OrderClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import MockClient.Mock;
import OrderManager.Order;
import Ref.EqInstrument;
import Ref.Instrument;
import Ref.Ric;
import org.apache.log4j.Logger;

public class SampleClient extends MockClient.Mock implements Client{
	private static final Random RANDOM_NUM_GENERATOR=new Random();
	private static final Instrument[] INSTRUMENTS={new EqInstrument(17l), new EqInstrument(4l), new EqInstrument(7l)};
	private static final HashMap OUT_QUEUE=new HashMap(); //queue for outgoing orders
	private int id=0; //message id number
	private Socket omConn; //connection to order manager
	private Logger log = Logger.getLogger(SampleClient.class.getName());

	public boolean isRunner() {
		return runner;
	}

	public void setRunner(boolean runner) {
		this.runner = runner;
	}

	private boolean runner = true;
	
	public SampleClient(int port) throws IOException{
		//OM will connect to us
		omConn=new ServerSocket(port).accept();
		log.info("OM connected to client port "+port);
	}

    public static HashMap getOutQueue() {
        return OUT_QUEUE;
    }

    @Override
    public int sendOrder() throws IOException {
        int size = RANDOM_NUM_GENERATOR.nextInt(5000);
        int instid = RANDOM_NUM_GENERATOR.nextInt(3);
        Instrument instrument = INSTRUMENTS[RANDOM_NUM_GENERATOR.nextInt(INSTRUMENTS.length)];
        NewOrderSingle nos = new NewOrderSingle(size, instid, instrument);

		MockClient.Mock.show("sendOrder: id="+id+" size="+size+" instrument="+INSTRUMENTS[instid].toString());
		OUT_QUEUE.put(id,nos);
		if(omConn.isConnected()){
			ObjectOutputStream os=new ObjectOutputStream(omConn.getOutputStream());
			os.writeObject("newOrderSingle");
			//os.writeObject("35=D;");
			os.writeInt(id);
			os.writeObject(nos);
			os.flush();
		}
		return id++;
	}

	@Override
	public void sendCancel(int idToCancel){
		MockClient.Mock.show("sendCancel: id="+idToCancel);
		if(omConn.isConnected()){
			//OMconnection.sendMessage("cancel",idToCancel);
		}
	}

	@Override
	public void partialFill(Order order){
		MockClient.Mock.show(""+order);
	}

	@Override
	public void fullyFilled(Order order){
		MockClient.Mock.show(""+order);
		OUT_QUEUE.remove(order.getClientOrderID());
	}

	@Override
	public void cancelled(Order order){
		MockClient.Mock.show(""+order);
		OUT_QUEUE.remove(order.getClientOrderID());
	}

	@Override
	public void messageHandler(){

		ObjectInputStream is;
		try {
			while(runner){
				//is.wait(); //this throws an exception!!
				while(0 < omConn.getInputStream().available()){
					is = new ObjectInputStream(omConn.getInputStream());
					Object fixO = is.readObject();
					Order o = null;
					if (fixO instanceof Order) {
						o = (Order) fixO;
						log.info(Thread.currentThread().getName()+" received fix message: " + o.getOrdStatus() + ", " + o.getClientid() + ", " + o.getId());
						
						char MsgType = o.getOrdStatus();
						switch (MsgType) {
							case 'A':
								log.info("Order: " + o.getId() + " with status [" + o.getOrdStatus() + "] went to new order single acknowledgement");
								newOrderSingleAcknowledgement(o.getId());
								break;
							case 'C':
								log.info("Order: " + o.getId() + " with status [" + o.getOrdStatus() + "] went to cancelled");
								cancelled(o);
								break;
							case 'P':
								log.info("Order: " + o.getId() + " with status [" + o.getOrdStatus() + "] went to partially filled");
								partialFill(o);
								break;
							case 'F':
								log.info("Order: " + o.getId() + " with status [" + o.getOrdStatus() + "] went to fully filled");
								fullyFilled(o);
								break;
							default:
								log.info("Order: " + o.getId() + " with status [" + o.getOrdStatus() + "] went to default");
						}
					}
				}
			}
		} catch (IOException|ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	void newOrderSingleAcknowledgement(long OrderId){
		log.info(Thread.currentThread().getName()+" called newOrderSingleAcknowledgement for " + OrderId);
		//do nothing, as not recording so much state in the NOS class at present
	}

	public void fillOrder(long id, long sliceId, long size, double price) throws IOException {
		MockClient.Mock.show("fillOrder: id="+id+" sliceid="+sliceId+" size="+size+" price="+price);
		if(omConn.isConnected()){
			ObjectOutputStream os=new ObjectOutputStream(omConn.getOutputStream());
			os.writeObject("fill");
			os.writeLong(id);
			os.writeLong(sliceId);
			os.writeLong(size);
			os.writeDouble(price);
			os.flush();
		}
	}
/*listen for connections
once order manager has connected, then send and cancel orders randomly
listen for messages from order manager and print them to stdout.*/
}