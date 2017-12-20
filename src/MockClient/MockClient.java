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
			if(port==2000){
				//TODO why does this take an arg?
				client.sendOrder();
				int id=client.sendOrder();
				//TODO client.sendCancel(id);
				client.messageHandler();
			}else{
				client.sendOrder();
				client.messageHandler();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
