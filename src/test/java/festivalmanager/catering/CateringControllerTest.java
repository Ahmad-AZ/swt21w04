package festivalmanager.catering;

import festivalmanager.AbstractIntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.ui.ExtendedModelMap;
//import org.springframework.ui.Model;
import org.javamoney.moneta.Money;
import org.salespointframework.quantity.Quantity;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.salespointframework.core.Currencies.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.quantity.*;

/**
 * Integration tests for {@link CateringController} that interact with the
 * controller directly.
 *
 * @author Robert Menzel
 */
public class CateringControllerTest extends AbstractIntegrationTests {
    @Autowired
    CateringController controller;
    private long festivalid = 10;

    private void initializeStock() {
        CateringStock stock = controller.getStock();
        if (!stock.findAll().isEmpty()) {
            return;
        }
        CateringProductCatalog catalog = controller.getCateringProductCatalog();

        Iterable<CateringProduct> icp = catalog.findAll();
        for (CateringProduct cateringProduct : icp) {
            CateringStockItem csi = new CateringStockItem(
                    festivalid, cateringProduct, Quantity.of(100), 500,
                    Money.of(0.5, EURO),
                    LocalDate.now(), LocalDate.now().plusDays(365));
            stock.save(csi);
            CateringStockItem csi2 = new CateringStockItem(
                    festivalid, cateringProduct, Quantity.of(300), 700,
                    Money.of(0.5, EURO),
                    LocalDate.now(), LocalDate.now().plusDays(364));
            stock.save(csi2);
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetBoughtProducts() {
        initializeStock();
        List<CateringProduct> lCP = new LinkedList<CateringProduct>();
        for (CateringProduct cateringProduct : controller.getBoughtProducts(festivalid)) {
            lCP.add(cateringProduct);
        }
        assertEquals(4, lCP.size(), "Es wurden " + lCP.size() + " gekaufte Produkte gefunden.");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetProductCounts() {
        initializeStock();
        Map<CateringProduct, Quantity> prodcnts = controller.getProductCounts(festivalid);
        for (CateringProduct product : prodcnts.keySet()) {
            Quantity quantity = prodcnts.get(product);
            assertEquals(400, quantity.getAmount().toBigInteger().intValue(),
                    "Es sind " + quantity.getAmount() + " " + product.getName() + " vorhanden.");
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testChopCart() {
        initializeStock();
        Cart cart = controller.initializeCart();
        Iterable<CateringProduct> icp = controller.getBoughtProducts(festivalid);
        for (CateringProduct cateringProduct : icp) {
            cart.addOrUpdateItem(cateringProduct, 500);
        }
        controller.chopCart(cart, festivalid);
        for (CartItem cartItem : cart) {
            assertEquals(400, cartItem.getQuantity().getAmount().toBigInteger().intValue());
        }
    }

}
