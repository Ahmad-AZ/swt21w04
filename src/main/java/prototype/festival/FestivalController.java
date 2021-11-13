package prototype.festival;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FestivalController {

	private final FestivalManagement festivalManagement;


	public FestivalController(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
		
	}
	
	@GetMapping("/festivalOverview/{festival}")
	public String festivalDetail(@PathVariable Festival festival, Model model) {
		Streamable<Festival> festivals= festivalManagement.findAll();
		for(Festival aFestival : festivals) {
			System.out.println(aFestival.getId());
			if(aFestival.getId() == festival.getId()) {
				model.addAttribute("festival", aFestival);
				break;
			}
		}
		

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
}
