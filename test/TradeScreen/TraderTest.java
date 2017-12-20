package TradeScreen;

import MockClient.MockClient;
import OrderManager.Order;
import OrderRouter.SampleRouter;
import Ref.EqInstrument;
import Ref.Instrument;
import Ref.LoggingManager;
import Ref.Ric;
import org.apache.log4j.Logger;
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
        o = new Order(1l, 1l, instr, 2l);
        
        LoggingManager.initialiseLogging();
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
        assertNull(ts.getOmConn());
        ts.start();
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
    public void price() throws IOException, InterruptedException {
        assertEquals(0, o.getSlices().size());
        ts.price(1l, o);
        assertEquals(1, o.getSlices().size());
    }
}