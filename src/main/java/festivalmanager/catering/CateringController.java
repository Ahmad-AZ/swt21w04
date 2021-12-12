package festivalmanager.catering;

import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.salespointframework.catalog.ProductIdentifier;
import festivalmanager.festival.*;
import festivalmanager.utils.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Optional;

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

	@PostMapping("/catering/addToCart")
	String addToCart(Model model, AddToCartFormResult formResult, @ModelAttribute Cart cart) {
		System.out.println(formResult.productId);
		System.out.println(formResult.productCount);

		Optional<CateringProduct> oProduct = catalog.findById(formResult.productId);
		if (oProduct.isPresent()) {
			CateringProduct product = oProduct.get();
			cart.addOrUpdateItem(product, Quantity.of(formResult.productCount));
		}
		return "redirect:/catering";
	}

	class AddToCartFormResult {
		ProductIdentifier productId;
		long productCount;

		public AddToCartFormResult(ProductIdentifier productId, long productCount) {
			this.productId = productId;
			this.productCount = productCount;
		}
	}

}
