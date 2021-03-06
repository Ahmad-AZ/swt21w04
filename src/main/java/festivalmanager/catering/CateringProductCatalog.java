package festivalmanager.catering;

//import java.util.Collection;
//import java.util.Optional;

import org.salespointframework.catalog.Catalog;
//import org.salespointframework.catalog.ProductIdentifier;
//import org.springframework.data.util.Streamable;

/**
 * The catalog with the products in the database
 * 
 * @author Robert Menzel
 */
public interface CateringProductCatalog extends Catalog<CateringProduct> {

    public Iterable<CateringProduct> findByHidden(boolean hidden);
}
