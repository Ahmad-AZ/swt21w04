package festivalmanager.catering;

import org.salespointframework.inventory.*;
import org.springframework.data.domain.Sort;

/**
 * The database table with the stock of all festivals
 * 
 * @author Robert Menzel
 */
public interface CateringStock extends MultiInventory<CateringStockItem> {
    /**
     * a sorter ordering the stock ascending with bestBefore
     */
    public static final Sort BBD_SORT = Sort.by("bestBeforeDate").ascending();

    /**
     * gives you all the stock for this festival
     * 
     * @param festivalid the id of the current festival
     * @return an iteration of catering stock items
     */
    public Iterable<CateringStockItem> findByFestivalId(long festivalid);

    /**
     * gives you all the stock for this festival ordered by a sort criteria
     * 
     * @param festivalid the id of the current festival
     * @param sort       a sort criteria
     * @return an iteration of catering stock items
     */
    public Iterable<CateringStockItem> findByFestivalId(long festivalid, Sort sort);
}
