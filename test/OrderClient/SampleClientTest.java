package OrderClient;

import MockClient.MockClient;
import Ref.EqInstrument;
import Ref.Instrument;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.*;

public class SampleClientTest extends SampleClient {

    public SampleClientTest(int port) throws IOException {
        super(port);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void sendCancel() {
    }

    @Test
    public void partialFill() {
    }

    @Test
    public void fullyFilled() {
    }

    @Test
    public void cancelled() {
    }

    @Test
    public void messageHandler() {
    }
}