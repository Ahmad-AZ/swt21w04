package festivalmanager.catering;

import festivalmanager.festival.*;
import festivalmanager.utils.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

/**
 * @author Robert Menzel
 */
@Controller
public class CateringSalesController {

    private Festival currentFestival;
    private UtilsManagement utilsManagement;
    private FestivalManagement festivalManagement;
    private CateringProductCatalog catalog;
    private CateringStock stock;

    public CateringSalesController(CateringProductCatalog catalog, CateringStock stock,
            UtilsManagement utilsManagement, FestivalManagement festivalManagement) {
        this.catalog = catalog;
        this.stock = stock;
        this.utilsManagement = utilsManagement;
        this.festivalManagement = festivalManagement;
        CateringSalesItem.festivalManagement = festivalManagement;
    }

    @GetMapping("/cateringSales")
    String sales(Model model) {
        currentFestival = festivalManagement.findById(utilsManagement.getCurrentFestivalId()).get();
        model.addAttribute("stock", stock.findByFestivalId(currentFestival.getId()));
        model.addAttribute("productcatalog", catalog.findAll());
        utilsManagement.setCurrentPageLowerHeader("catering");
        utilsManagement.prepareModel(model);
        return "cateringProductCatalog";
    }
}
