package prototype.festival;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FestivalController {

	private final FestivalManagement festivalManagement;
	private Festival currentFestival;

 
	public FestivalController(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
		this.currentFestival = null;
		
	}
	
	@GetMapping("/festivalOverview/{festival}")
	public String festivalDetail(@PathVariable Festival festival, Model model) {
		Streamable<Festival> festivals= festivalManagement.findAll();
		for(Festival aFestival : festivals) {
			System.out.println(aFestival.getId());
			if(aFestival.getId() == festival.getId()) {
				model.addAttribute("festival", aFestival);
				if(aFestival.getLocation() != null) {
					System.out.println(aFestival.getLocation().getName());
					model.addAttribute("location", aFestival.getLocation());
				} 
				break;
			}
		}
		
		
		currentFestival = festival;
		return "festivalDetail";
	}
	
	@PostMapping("/newFestival")
	public String registerNew(@Validated NewFestivalForm form, Errors result) {
		
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
	public String register(Model model, NewFestivalForm form) {
		return "newFestival";
	}
	
	// shows Festival Overview
	@GetMapping("/festivalOverview")
	public String festivals(Model model) {

		model.addAttribute("festivalList", festivalManagement.findAll());

		return "festivalOverview"; 
	}

	@GetMapping("/financesPre1")
	// TODO: @PreAuthorize("hasRole('BOSS')")
	String financesPre1(Model model, RedirectAttributes ra) {
		ra.addFlashAttribute("currentFestival", currentFestival);
		System.out.println(currentFestival.getName());
		return "redirect:finances";
	}
}
