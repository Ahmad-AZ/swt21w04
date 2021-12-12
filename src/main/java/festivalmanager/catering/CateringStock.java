package festivalmanager.catering;

import org.salespointframework.inventory.*;

public interface CateringStock extends MultiInventory<CateringStockItem> {
    public Iterable<CateringStockItem> findByFestivalId(long festivalid);
}
