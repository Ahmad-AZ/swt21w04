package festivalmanager.catering;

import org.salespointframework.catalog.*;
//import org.salespointframework.inventory.*;
import org.salespointframework.quantity.Quantity;

import org.javamoney.moneta.Money;

import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;

/**
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

    public double getFilling() {
        return filling;
    }

    public void setFilling(double filling) {
        this.filling = filling;
    }

    public Money getDeposit() {
        return deposit;
    }

    public void setDeposit(Money deposit) {
        this.deposit = deposit;
    }

    public Quantity getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Quantity minimumStock) {
        this.minimumStock = minimumStock;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}