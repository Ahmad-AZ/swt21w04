package festivalmanager.catering;

//import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.utils.UtilsManagement;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.InventoryItems;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.quantity.Quantity;
import static org.salespointframework.core.Currencies.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
//import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.money.format.MonetaryParseException;

import org.javamoney.moneta.*;

/**
 * A controller supporting the catering stock for a festival and the product
 * catalog
 * 
 * @author Robert Menzel
 */
@Controller
public class CateringStockController {

    private UtilsManagement utilsManagement;
    @SuppressWarnings("unused")
    private FestivalManagement festivalManagement;
    private CateringProductCatalog catalog;
    private CateringStock stock;
    private CateringSales sales;

    /**
     * The constructor for this controller
     * 
     * @param catalog            the catering product catalog
     * @param stock              the catering stock
     * @param sales              the sold products
     * @param utilsManagement    utilities
     * @param festivalManagement all for the festival
     */
    public CateringStockController(
            CateringProductCatalog catalog,
            CateringStock stock,
            CateringSales sales,
            UtilsManagement utilsManagement,
            FestivalManagement festivalManagement) {
        this.catalog = catalog;
        this.stock = stock;
        this.utilsManagement = utilsManagement;
        this.festivalManagement = festivalManagement;
        this.sales = sales;
        CateringStockItem.festivalManagement = festivalManagement;
    }

    /**
     * the title of the site
     * 
     * @return the title
     */
    @ModelAttribute("title")
    public String getTitle() {
        return "Catering Produkte";
    }

    /**
     * the data for the catering product catalog site
     * 
     * @param model      the data on this page
     * @param festivalId the id of the current festival
     * @return the path to the site
     */
    @GetMapping("/cateringProductCatalog/{festivalId}")
    String products(Model model, @PathVariable Long festivalId) {
        model.addAttribute("stock", stock.findByFestivalId(festivalId));
        model.addAttribute("productcatalog", catalog.findByHidden(false));
        utilsManagement.prepareModel(model, festivalId);
        return "cateringProductCatalog";
    }

    /**
     * the data for the site to add products
     * 
     * @param model      the data of this site
     * @param festivalId the id of the current festival
     * @return the path to this site
     */
    @GetMapping("/cateringAddProduct/{festivalId}")
    String addProduct(Model model, @PathVariable Long festivalId) {
        utilsManagement.prepareModel(model, festivalId);
        return "cateringAddProduct";
    }

    /**
     * the function called to add a product.
     * 
     * @param model        the data of this site
     * @param formularData the data contained in this formular
     * @param festivalId   the id of the current festival
     * @return the path to product catalog site
     */
    @PostMapping("/cateringAddProduct/editData")
    String addProduct(
            Model model,
            ProductFormularData formularData,
            @RequestParam("festivalId") Long festivalId) {
        boolean failure = false;

        if (formularData.name.equals("")) {
            failure = true;
            formularData.name = "[leer]";
        }

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

        double formFilling = 0.0;
        try {
            formFilling = Double.parseDouble(formularData.filling);
        } catch (NumberFormatException ex) {
            failure = true;
        }

        Quantity formMinimumStock = Quantity.of(0);
        try {
            double dMinimumStock = Double.parseDouble(formularData.minimumStock);
            formMinimumStock = Quantity.of(dMinimumStock);
        } catch (NumberFormatException ex) {
            failure = true;
        }

        CateringProduct product = new CateringProduct(
                formularData.name, formPrice, formDeposit,
                formFilling, formMinimumStock, false);

        if (!failure) {
            catalog.save(product);
        } else {
            model.addAttribute("product", product);
        }

        utilsManagement.prepareModel(model, festivalId);
        return (failure) ? "/cateringAddProduct" : "redirect:/cateringProductCatalog/" + festivalId;
    }

    /**
     * the data for the "edit a product" site
     * 
     * @param model      the data on this site
     * @param festivalId the id of the current festival
     * @return the address of this site
     */
    @GetMapping("/cateringEditProduct/{festivalId}")
    String editProduct(Model model, @PathVariable Long festivalId) {
        utilsManagement.prepareModel(model, festivalId);
        return "cateringEditProduct";
    }

