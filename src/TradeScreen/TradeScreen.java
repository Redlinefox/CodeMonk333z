package TradeScreen;

import java.io.IOException;

import OrderManager.Order;

public interface TradeScreen {
	public enum api{newOrder,price,fill,cross};
	public void newOrder(long id,Order order) throws IOException, InterruptedException;
	public void acceptOrder(long id) throws IOException;
	public void sliceOrder(long id,long sliceSize) throws IOException;
	public void price(long id,Order o) throws InterruptedException, IOException;
}
