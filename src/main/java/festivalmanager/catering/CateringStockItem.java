package festivalmanager.catering;

import org.javamoney.moneta.Money;
import org.salespointframework.inventory.*;
import org.salespointframework.quantity.*;
import java.time.LocalDate;
import javax.persistence.Entity;
//import javax.persistence.ManyToOne;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;

@Entity
public class CateringStockItem extends MultiInventoryItem {
    private Money buyingPrice;
    private int amount;
    private LocalDate orderDate;
    private LocalDate bestBeforeDate;

    @SuppressWarnings({ "unused" })
    private CateringStockItem() {
    }

    public CateringStockItem(CateringProduct product, Quantity quantity, Money buyingPrice, LocalDate orderDate,
            LocalDate bestBeforeDate) {
        super(product, quantity);
        this.buyingPrice = buyingPrice;
        this.orderDate = orderDate;
        this.bestBeforeDate = bestBeforeDate;
    }

    public Money getBuyingPrice() {
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