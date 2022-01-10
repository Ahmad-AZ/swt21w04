package festivalmanager.catering;

import java.time.LocalDate;
import java.util.List;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Quantity;
import org.javamoney.moneta.Money;
import static org.salespointframework.core.Currencies.*;
import festivalmanager.festival.*;

/**
 * @author Robert Menzel
 */
public class CateringStockInitializer implements DataInitializer {
    private final CateringProductCatalog catalog;
    private final CateringStock stock;
    private final Festival festival;

    public CateringStockInitializer(Festival festival, CateringProductCatalog catalog, CateringStock stock) {
        this.catalog = catalog;
        this.stock = stock;
        this.festival = festival;
    }

    public void initialize() {
        if (stock.findAll().iterator().hasNext()) {
            return;
        }
        List<CateringProduct> lsProduct;
        lsProduct = catalog.findByName("Coca-Cola").toList();
        if (!lsProduct.isEmpty()) {
            CateringStockItem item = new CateringStockItem(festival.getId(), lsProduct.get(0), Quantity.of(500),
                    500,
                    Money.of(0.50, EURO),
                    LocalDate.now(), LocalDate.of(2023, 04, 01));
            stock.save(item);
        }
    }
}