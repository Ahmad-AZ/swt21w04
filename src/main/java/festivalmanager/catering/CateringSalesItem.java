package festivalmanager.catering;

import festivalmanager.festival.*;
import java.util.Optional;
import javax.persistence.Entity;
import org.salespointframework.inventory.*;
import org.salespointframework.quantity.*;
import org.salespointframework.order.*;

/**
 * @author Robert Menzel
 */
@Entity
public class CateringSalesItem extends MultiInventoryItem {
    private long festivalId, cateringStandId;
    // private Cart cart = new Cart();
    static FestivalManagement festivalManagement;

    public CateringSalesItem(CateringProduct product, Quantity quantity, long festivalId, long cateringStandId) {
        super(product, quantity);
        this.festivalId = festivalId;
        this.cateringStandId = cateringStandId;
    }

    public long getFestivalId() {
        return festivalId;
    }

    public long getCateringStandId() {
        return cateringStandId;
    }

    public Festival getFestival() {
        if (festivalManagement == null)
            return null;
        Optional<Festival> oFm = festivalManagement.findById(festivalId);
        if (!oFm.isPresent())
            return null;
        return oFm.get();
    }

    // public Cart getCart() {
    // return cart;
    // }
}
