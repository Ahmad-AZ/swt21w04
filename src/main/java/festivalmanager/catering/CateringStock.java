package festivalmanager.catering;

import org.salespointframework.inventory.*;
import org.springframework.data.domain.Sort;

/**
 * @author Robert Menzel
 */
public interface CateringStock extends MultiInventory<CateringStockItem> {
    public static final Sort BBD_SORT = Sort.by("bestBeforeDate").ascending();

    public Iterable<CateringStockItem> findByFestivalId(long festivalid);

    public Iterable<CateringStockItem> findByFestivalId(long festivalid, Sort sort);
}
