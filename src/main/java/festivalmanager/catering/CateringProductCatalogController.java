package festivalmanager.catering;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.LongOrNull;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import org.javamoney.moneta.Money;

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
        CateringProduct product = new CateringProduct(formularData.name, formularData.price, formularData.deposit,
                formularData.filling);
        catalog.save(product);
		model.addAttribute("festival", currentFestival);
        return "redirect:/cateringProductCatalog";
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

        Optional<CateringProduct> oProduct = catalog.findById(productid);
        if (oProduct.isPresent()) {
            CateringProduct product = oProduct.get();
            boolean changed = false;

            if (!product.getName().equals(formularData.name)) {
                changed = true;
                product.setName(formularData.name);
                System.out.println("Name:" + formularData.name);
            }

            if (!product.getPrice().equals(formularData.price)) {
                changed = true;
                product.setPrice(formularData.price);
                System.out.println("Preis:" + formularData.price);
            }

            if (!product.getDeposit().equals(formularData.deposit)) {
                changed = true;
                product.setDeposit(formularData.deposit);
                System.out.println("Pfand:" + formularData.deposit);
            }

            if (product.getFilling() != formularData.filling) {
                changed = true;
                product.setFilling(formularData.filling);
                System.out.println("Füllmenge:" + formularData.filling);
            }

            if (changed) {
                catalog.save(product);
            }

        }
		model.addAttribute("festival", currentFestival);
        return "redirect:/cateringProductCatalog";
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
        Money price, deposit;
        double filling;

        public FormularData(String name, Money price, Money deposit, double filling) {
            this.name = name;
            this.price = price;
            this.deposit = deposit;
            this.filling = filling;
        }
    }
}
