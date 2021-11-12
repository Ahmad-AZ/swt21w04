package prototype.festival;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FestivalController {

	private final FestivalManagement festivalManagement;

	public FestivalController(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
	}
	
//	@GetMapping("/festivals/{disc}")
//	String detail(@PathVariable Disc disc, Model model, CommentAndRating form) {
//
//		var quantity = inventory.findByProductIdentifier(disc.getId()) //
//				.map(InventoryItem::getQuantity) //
//				.orElse(NONE);
//
//		model.addAttribute("disc", disc);
//		model.addAttribute("quantity", quantity);
//		model.addAttribute("orderable", quantity.isGreaterThan(NONE));
//
//		return "detail";
//	}
//	
	@GetMapping("/festivalCatalog")
	public String festivals(Model model) {

		model.addAttribute("festivalList", festivalManagement.findAll());

		return "festivals"; 
	}
}
