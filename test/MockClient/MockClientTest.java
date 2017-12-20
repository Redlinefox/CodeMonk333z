package MockClient;

import OrderClient.SampleClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MockClientTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void run() {
        try {
            SampleClient sampleClient = new SampleClient(2000);



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


/*
*
* try {
			SampleClient client=new SampleClient(port);
			if(port==2000){
				client.sendOrder();
				int id=client.sendOrder();
				//TODO client.sendCancel(id);
				client.messageHandler();
			}else{
				client.sendOrder();
				client.messageHandler();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
*
*
* */