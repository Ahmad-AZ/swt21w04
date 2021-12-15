package festivalmanager.catering;

import festivalmanager.festival.*;
import org.javamoney.moneta.Money;
import org.salespointframework.inventory.*;
import org.salespointframework.quantity.*;
import java.util.Optional;
import java.time.LocalDate;
import javax.persistence.Entity;
//import javax.persistence.ManyToOne;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;

/**
 * @author Robert Menzel
 */
@Entity
public class CateringStockItem extends MultiInventoryItem {
    private Money buyingPrice;
    private LocalDate orderDate;
    private LocalDate bestBeforeDate;
    private long festivalId;
    static FestivalManagement festivalManagement;

    @SuppressWarnings({ "unused" })
    private CateringStockItem() {
    }

    public CateringStockItem(long festivalId, CateringProduct product, Quantity quantity, Money buyingPrice,
            LocalDate orderDate,
            LocalDate bestBeforeDate) {
        super(product, quantity);
        this.buyingPrice = buyingPrice;
        this.orderDate = orderDate;
        this.bestBeforeDate = bestBeforeDate;
        this.festivalId = festivalId;
    }

    public long getFestivalId() {
        return festivalId;
    }

    public Festival getFestival() {
        if (festivalManagement == null) {
            return null;
        }
        Optional<Festival> oFm = festivalManagement.findById(festivalId);
        if (!oFm.isPresent()) {
            return null;
        }
        return oFm.get();
    }

    public Money getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(Money buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getBestBeforeDate() {
        return bestBeforeDate;
    }

    public void setBestBeforeDate(LocalDate bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    public CateringProduct getProduct() {
        return (CateringProduct) super.getProduct();
    }
}