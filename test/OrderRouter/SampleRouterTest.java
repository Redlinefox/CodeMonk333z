package OrderRouter;

import MockClient.MockClient;
import org.junit.Assert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import static org.junit.Assert.*;

public class SampleRouterTest {

    private SampleRouter router;
    private static final Random RANDOM_NUM_GENERATOR=new Random();
    ObjectInputStream is;
    ObjectOutputStream os;
    private Socket omConn;

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {
    }





    @org.junit.Test
    public void run() {


    }

    @org.junit.Test
    public void routeOrder() throws IOException {
        router = new SampleRouter("test", 2201);


    }

    /*
     os=new ObjectOutputStream(omConn.getOutputStream());
       Assert.assertNotNull(os);
    *
    * os=new ObjectOutputStream(omConn.getOutputStream());
		os.writeObject("newFill");
		os.writeInt(id);
		os.writeInt(sliceId);
		os.writeInt(fillSize);
		os.writeDouble(fillPrice);
		os.flush();
    *
    * */


    @org.junit.Test
    public void sendCancel() {

    }

    @org.junit.Test
    public void priceAtSize() {

    }


}