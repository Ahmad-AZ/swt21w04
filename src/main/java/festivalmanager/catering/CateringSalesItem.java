package festivalmanager.catering;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import static org.salespointframework.core.Currencies.*;
import org.javamoney.moneta.Money;
import org.salespointframework.quantity.*;
//import org.salespointframework.order.*;

/**
 * An item of the database table with the sold products
 * 
 * @author Robert Menzel
 */
@Entity
public class CateringSalesItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long festivalId;

	@Embedded
	private Quantity quantity;

	@ManyToOne
	CateringProduct cateringProduct;

	private MonetaryAmount salesPrice;

	/** A default constructor is necessary for CateringSales.findAll() to work */
	public CateringSalesItem() {
		this.quantity = Quantity.of(0);
		this.festivalId = 0;
		this.cateringProduct = null;
		this.salesPrice = Money.of(0.00, EURO);
	}

	/**
	 * The constructor that you have to use to construct a catering sales item
	 * 
	 * @param product    the catering product in this sales item
	 * @param quantity   the quantity of products sold in this item
	 * @param festivalId the id of the current festival
	 * @param salesPrice the sales price
	 */
	public CateringSalesItem(CateringProduct product, Quantity quantity, long festivalId, MonetaryAmount salesPrice) {
		this.quantity = quantity;
		this.festivalId = festivalId;
		this.cateringProduct = product;
		this.salesPrice = salesPrice;
	}

	/**
	 * a getter for the caqtering product in this sales item
	 * 
	 * @return the catering product
	 */
	public CateringProduct getCateringProduct() {
		return cateringProduct;
	}

	/**
	 * a getter for the festival id of this sales item
	 * 
	 * @return the festival id
	 */
	public long getFestivalId() {
		return festivalId;
	}

	/**
	 * a getter for the quantity of the product in this sales item
	 * 
	 * @return the quantity
	 */
	public Quantity getQuantity() {
		return quantity;
	}

	/**
	 * a getter for the price this bunch of products is sold for
	 * 
	 * @return the sales price
	 */
	public MonetaryAmount getSalesPrice() {
		return salesPrice;
	}

	/**
	 * a getter for the id as this saless item is saved in the database
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}
}
