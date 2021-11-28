package festivalmanager.catering;

import org.salespointframework.inventory.*;
import java.time.LocalDate;
import javax.persistence.Entity;
//import javax.persistence.ManyToOne;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;

@Entity
public class CateringStockItem extends MultiInventoryItem {

    private CateringProduct product;
    private double buyingPrice;
    private int amount;
    private LocalDate orderDate;
    private LocalDate bestBeforeDate;

    @SuppressWarnings({ "unused" })
    private CateringStockItem() {
    }

    public CateringStockItem(CateringProduct product, double buyingPrice, int amount, LocalDate orderDate,
            LocalDate bestBeforeDate) {
        this.product = product;
        this.buyingPrice = buyingPrice;
        this.amount = amount;
        this.orderDate = orderDate;
        this.bestBeforeDate = bestBeforeDate;
    }

    public CateringProduct getProduct() {
        return product;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public LocalDate getBestBeforeDate() {
        return bestBeforeDate;
    }

}