    /**
     * the data for this "edit a product" site
     * 
     * @param festivalId the id of the current festival
     * @param productid  the id of this product
     * @param model      the data of this page
     * @return the address of this site
     */
    @GetMapping("/cateringEditProduct/{festivalId}/{productid}")
    String editProduct(
            @PathVariable Long festivalId,
            @PathVariable ProductIdentifier productid,
            Model model) {
        Optional<CateringProduct> oProduct = catalog.findById(productid);
        if (oProduct.isPresent()) {
            CateringProduct product = oProduct.get();
            model.addAttribute("product", product);
        }

        utilsManagement.prepareModel(model, festivalId);
        return "cateringEditProduct";
    }

    /**
     * the function that changes the edited products information
     * 
     * @param productid    the id of the edited product
     * @param model        the data of the site
     * @param formularData the data in the formular
     * @param festivalId   the id of the current festival
     * @return the address of the following site
     */
    @PostMapping("/cateringEditProduct/editData/{productid}")
    String editProductData(
            @PathVariable ProductIdentifier productid,
            Model model,
            ProductFormularData formularData,
            @RequestParam("festivalId") Long festivalId) {
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

            Double formfilling = product.getFilling();
            try {
                formfilling = Double.parseDouble(formularData.filling);
            } catch (NumberFormatException ex) {
                failure = true;
            }

            if (Double.compare(product.getFilling(), formfilling) != 0) {
                changed = true;
                product.setFilling(formfilling);
                System.out.println("Füllmenge:" + formularData.filling);
            }

            double formMinimumStock = product.getMinimumStock().getAmount().doubleValue();
            try {
                formMinimumStock = Double.parseDouble(formularData.minimumStock);
            } catch (NumberFormatException ex) {
                failure = true;
            }

            if (Float.compare((float) product.getMinimumStock().getAmount().doubleValue(),
					(float) formMinimumStock) != 0) {
                changed = true;
                product.setMinimumStock(Quantity.of(formMinimumStock));
            }

            if (changed) {
                catalog.save(product);
            }

        }

