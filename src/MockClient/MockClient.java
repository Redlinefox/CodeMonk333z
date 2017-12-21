package MockClient;

import OrderClient.SampleClient;

import java.io.IOException;

public class MockClient extends Thread implements MockClientInterface{
	private int port;

	public MockClient(String name,int port){
		this.port=port;
		this.setName(name);
	}

	@Override
	public void run(){
		try {
			SampleClient client=new SampleClient(port);
			// Creates new order single and sends to output stream
			client.sendOrder();
			client.messageHandler();
			client.fillOrder(0, 0, 100, 200);
			client.messageHandler();
			client.cancelOrder(0, 0);
			client.messageHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
