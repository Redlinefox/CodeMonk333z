package OrderManager;

import LiveMarketData.LiveMarketData;
import LiveMarketData.SampleLiveMarketData;
import MockClient.MockOM;
import Ref.EqInstrument;
import Ref.Instrument;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.junit.Assert.*;

public class OrderManagerTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void acceptOrder() {


    }

    @Test
    public void sliceOrder() {
        try {
            InetSocketAddress[] clients = {new InetSocketAddress("localhost", 2000),
                    new InetSocketAddress("localhost", 2001)};
            InetSocketAddress[] routers = {new InetSocketAddress("localhost", 2010),
                    new InetSocketAddress("localhost", 2011)};
            InetSocketAddress trader = new InetSocketAddress("localhost", 2020);
            LiveMarketData liveMarketData = new SampleLiveMarketData();
//            SampleLiveMarketData sampleLiveMarketData = new SampleLiveMarketData();
            Instrument instrument = new EqInstrument(2002);
            Order order = new Order(2000, 2001, instrument, 2003);
//            MockOM mockOM = new MockOM("Order Manager", routers, clients, trader, liveMarketData);
//            mockOM.start();

            OrderManager orderManager = new OrderManager(routers, clients, trader, liveMarketData);
            orderManager.sliceOrder(((int) order.getId()), 1003);
            long exspectedSize = 1000;
            long acualSize = order.getSize();
            Assert.assertEquals(exspectedSize, acualSize);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}