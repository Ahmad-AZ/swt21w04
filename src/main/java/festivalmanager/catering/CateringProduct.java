package festivalmanager.catering;

import org.salespointframework.catalog.*;
//import org.salespointframework.inventory.*;
//import org.salespointframework.quantity.Quantity;

import org.javamoney.moneta.Money;

import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;

@Entity
public class CateringProduct extends Product {
    private int productID;
    private Money deposit;
    private double filling;

    @SuppressWarnings({ "unused", "deprecation" })
    private CateringProduct() {
    }

    public CateringProduct(int productID, String productName, Money salesPrice, Money deposit, double filling) {
        super(productName, salesPrice);

        this.productID = productID;
        this.deposit = deposit;
        this.filling = filling;
    }

    public int getProductID() {
        return productID;
    }

    public double getFilling() {
        return filling;
    }

    public Money getDeposit() {
        return deposit;
    }
}