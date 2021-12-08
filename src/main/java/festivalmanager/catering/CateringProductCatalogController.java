package festivalmanager.catering;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.LongOrNull;
import org.salespointframework.catalog.ProductIdentifier;
import static org.salespointframework.core.Currencies.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

import javax.money.format.MonetaryParseException;

import org.javamoney.moneta.*;

@Controller
public class CateringProductCatalogController {

    private Festival currentFestival;
    private FestivalManagement festivalManagement;
    private CateringProductCatalog catalog;

    public CateringProductCatalogController(CateringProductCatalog catalog, FestivalManagement festivalManagement) {
        this.catalog = catalog;
        this.festivalManagement = festivalManagement;
    }

    public CateringProductCatalog getCatalog() {
        return catalog;
    }

    @GetMapping("/cateringProductCatalog")
    String products(Model model, @ModelAttribute("currentFestivalId") LongOrNull currentFestivalId) {
        if (currentFestivalId.getValue() != null) {
            long longId = currentFestivalId.getValue();
            this.currentFestival = festivalManagement.findById(longId).get();
        }

        model.addAttribute("productcatalog", catalog.findAll());
        model.addAttribute("festival", currentFestival);
        return "cateringProductCatalog";
    }

    @GetMapping("/cateringAddProduct")
    String addProduct(Model model) {
        model.addAttribute("festival", currentFestival);
        return "cateringAddProduct";
    }

    @PostMapping("/cateringAddProduct/editData")
    String addProduct(Model model, FormularData formularData) {
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

        model.addAttribute("festival", currentFestival);

        return (failure) ? "/cateringAddProduct" : "redirect:/cateringProductCatalog";
    }

    @GetMapping("/cateringEditProduct")
    String editProduct(Model model) {
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
        return "cateringEditProduct";
    }

    @PostMapping("/cateringEditProduct/editData/{productid}")
    String editProductData(@PathVariable ProductIdentifier productid, Model model, FormularData formularData) {
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

        model.addAttribute("festival", currentFestival);

        return (failure) ? "redirect:/cateringEditProduct/" + productid : "redirect:/cateringProductCatalog";
    }

    @GetMapping("/cateringDeleteProduct/{productid}")
    String deleteProduct(@PathVariable ProductIdentifier productid, Model model) {
        Optional<CateringProduct> oProduct = catalog.findById(productid);
        if (oProduct.isPresent()) {
            CateringProduct product = oProduct.get();
            model.addAttribute("product", product);
        }

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

    class FormularData {
        String name;
        String price, deposit;
        double filling;

        public FormularData(String name, String price, String deposit, double filling) {
            this.name = name;
            this.price = price;
            this.deposit = deposit;
            this.filling = filling;
        }
    }
}
