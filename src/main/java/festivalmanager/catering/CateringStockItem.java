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
 * A item in the database table saving the catering stock.
 * 
 * @author Robert Menzel
 */
@Entity
public class CateringStockItem extends MultiInventoryItem {
    private Money buyingPrice;
    private LocalDate orderDate;
    private LocalDate bestBeforeDate;
    private long festivalId;
    private long orderQuantity;
    /**
     * The festival management lets get a festival by its id.
     */
    static FestivalManagement festivalManagement;

    /**
     * the constructor the spring framework uses to get data out of the database
     */
    @SuppressWarnings({ "unused" })
    private CateringStockItem() {
    }

    /**
     * the constructor of an catering stock item
     * 
     * @param festivalId     the id of the current festival
     * @param product        the product this stock is based on
     * @param quantity       the quantity of the product
     * @param orderQuantity  the quantity of this stockitem as it is ordered
     * @param buyingPrice    the price the product came into the stock
     * @param orderDate      the date the product is bought as
     * @param bestBeforeDate the date until the product is fresh
     */
    public CateringStockItem(
            long festivalId,
            CateringProduct product,
            Quantity quantity,
            long orderQuantity,
            Money buyingPrice,
            LocalDate orderDate,
            LocalDate bestBeforeDate) {
        super(product, quantity);
        this.buyingPrice = buyingPrice;
        this.orderDate = orderDate;
        this.bestBeforeDate = bestBeforeDate;
        this.festivalId = festivalId;
        this.orderQuantity = orderQuantity;
    }

    /**
     * returns the festival this stock item is in
     * 
     * @return a festival object
     */
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

    /**
     * a getter for the price this product is bought for
     * 
     * @return the in price
     */
    public Money getBuyingPrice() {
        return buyingPrice;
    }

    /**
     * a setter for the money the product is bought for
     * 
     * @param buyingPrice the in price
     */
    public void setBuyingPrice(Money buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    /**
     * a getter for the date the product is bought at
     * 
     * @return the date
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }

    /**
     * a setter for the date this product is bought at
     * 
     * @param orderDate the order date
     */
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * a getter for the date until the product is fresh
     * 
     * @return the best before date
     */
    public LocalDate getBestBeforeDate() {
        return bestBeforeDate;
    }

    /**
     * a setter for the date until the product is fresh
     * 
     * @param bestBeforeDate the best before date
     */
    public void setBestBeforeDate(LocalDate bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    /**
     * a getter for the product of this stock item
     */
    public CateringProduct getProduct() {
        return (CateringProduct) super.getProduct();
    }

    /**
     * a getter for the order quantity
     * 
     * @return a long with the quantity
     */
    public long getOrderQuantity() {
        return orderQuantity;
    }

    /**
     * a setter for the order quantity
     * 
     * @param orderQuantity a long with the quantity
     */
    public void setOrderQuantity(long orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}