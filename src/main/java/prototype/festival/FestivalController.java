package prototype.festival;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
	@PostMapping("/newFestival")
	String registerNew(@Validated NewFestivalForm form, Errors result) {
		
//		Streamable<F> customers = customerManagement.findAll();
//		for (Customer customer : customers) {
//			if (form.getName().equals(customer.getUserAccount().getUsername())) {
//				result.rejectValue("name", null, "Benutzername bereits vergeben");
//			}
//		}

//		if (!form.getPassword().equals(form.getPasswordReputation())) {
//			result.rejectValue("passwordReputation", null, "Passwörter stimmen nicht überein.");
//
//		}

		if (result.hasErrors()) {
			return "register";
		}

		// create Festival if no error appears
		festivalManagement.createFestival(form);

		return "redirect:/festivalOverview";
	}
	
	
	
	// gives NewFestivalForm to fill out
	@GetMapping("/newFestival")
	String register(Model model, NewFestivalForm form) {
		return "newFestival";
	}
	
	// shows Festival Overview
	@GetMapping("/festivalOverview")
	public String festivals(Model model) {

		model.addAttribute("festivalList", festivalManagement.findAll());

		return "festivalOverview"; 
	}
}
