package festivalmanager.catering;

import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import festivalmanager.festival.*;
import festivalmanager.utils.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * @author Robert Menzel
 */
@Controller
@SessionAttributes("cart")
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

	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

	@GetMapping("/catering")
	String sales(Model model, @ModelAttribute Cart cart) {
		currentFestival = festivalManagement.findById(utilsManagement.getCurrentFestivalId()).get();
		model.addAttribute("stock", stock.findByFestivalId(currentFestival.getId()));
		model.addAttribute("productcatalog", catalog.findAll());
		model.addAttribute("sales", sales);
		model.addAttribute("productid", null);
		utilsManagement.setCurrentPageLowerHeader("cateringSales");
		utilsManagement.prepareModel(model);
		model.addAttribute("cart", cart);
		return "catering";
	}

	//Daraus PostMapping methode bauen
	/*
	String addItem (Model mode, Hier ReqeustParam für item übergeben, Hier ReqeustParam für amount übergeben, @ModelAttribute Cart cart) {

		cart.addOrUpdateItem(item, Quantity.of(amount));
	}
	*/
}
