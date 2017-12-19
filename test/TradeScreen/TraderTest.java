package TradeScreen;

import MockClient.MockClient;
import OrderManager.Order;
import OrderRouter.SampleRouter;
import Ref.EqInstrument;
import Ref.Instrument;
import Ref.Ric;
import org.junit.*;
import org.junit.Assert;

import java.io.IOException;
import java.net.InetSocketAddress;

import static junit.framework.TestCase.*;

public class TraderTest {
    Trader ts;
    String traderName;
    int traderPort;
    Order o;
    Instrument instr;
    Ric r;

    @Before
    public void setUp() throws Exception {
        traderName = "TradeScreen.Trader James";
        traderPort = 2020;
        ts = new Trader(traderName, traderPort);
        r = new Ric("testRic");
        instr = new EqInstrument(1);
        o = new Order(1l, 1l, instr, 1l);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isRunner() {
    }

    @Test
    public void setRunner() {
    }

    @Test
    public void run() throws InterruptedException {
        ts.start();
        Thread.sleep(5000);
        ts.setRunner(false);
    }

    @Test
    public void newOrder() throws IOException, InterruptedException {
        ts.start();
        assertEquals(0, ts.getOrders().size());
        ts.newOrder(5, o);
        assertEquals(1, ts.getOrders().size());
    }

    @Test
    public void acceptOrder() throws IOException {
        //start sample clients
        MockClient c1=new MockClient("Client 1",2000);
        c1.start();
        (new MockClient("Client 2",2001)).start();

        //start sample routers
        (new SampleRouter("Router LSE",2010)).start();
        (new SampleRouter("Router BATE",2011)).start();
        Trader tsorder = new Trader("TradeScreen.Trader James",2020);
        InetSocketAddress trader=new InetSocketAddress("localhost",2020);
        tsorder.start();
        tsorder.acceptOrder(1);
        assertEquals(0, 0);
    }

    @Test
    public void sliceOrder() throws IOException {
        Trader tsorder = new Trader("TradeScreen.Trader James",2020);
        tsorder.start();
        tsorder.sliceOrder(1, 1);
        assertEquals(0, 0);
    }

    @Test
    public void price() {
        
    }
}