package festivalmanager.catering;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.utils.CurrentPageManagement;
import festivalmanager.utils.LongOrNull;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.quantity.Quantity;
import static org.salespointframework.core.Currencies.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.money.format.MonetaryParseException;

import org.javamoney.moneta.*;

@Controller
public class CateringProductCatalogController {

    private Festival currentFestival;
    private FestivalManagement festivalManagement;
	private CurrentPageManagement currentPageManagement;
    private CateringProductCatalog catalog;
    private CateringStock stock;

    public CateringProductCatalogController(CateringProductCatalog catalog, CateringStock stock,
            FestivalManagement festivalManagement, CurrentPageManagement currentPageManagement) {
        this.catalog = catalog;
        this.festivalManagement = festivalManagement;
        this.stock = stock;
        this.currentPageManagement = currentPageManagement;
    }

    @GetMapping("/cateringProductCatalog")
    String products(Model model, @ModelAttribute("currentFestivalId") LongOrNull currentFestivalId) {
        if (currentFestivalId.getValue() != null) {
            long longId = currentFestivalId.getValue();
            this.currentFestival = festivalManagement.findById(longId).get();
        }

        model.addAttribute("stock", stock.findAll());
        model.addAttribute("productcatalog", catalog.findAll());
        model.addAttribute("festival", currentFestival);
		currentPageManagement.updateCurrentPage(model, "catering");
        return "cateringProductCatalog";
    }

    @GetMapping("/cateringAddProduct")
    String addProduct(Model model) {
        model.addAttribute("festival", currentFestival);
		currentPageManagement.updateCurrentPage(model, "catering");
        return "cateringAddProduct";
    }

    @PostMapping("/cateringAddProduct/editData")
    String addProduct(Model model, ProductFormularData formularData) {
        boolean failure = false;

        Money formPrice = Money.of(2.50, EURO);
        try {
            formPrice = Money.parse(formularData.price);
        } catch (MonetaryParseException ex) {
            failure = true;
        }

        Money formDeposit = Money.of(0.25, EURO);
        try {
            formDeposit = Money.parse(formularData.deposit);
        } catch (MonetaryParseException ex) {
            failure = true;
        }

        CateringProduct product = new CateringProduct(formularData.name, formPrice, formDeposit,
                formularData.filling);

        if (!failure)
            catalog.save(product);
        else
            model.addAttribute("product", product);


        return (failure) ? "/cateringAddProduct" : "redirect:/cateringProductCatalog";
    }

    @GetMapping("/cateringEditProduct")
    String editProduct(Model model) {
		model.addAttribute("festival", currentFestival);
		currentPageManagement.updateCurrentPage(model, "catering");
        return "cateringEditProduct";
    }

    @GetMapping("/cateringEditProduct/{productid}")
    String editProduct(@PathVariable ProductIdentifier productid, Model model) {

        // System.out.println("productid:" + productid);
        Optional<CateringProduct> oProduct = catalog.findById(productid);
        if (oProduct.isPresent()) {
            CateringProduct product = oProduct.get();
            model.addAttribute("product", product);
        }

        model.addAttribute("festival", currentFestival);
		currentPageManagement.updateCurrentPage(model, "catering");
        return "cateringEditProduct";
    }

