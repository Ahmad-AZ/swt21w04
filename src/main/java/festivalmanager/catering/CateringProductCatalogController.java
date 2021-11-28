package festivalmanager.catering;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CateringProductCatalogController {
    private CateringProductCatalog catalog;

    public CateringProductCatalogController(CateringProductCatalog catalog) {
        this.catalog = catalog;
    }

    public CateringProductCatalog getCatalog() {
        return catalog;
    }

    @GetMapping("/cateringproductcatalog.html")
    String products(Model model) {

        model.addAttribute("productcatalog", catalog.findAll());
        return "cateringproductcatalog";
    }

}
