package festivalmanager.catering;

import org.salespointframework.inventory.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * @author Robert Menzel
 */
public interface CateringSales extends CrudRepository<CateringSalesItem, Long> {
    // public Iterable<CateringStockItem> findByFestivalId(long festivalid);

	@Override
	Streamable<CateringSalesItem> findAll();
}