    @PostMapping("/cateringEditProduct/editData/{productid}")
    String editProductData(@PathVariable ProductIdentifier productid, Model model, ProductFormularData formularData) {
        boolean changed = false;
        boolean failure = false;
        CateringProduct product;

        Optional<CateringProduct> oProduct = catalog.findById(productid);
        if (oProduct.isPresent()) {
            product = oProduct.get();

            if (!product.getName().equals(formularData.name)) {
                changed = true;
                product.setName(formularData.name);
                System.out.println("Name:" + formularData.name);
            }

            Money formprice = Money.of(2.50, EURO);
            try {
                formprice = Money.parse(formularData.price);
            } catch (MonetaryParseException ex) {
                failure = true;
            }
            if (!product.getPrice().equals(formprice)) {
                changed = true;
                product.setPrice(formprice);
                System.out.println("Preis:" + formprice);
            }

            Money formdeposit = Money.of(0.25, EURO);
            try {
                formdeposit = Money.parse(formularData.deposit);
            } catch (MonetaryParseException ex) {
                failure = true;
            }
            if (!product.getDeposit().equals(formdeposit)) {
                changed = true;
                product.setDeposit(formdeposit);
                System.out.println("Pfand:" + formdeposit);
            }

            if (Double.compare(product.getFilling(), formularData.filling) != 0) {
                changed = true;
                product.setFilling(formularData.filling);
                System.out.println("Füllmenge:" + formularData.filling);
            }

            if (changed) {
                catalog.save(product);
            }

        }

        return (failure) ? "redirect:/cateringEditProduct/" + productid : "redirect:/cateringProductCatalog";
    }

    @GetMapping("/cateringDeleteProduct/{productid}")
    String deleteProduct(@PathVariable ProductIdentifier productid, Model model) {
        Optional<CateringProduct> oProduct = catalog.findById(productid);
        if (oProduct.isPresent()) {
            CateringProduct product = oProduct.get();
            model.addAttribute("product", product);
        }

		currentPageManagement.updateCurrentPage(model, "catering");
        model.addAttribute("festival", currentFestival);
        return "cateringDeleteProduct";
    }

    @PostMapping("/cateringDeleteProduct/delete/{productid}")
    String deleteProduct(@PathVariable ProductIdentifier productid) {
        Optional<CateringProduct> oProduct = catalog.findById(productid);
        if (oProduct.isPresent()) {
            CateringProduct product = oProduct.get();
            catalog.delete(product);

        }
        return "redirect:/cateringProductCatalog";
    }

    class ProductFormularData {
        String name;
        String price, deposit;
        double filling;

        public ProductFormularData(String name, String price, String deposit, double filling) {
            this.name = name;
            this.price = price;
            this.deposit = deposit;
            this.filling = filling;
        }
    }

    @GetMapping("/cateringAddStockItem")
    String addStockItem(Model model) {
        model.addAttribute("festival", currentFestival);
        model.addAttribute("productcatalog", catalog.findAll());
        model.addAttribute("orderdate", LocalDate.now());
        model.addAttribute("bestbeforedate", LocalDate.now().plusYears(2));
        return "cateringAddStockItem";
    }

    @PostMapping("/cateringAddStockItem/editData")
    String addStockItem(Model model, StockFormularData formularData) {
        boolean failure = false;

        Optional<CateringProduct> oProduct = catalog.findById(formularData.productid);
        CateringProduct product = null;
        if (oProduct.isPresent()) {
            product = oProduct.get();
        } else
            failure = true;

        Money formBuyingPrice = Money.of(0.50, EURO);
        try {
            formBuyingPrice = Money.parse(formularData.buyingprice);
        } catch (MonetaryParseException ex) {
            failure = true;
        }

        LocalDate formOrderDate = LocalDate.now();
        try {
            formOrderDate = LocalDate.parse(formularData.orderdate);
        } catch (DateTimeParseException ex) {
            failure = true;
        }

        LocalDate formBestBeforeDate = LocalDate.now().plusYears(2);
        try {
            formBestBeforeDate = LocalDate.parse(formularData.bestbeforedate);
        } catch (DateTimeParseException ex) {
            failure = true;
        }

        if (!failure) {
            CateringStockItem stockitem = new CateringStockItem(product, Quantity.of(formularData.amount),
                    formBuyingPrice, formOrderDate, formBestBeforeDate);
            stock.save(stockitem);
        } else {
            model.addAttribute("productcatalog", catalog.findAll());
            model.addAttribute("orderdate", LocalDate.now());
            model.addAttribute("bestbeforedate", LocalDate.now().plusYears(2));
        }
        model.addAttribute("festival", currentFestival);

        return (failure) ? "/cateringAddStockItem" : "redirect:/cateringProductCatalog";
    }

