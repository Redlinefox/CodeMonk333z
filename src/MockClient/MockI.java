package MockClient;

import OrderManager.OrderManager;
import org.apache.log4j.Logger;

public class MockI{
	public static void show(String out) {
		Logger log = Logger.getLogger(MockI.class.getName());
		log.error(out);
	}
}