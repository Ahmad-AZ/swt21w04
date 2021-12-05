package festivalmanager.catering;

import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

@Controller
public class CateringProductCatalogController {
    private CateringProductCatalog catalog;

    public CateringProductCatalogController(CateringProductCatalog catalog) {
        this.catalog = catalog;
    }

    public CateringProductCatalog getCatalog() {
        return catalog;
    }

    @GetMapping("/cateringProductCatalog")
    String products(Model model) {

        model.addAttribute("productcatalog", catalog.findAll());
        return "cateringProductCatalog";
    }

    @GetMapping("/cateringAddProduct")
    String addProduct(Model model) {
        return "cateringAddProduct";
    }

    @GetMapping("/cateringEditProduct")
    String editProduct(Model model) {
        return "cateringEditProduct";
    }

    @GetMapping("/cateringEditProduct/{productid}")
    String editProduct(@PathVariable ProductIdentifier productid, Model model) {

        System.out.println("productid:" + productid);
        Optional<CateringProduct> oProduct = catalog.findById(productid);
        if (oProduct.isPresent()) {
            CateringProduct product = oProduct.get();
            model.addAttribute("product", product);
        }

        return "cateringEditProduct";
    }

    @PostMapping("/cateringEditProduct/editData")
    String editProductData(Model model, CateringProduct formularData) {

        System.out.println("Name:" + formularData.getName());
        return "redirect:/cateringProductCatalog";
    }
}
