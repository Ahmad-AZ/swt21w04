package festivalmanager.catering;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.util.Streamable;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.javamoney.moneta.Money;
import org.salespointframework.quantity.Quantity;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.salespointframework.core.Currencies.*;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;

/**
 * Integration tests for {@link CateringController} that interact with the
 * controller directly.
 *
 * @author Robert Menzel
 */
public class CateringControllerTest extends AbstractIntegrationTests {
    @Autowired
    CateringController controller;

    private void initializeStock() {
        long festivalid = getFestivalId();
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

    private Festival findFirstFestival() {
        if (controller == null) {
            return null;
        }
        FestivalManagement fm = controller.getFestivalManagement();
        if (fm == null) {
            return null;
        }
        Iterator<Festival> sFe = fm.findAll().iterator();
        Festival festival = null;
        if (sFe.hasNext()) {
            festival = sFe.next();
        }
        return festival;
    }

    private long getFestivalId() {
        Festival festival = findFirstFestival();
        long festivalid = (festival == null) ? -1 : festival.getId();
        return festivalid;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetBoughtProducts() {
        long festivalid = getFestivalId();
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
        long festivalid = getFestivalId();
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
        long festivalid = getFestivalId();
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

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testBookOut() {
        long festivalid = getFestivalId();
        System.out.println("festivalid:" + festivalid);
        initializeStock();
        Iterator<CateringProduct> icp = controller.getBoughtProducts(festivalid).iterator();
        CateringProduct p1 = icp.next();
        CateringProduct p2 = icp.next();
        CateringProduct p3 = icp.next();
        CateringProduct p4 = icp.next();
        controller.bookOut(p1, Quantity.of(400), festivalid);
        controller.bookOut(p2, Quantity.of(300), festivalid);
        controller.bookOut(p3, Quantity.of(200), festivalid);
        controller.bookOut(p4, Quantity.of(100), festivalid);
        Map<CateringProduct, Quantity> prodcnts = controller.getProductCounts(festivalid);
        assertEquals(0, prodcnts.get(p1).getAmount().toBigInteger().intValue());
        assertEquals(100, prodcnts.get(p2).getAmount().toBigInteger().intValue());
        assertEquals(200, prodcnts.get(p3).getAmount().toBigInteger().intValue());
        assertEquals(300, prodcnts.get(p4).getAmount().toBigInteger().intValue());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCSales() {
        long festivalid = getFestivalId();
        initializeStock();
        Cart cart = controller.initializeCart();
        Model model = new ExtendedModelMap();
        String returnedView = controller.sales(model, cart, festivalid);
        Object oPC = model.getAttribute("productcatalog");
        assertNotNull(oPC);
        assertEquals("catering", returnedView);
        // Iterable<CateringProduct> = (Iterable<CateringProduct>) oPC;
        if (oPC instanceof Iterable<?>) {
            Iterable<?> iPC = (Iterable<?>) oPC;
            LinkedList<Object> lPC = new LinkedList<Object>();
            for (Object object : iPC) {
                lPC.add(object);
            }
            assertEquals(4, lPC.size());

        }
    }
}
