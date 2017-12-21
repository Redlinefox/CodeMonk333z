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
				client.sendOrder();
				client.messageHandler();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
