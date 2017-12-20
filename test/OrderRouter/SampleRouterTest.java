package OrderRouter;

import MockClient.MockClient;
import Ref.EqInstrument;
import Ref.Instrument;
import org.junit.Assert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class SampleRouterTest {

    private SampleRouter router;
    private static final Random RANDOM_NUM_GENERATOR=new Random();
    ObjectInputStream is;
    ObjectOutputStream os;
    private Socket omConn;
    Instrument instr;

    @org.junit.Before
    public void setUp() throws Exception {
        router = new SampleRouter("test", 2201);
        instr = new EqInstrument(1);
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void run() {


    }

    @org.junit.Test
    public void routeOrder() throws IOException, InterruptedException {
        router.routeOrder(1,1,500,instr);
        assertNull(omConn);
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
    public void priceAtSize() throws IOException {
        router.priceAtSize(1,1,instr,500);
        assertNull(omConn);
    }


}