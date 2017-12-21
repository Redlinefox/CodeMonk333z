package OrderManager;

import java.util.HashMap;
import java.util.Map;

public class Orders {
    private Map<Integer,Order> orders = new HashMap<>();

    public Orders() {
        
    }
    
    public void put(int id, Order o) {
        orders.put(id, o);
    }

    public Map<Integer, Order> getOrders() {
        return orders;
    }
    
    public Order get(Integer i) {
        return orders.get(i);
    }

    public void setOrders(Map<Integer, Order> orders) {
        this.orders = orders;
    }
}
