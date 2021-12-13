package festivalmanager.catering;

import javax.persistence.*;
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

	// A default constructor is necessary for CateringSales.findAll() to work
	public CateringSalesItem() {
		this.quantity = Quantity.of(0);
		this.festivalId = 0;
		this.cateringProduct =null;
	}

    public CateringSalesItem(CateringProduct product, Quantity quantity, long festivalId) {
        this.quantity = quantity;
        this.festivalId = festivalId;
		this.cateringProduct= product;
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

	public long getId(){
		return id;
	}
}
