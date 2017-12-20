package Ref;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RicTest {
    Ric ric;

    @Before
    public void setup() {
        ric = new Ric("test.l");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getRic() {
        assertEquals("test", ric.getRic());
    }

    @Test
    public void setRic() {
        ric.setRic("ricTest");
        assertEquals("ricTest", ric.getRic());
    }

    @Test
    public void getEx() {
        assertEquals("l", ric.getEx());
    }

    @Test
    public void getCompany() {
        assertEquals("l", ric.getEx());

    }
}