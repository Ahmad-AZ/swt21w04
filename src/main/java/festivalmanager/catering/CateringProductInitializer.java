package festivalmanager.catering;

import static org.salespointframework.core.Currencies.*;
import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;
import org.javamoney.moneta.Money;

@Component
public class CateringProductInitializer implements DataInitializer {

    private final CateringProductCatalog catalog;

    public CateringProductInitializer(CateringProductCatalog catalog) {
        this.catalog = catalog;
    }

    public void initialize() {
        if (catalog.findAll().iterator().hasNext())
            return;
        catalog.save(new CateringProduct(1, "Coca-Cola", Money.of(2.50, EURO), Money.of(0.25, EURO), 0.33));
        catalog.save(new CateringProduct(2, "Red Bull", Money.of(3.50, EURO), Money.of(0.25, EURO), 0.25));
        catalog.save(new CateringProduct(3, "Becks", Money.of(3.00, EURO), Money.of(0.08, EURO), 0.5));
        catalog.save(new CateringProduct(4, "Wurst", Money.of(2.50, EURO), Money.of(0.00, EURO), 0.0));
    }

}