        utilsManagement.prepareModel(model, festivalId);
        return (failure) ? "redirect:/cateringEditProduct/" + festivalId + "/" + productid
                : "redirect:/cateringProductCatalog/" + festivalId;
    }

    /**
     * the data for the site to delete a product
     * 
     * @param festivalId the id of the current festival
     * @param productid  the id of this product
     * @param model      the data on this page
     * @return the path to the site
     */
    @GetMapping("/cateringDeleteProduct/{festivalId}/{productid}")
    String deleteProduct(
            @PathVariable Long festivalId,
            @PathVariable ProductIdentifier productid,
            Model model) {
        Optional<CateringProduct> oProduct = catalog.findById(productid);
        if (oProduct.isPresent()) {
            CateringProduct product = oProduct.get();
            model.addAttribute("product", product);
        }
        utilsManagement.prepareModel(model, festivalId);
        return "cateringDeleteProduct";
    }

    /**
     * the function that is really deleting a product
     * 
     * @param productid  the id of the product
     * @param festivalId the id of the current festival
     * @return the address of the following site
     */
    @PostMapping("/cateringDeleteProduct/delete/{productid}")
    String deleteProduct(
            @PathVariable ProductIdentifier productid,
            @RequestParam("festivalId") Long festivalId) {
        Optional<CateringProduct> oProduct = catalog.findById(productid);
        boolean empty = true, empty2 = true;
        if (oProduct.isPresent()) {
            CateringProduct product = oProduct.get();

            InventoryItems<CateringStockItem> iCSI = stock.findByProduct(product);
            empty = iCSI.isEmpty();

            // long lProductId = Long.parseLong(productid.toString());
            Iterator<CateringSalesItem> iSalesItem = sales.findByCateringProduct(product).iterator();
            empty2 = !iSalesItem.hasNext();

            if (empty && empty2) {
                catalog.delete(product);
            } else {
                product.setHidden(true);
                catalog.save(product);
            }
        }
        return "redirect:/cateringProductCatalog/" + festivalId;
    }

    /**
     * the class containg the formular data for an catering product
     */
    class ProductFormularData {
        /** the name of this product */
        String name;
        /** the price of this product */
        String price;
        /** the deposit of this product */
        String deposit;
        /** the filling of the beverage */
        String filling;
        /** the minimum amount of the stock item */
        String minimumStock;

        /**
         * the constructor for this product formular data
         * 
         * @param name         the name of this product
         * @param price        the price of this product
         * @param deposit      the deposit of this product
         * @param filling      the filling of the beverage
         * @param minimumStock the minimum amount of the stock item
         */
        public ProductFormularData(String name, String price, String deposit, String filling, String minimumStock) {
            this.name = name;
            this.price = price;
            this.deposit = deposit;
            this.filling = filling;
            this.minimumStock = minimumStock;
        }
    }

    /**
     * the data for the formular site to add a stock item
     * 
     * @param model      the data of this site
     * @param festivalId the id of the current festival
     * @return the address of this site
     */
    @GetMapping("/cateringAddStockItem/{festivalId}")
    String addStockItem(Model model, @PathVariable Long festivalId) {
        model.addAttribute("productcatalog", catalog.findByHidden(false));
        model.addAttribute("orderdate", LocalDate.now());
        model.addAttribute("bestbeforedate", LocalDate.now().plusYears(2));
        utilsManagement.prepareModel(model, festivalId);
        return "cateringAddStockItem";
    }

    /**
     * the function that really adds a stock item
     * 
     * @param model        the data of this site
     * @param formularData the formular data of the frontend
     * @param festivalId   the id of current festival
     * @return the address of the following site
     */
    @PostMapping("/cateringAddStockItem/editData")
    String addStockItem(
            Model model,
            StockFormularData formularData,
            @RequestParam("festivalId") Long festivalId) {
        boolean failure = false;

        Optional<CateringProduct> oProduct = catalog.findById(formularData.productid);
        CateringProduct product = null;
        if (oProduct.isPresent()) {
            product = oProduct.get();
        } else {
            failure = true;
        }

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

        Quantity formAmount = Quantity.of(0);
        long lAmount = 0;
        try {
            lAmount = Long.parseLong(formularData.amount);
            formAmount = Quantity.of(lAmount);
        } catch (NumberFormatException ex) {
            failure = true;
        }

        if (!failure) {
            CateringStockItem stockitem = new CateringStockItem(festivalId, product,
                    formAmount, lAmount,
                    formBuyingPrice, formOrderDate, formBestBeforeDate);
            stock.save(stockitem);
        } else {
            model.addAttribute("productcatalog", catalog.findByHidden(false));
            model.addAttribute("orderdate", LocalDate.now());
            model.addAttribute("bestbeforedate", LocalDate.now().plusYears(2));
        }
        utilsManagement.prepareModel(model, festivalId);

        return (failure) ? "/cateringAddStockItem" : "redirect:/cateringProductCatalog/" + festivalId;
    }

    /**
     * the data of the site to edit a stock item
     * 
     * @param festivalId  the current festivals id
     * @param stockitemid the id of the stock item
     * @param model       the data of this page
     * @return the address of this site
     */
    @GetMapping("/cateringEditStockItem/{festivalId}/{stockitemid}")
    String editStockItem(
            @PathVariable Long festivalId,
            @PathVariable InventoryItemIdentifier stockitemid,
            Model model) {
        Optional<CateringStockItem> oStockItem = stock.findById(stockitemid);
        if (oStockItem.isPresent()) {
            CateringStockItem stockitem = oStockItem.get();
            model.addAttribute("stockitem", stockitem);
        }
        model.addAttribute("productcatalog", catalog.findByHidden(false));
        utilsManagement.prepareModel(model, festivalId);
        return "cateringEditStockItem";
    }

    /**
     * the function that really changes the stock items data
     * 
     * @param stockitemid  the id of the stockitem
     * @param model        the data on this page
     * @param formularData the formular data with the changes
     * @param festivalId   the id of the current festival
     * @return the address of the following site
     */
    @PostMapping("/cateringEditStockItem/editData/{stockitemid}")
    String editStockItem(
            @PathVariable InventoryItemIdentifier stockitemid,
            Model model,
            StockFormularData formularData,
            @RequestParam("festivalId") Long festivalId) {
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

        BigDecimal bdFormAmount = new BigDecimal(0.0);
        try {
            bdFormAmount = new BigDecimal(formularData.amount);
        } catch (NumberFormatException ex) {
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

                if (!stockitem.getQuantity().getAmount().equals(bdFormAmount)) {
                    changed = true;
                    BigDecimal bdDifference = stockitem.getQuantity().getAmount().subtract(bdFormAmount);
                    double lDifference = bdDifference.doubleValue();
                    Quantity qDifference = Quantity.of(lDifference);
                    stockitem.decreaseQuantity(qDifference);
                }
                if (changed) {
                    stock.save(stockitem);
                }
            } else {
                failure = true;
            }

            // stock.save(stockitem);
        } else {
            model.addAttribute("productcatalog", catalog.findByHidden(false));
            model.addAttribute("orderdate", LocalDate.now());
            model.addAttribute("bestbeforedate", LocalDate.now().plusYears(2));
        }
        utilsManagement.prepareModel(model, festivalId);

        return (failure) ? "cateringEditStockItem/" + festivalId + "/" + stockitemid
                : "redirect:/cateringProductCatalog/" + festivalId;
    }

    /**
     * the site for deleting a stock item
     * 
     * @param festivalId  the id of the current festival
     * @param stockitemid the id of the stock item
     * @param model       the data on this site
     * @return the address of this site
     */
    @GetMapping("/cateringDeleteStockItem/{festivalId}/{stockitemid}")
    String deleteStockItem(
            @PathVariable Long festivalId,
            @PathVariable InventoryItemIdentifier stockitemid,
            Model model) {
        Optional<CateringStockItem> oStockItem = stock.findById(stockitemid);
        if (oStockItem.isPresent()) {
            CateringStockItem stockitem = oStockItem.get();
            model.addAttribute("stockitem", stockitem);
        }

        utilsManagement.prepareModel(model, festivalId);
        return "cateringDeleteStockItem";
    }

    /**
     * the function that really deletes a stock item
     * 
     * @param stockitemid the id of the stock item
     * @param festivalId  the id of the currejnt festival
     * @return the address to the following site
     */
    @PostMapping("/cateringDeleteStockItem/delete/{stockitemid}")
    String deleteStockItem(@PathVariable InventoryItemIdentifier stockitemid,
            @RequestParam("festivalId") Long festivalId) {
        Optional<CateringStockItem> oStockItem = stock.findById(stockitemid);
        if (oStockItem.isPresent()) {
            CateringStockItem stockitem = oStockItem.get();
            stock.delete(stockitem);
        }

        return "redirect:/cateringProductCatalog/" + festivalId;
    }

    /**
     * the class representing the data of a stock item formular
     */
    class StockFormularData {
        /** the id of the stock items product */
        ProductIdentifier productid;
        /** the number of products in this stock item */
        String amount;
        /** the price of buying a product */
        String buyingprice;
        /** the date when this product was bought */
        String orderdate;
        /** the date until this product is fresh */
        String bestbeforedate;

        /**
         * the constructor for this formular data
         * 
         * @param productid      the id of the stock items product
         * @param amount         the number of products in this stock item
         * @param buyingprice    the price of buying a product
         * @param orderdate      the date when this product was bought
         * @param bestbeforedate the date until this product is fresh
         */
        public StockFormularData(
                ProductIdentifier productid,
                String amount,
                String buyingprice,
                String orderdate,
                String bestbeforedate) {
            this.productid = productid;
            this.amount = amount;
            this.buyingprice = buyingprice;
            this.orderdate = orderdate;
            this.bestbeforedate = bestbeforedate;
            // this.orderQuantity = orderQuantity;
        }
    }
}
