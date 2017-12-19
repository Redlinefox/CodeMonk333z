package OrderManager;

import java.io.Serializable;

public class Fill implements Serializable {
    //long id;
    private long size;
    private double price;
    
    public Fill(long size,double price){
        this.size=size;
        this.price=price;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
