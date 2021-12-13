package festivalmanager.catering;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import static org.salespointframework.core.Currencies.*;
import org.javamoney.moneta.Money;
import org.salespointframework.quantity.*;
//import org.salespointframework.order.*;

/**
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

	// A default constructor is necessary for CateringSales.findAll() to work
	public CateringSalesItem() {
		this.quantity = Quantity.of(0);
		this.festivalId = 0;
		this.cateringProduct = null;
		this.salesPrice = Money.of(0.00, EURO);
	}

	public CateringSalesItem(CateringProduct product, Quantity quantity, long festivalId, MonetaryAmount salesPrice) {
		this.quantity = quantity;
		this.festivalId = festivalId;
		this.cateringProduct = product;
		this.salesPrice = salesPrice;
	}

	public CateringProduct getCateringProduct() {
		return cateringProduct;
	}

	public long getFestivalId() {
		return festivalId;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public MonetaryAmount getSalesPrice() {
		return salesPrice;
	}

	public long getId() {
		return id;
	}
}
