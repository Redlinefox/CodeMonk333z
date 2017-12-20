package LiveMarketData;

import OrderManager.Order;
import Ref.EqInstrument;
import Ref.Instrument;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SampleLiveMarketDataTest {
    private Instrument instrument;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setPrice() {
        SampleLiveMarketData sampleLiveMarketData = new SampleLiveMarketData();
        Instrument instrument = new EqInstrument(2002);
        Order order = new Order(2000, 2001, instrument, 2003);
        double startPrice = order.getInitialMarketPrice();
        sampleLiveMarketData.setPrice(order);
        double endPrice = order.getInitialMarketPrice();
        Assert.assertNotEquals(startPrice, endPrice);
    }
}