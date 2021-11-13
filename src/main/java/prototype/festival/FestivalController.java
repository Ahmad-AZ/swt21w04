package prototype.festival;

import java.util.Calendar;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import prototype.location.Location;

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
		
		if(festival.getLocation() != null) 
			System.out.println(festival.getLocation().getName());
			
		System.out.println(festival.getId());
		model.addAttribute("festival", festival);
		String startDate = festival.getStartDate().toString();
		startDate = startDate.substring(0, startDate.length()-10);
		String endDate = festival.getEndDate().toString();
		endDate = endDate.substring(0, endDate.length()-10);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		if(festival.getLocation() != null) {
			System.out.println(festival.getLocation().getName());
			model.addAttribute("location", festival.getLocation());
		}

	
		currentFestival = festival;
		return "festivalDetail";
	}
	
	@PostMapping("/newFestival")
	public String createNewFestival(@Validated NewFestivalForm form, Errors result) {
		
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
			return "newFestival";
		}

		// create Festival if no error appears
		festivalManagement.createFestival(form);

		return "redirect:/festivalOverview";
	}
	
	
	
	// gives NewFestivalForm to fill out
	@GetMapping("/newFestival") 
	public String newFestival(Model model, NewFestivalForm form) {
		return "newFestival";
	}
	
	// shows Festival Overview
	@GetMapping("/festivalOverview")
	public String festivals(Model model) {
		
		model.addAttribute("festivalList", festivalManagement.findAll());

		return "festivalOverview"; 
	}
	
	@PostMapping("/selectLocationPre")
	// TODO: @PreAuthorize("hasRole('BOSS')")
	String selectLocationPre(Model model, @RequestParam("location") Location location) {
		
		currentFestival.setLocation(location);
		festivalManagement.saveFestival(currentFestival);
		System.out.println(currentFestival.getName());
		System.out.println(currentFestival.getId());
		System.out.println(currentFestival.getLocation().getName());
		long id = currentFestival.getId();
		return "redirect:/festivalOverview/"+id;
	}
	
	@GetMapping("/financesPre1")
	// TODO: @PreAuthorize("hasRole('BOSS')")
	String financesPre1(Model model, RedirectAttributes ra) {
		ra.addFlashAttribute("currentFestival", currentFestival);
		System.out.println(currentFestival.getName());
		return "redirect:finances";
	}
}
