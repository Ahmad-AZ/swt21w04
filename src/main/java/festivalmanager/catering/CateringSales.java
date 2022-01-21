package festivalmanager.catering;

//import org.salespointframework.catalog.ProductIdentifier;
//import org.salespointframework.inventory.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * The database table storing the sold products for every festival
 * 
 * @author Robert Menzel
 */
public interface CateringSales extends CrudRepository<CateringSalesItem, Long> {
	// public Iterable<CateringStockItem> findByFestivalId(long festivalid);

	/**
	 * get all sold products in the catering sales
	 */
	@Override
	Streamable<CateringSalesItem> findAll();

	/**
	 * get all sales for an product
	 * 
	 * @param product the product to search for
	 * @return an iteration of catering sales items
	 */
	Iterable<CateringSalesItem> findByCateringProduct(CateringProduct product);
}
