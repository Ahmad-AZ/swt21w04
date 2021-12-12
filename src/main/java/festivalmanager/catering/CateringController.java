package festivalmanager.catering;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import festivalmanager.festival.*;
import festivalmanager.utils.*;

/**
 * @author Robert Menzel
 */
@Controller
public class CateringController {
	private Festival currentFestival;
	private UtilsManagement utilsManagement;
	private FestivalManagement festivalManagement;
	private CateringProductCatalog catalog;
	private CateringStock stock;
	private CateringSales sales;

	public CateringController(
			CateringProductCatalog catalog,
			CateringStock stock,
			CateringSales sales,
			UtilsManagement utilsManagement,
			FestivalManagement festivalManagement) {
		this.catalog = catalog;
		this.stock = stock;
		this.utilsManagement = utilsManagement;
		this.festivalManagement = festivalManagement;
		CateringSalesItem.festivalManagement = festivalManagement;
		this.sales = sales;
	}

	@GetMapping("/catering")
	String sales(Model model) {
		currentFestival = festivalManagement.findById(utilsManagement.getCurrentFestivalId()).get();
		model.addAttribute("stock", stock.findByFestivalId(currentFestival.getId()));
		model.addAttribute("productcatalog", catalog.findAll());
		model.addAttribute("sales", sales);
		model.addAttribute("productid", null);
		utilsManagement.setCurrentPageLowerHeader("cateringSales");
		utilsManagement.prepareModel(model);
		return "catering";
	}
}
