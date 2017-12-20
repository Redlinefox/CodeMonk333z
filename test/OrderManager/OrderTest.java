package OrderManager;

import Ref.EqInstrument;
import Ref.Instrument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderTest {
    Order o;
    Order o1;
    Instrument instrument;

    @Before
    public void setUp() throws Exception {
        instrument = new EqInstrument(1);
        o = new Order(1, 1, instrument, 500);
        o1 = new Order(2, 2, instrument, 300);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void sliceSizes() {
        assertEquals(0, o.getSlices().size());
        o.newSlice(1);
        assertEquals(1, o.getSlices().size());
    }

    @Test
    public void sizeFilled() {
        assertEquals(0, o.sizeFilled());
        o.createFill(100,250);
        assertEquals(100, o.sizeFilled());
    }

    @Test
    public void sizeRemaining() {
        assertEquals(o.getSize(), o.sizeRemaining());
        o.createFill(100,250);
        assertEquals(400, o.sizeRemaining());
    }

    @Test
    public void createFill() {
        assertEquals(o.getSize(), o.sizeRemaining());
        o.createFill(100,250);
        assertEquals(400, o.sizeRemaining());
        o.createFill(400,325);
        assertEquals(0, o.sizeRemaining());
    }

    @Test
    public void cross() {
        assertEquals(o.getSize(), o.sizeRemaining());
        o.cross(o1);
        assertEquals(o.getSize(), o.sizeRemaining());

    }

}