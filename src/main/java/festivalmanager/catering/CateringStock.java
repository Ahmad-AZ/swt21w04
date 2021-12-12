package festivalmanager.catering;

import org.salespointframework.inventory.*;

/**
 * @author Robert Menzel
 */
public interface CateringStock extends MultiInventory<CateringStockItem> {
    public Iterable<CateringStockItem> findByFestivalId(long festivalid);
}