    @GetMapping("/cateringEditStockItem/{stockitemid}")
    String editStockItem(@PathVariable InventoryItemIdentifier stockitemid, Model model) {
        Optional<CateringStockItem> oStockItem = stock.findById(stockitemid);
        if (oStockItem.isPresent()) {
            CateringStockItem stockitem = oStockItem.get();
            model.addAttribute("stockitem", stockitem);
        }
        model.addAttribute("festival", currentFestival);
        model.addAttribute("productcatalog", catalog.findAll());
        return "cateringEditStockItem";
    }

    @PostMapping("/cateringEditStockItem/editData/{stockitemid}")
    String editStockItem(@PathVariable InventoryItemIdentifier stockitemid, Model model,
            StockFormularData formularData) {
        boolean failure = false;

        Money formBuyingPrice = Money.of(0.50, EURO);
        try {
            formBuyingPrice = Money.parse(formularData.buyingprice);
        } catch (MonetaryParseException ex) {
            failure = true;
        }

        LocalDate formOrderDate = LocalDate.now();
        try {
            formOrderDate = LocalDate.parse(formularData.orderdate);
        } catch (DateTimeParseException ex) {
            failure = true;
        }

        LocalDate formBestBeforeDate = LocalDate.now().plusYears(2);
        try {
            formBestBeforeDate = LocalDate.parse(formularData.bestbeforedate);
        } catch (DateTimeParseException ex) {
            failure = true;
        }

        if (!failure) {
            Optional<CateringStockItem> oStockItem = stock.findById(stockitemid);
            CateringStockItem stockitem = null;
            if (oStockItem.isPresent()) {
                stockitem = oStockItem.get();
                boolean changed = false;
                if (!stockitem.getBuyingPrice().equals(formBuyingPrice)) {
                    changed = true;
                    stockitem.setBuyingPrice(formBuyingPrice);
                }
                if (!stockitem.getOrderDate().equals(formOrderDate)) {
                    changed = true;
                    stockitem.setOrderDate(formOrderDate);
                }
                if (!stockitem.getBestBeforeDate().equals(formBestBeforeDate)) {
                    changed = true;
                    stockitem.setBestBeforeDate(formBestBeforeDate);
                }
                BigDecimal bdFormAmount = new BigDecimal(formularData.amount);
                if (!stockitem.getQuantity().getAmount().equals(bdFormAmount)) {
                    changed = true;
                    BigDecimal bdDifference = stockitem.getQuantity().getAmount().subtract(bdFormAmount);
                    double lDifference = bdDifference.doubleValue();
                    System.out.println("lDifference" + lDifference);
                    Quantity qDifference = Quantity.of(lDifference);
                    stockitem.decreaseQuantity(qDifference);
                    System.out.println(stockitem.getQuantity());
                }
                if (changed)
                    stock.save(stockitem);

            } else
                failure = true;

            stock.save(stockitem);
        } else {
            model.addAttribute("productcatalog", catalog.findAll());
            model.addAttribute("orderdate", LocalDate.now());
            model.addAttribute("bestbeforedate", LocalDate.now().plusYears(2));
        }
        model.addAttribute("festival", currentFestival);

        return (failure) ? "/cateringEditStockItem" : "redirect:/cateringProductCatalog";

    }

    class StockFormularData {
        ProductIdentifier productid;
        double amount;
        String buyingprice;
        String orderdate, bestbeforedate;

        public StockFormularData(ProductIdentifier productid, double amount, String buyingprice, String orderdate,
                String bestbeforedate) {
            this.productid = productid;
            this.amount = amount;
            this.buyingprice = buyingprice;
            this.orderdate = orderdate;
            this.bestbeforedate = bestbeforedate;
        }

    }
}
