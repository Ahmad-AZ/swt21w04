package festivalmanager.catering;

import org.salespointframework.catalog.*;
//import org.salespointframework.inventory.*;
import org.salespointframework.quantity.Quantity;
import org.javamoney.moneta.Money;
import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;

/**
 * The class representing a catering product
 * 
 * @author Robert Menzel
 */
@Entity
public class CateringProduct extends Product {
    private Money deposit;
    private double filling;
    private Quantity minimumStock;
    private boolean hidden;

    @SuppressWarnings({ "unused", "deprecation" })
    private CateringProduct() {
    }

    /**
     * the constructor for a catering product
     * 
     * @param productName  the name of this product
     * @param salesPrice   the price the product is sold for
     * @param deposit      the deposit of the beverage
     * @param filling      the filling of the beverage
     * @param minimumStock the minimum quantity of products in the stock
     *                     (if its below that the manager has to reorder)
     * @param hidden       hides products in the database when its not possible to
     *                     delete them
     */
    public CateringProduct(
            String productName,
            Money salesPrice,
            Money deposit,
            double filling,
            Quantity minimumStock,
            boolean hidden) {
        super(productName, salesPrice);

        this.deposit = deposit;
        this.filling = filling;
        this.minimumStock = minimumStock;
        this.hidden = hidden;
    }

    /**
     * a getter for the filling of the beverage
     * 
     * @return the filling
     */
    public double getFilling() {
        return filling;
    }

    /**
     * a setter for the filling of the beverage
     * 
     * @param filling
     */
    public void setFilling(double filling) {
        this.filling = filling;
    }

    /**
     * a getter for the deposit
     * 
     * @return the price of the deposit
     */
    public Money getDeposit() {
        return deposit;
    }

    /**
     * a setter for the deposit
     * 
     * @param deposit the price of the deposit
     */
    public void setDeposit(Money deposit) {
        this.deposit = deposit;
    }

    /**
     * a getter for the minimum quantity of this product in the stock
     * 
     * @return the quantity for the minimum message to the manager
     */
    public Quantity getMinimumStock() {
        return minimumStock;
    }

    /**
     * a setter for the minimum quantity of this product in the stock
     * 
     * @param minimumStock the quantity for the minimum message to the manager
     */
    public void setMinimumStock(Quantity minimumStock) {
        this.minimumStock = minimumStock;
    }

    /**
     * if a product is hidden in the database this flag is setted
     * 
     * @return the hidden flag
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * sets the hidden flag
     * 
     * @param hidden true if this product is to bes set hidden
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}