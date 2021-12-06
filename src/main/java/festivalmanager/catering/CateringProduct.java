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
    private Money deposit;
    private double filling;

    @SuppressWarnings({ "unused", "deprecation" })
    private CateringProduct() {
    }

    public CateringProduct(String productName, Money salesPrice, Money deposit, double filling) {
        super(productName, salesPrice);

        this.deposit = deposit;
        this.filling = filling;
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
}