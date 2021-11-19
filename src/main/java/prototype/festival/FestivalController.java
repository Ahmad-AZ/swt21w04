package prototype.festival;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import prototype.location.Location;
import prototype.location.LocationManagement;
import prototype.planning.Planning;

@Controller
public class FestivalController {

	private final FestivalManagement festivalManagement;
	private final LocationManagement locationManagement;
	private Festival currentFestival;
	private long currentId;

 
	public FestivalController(FestivalManagement festivalManagement, LocationManagement locationManagement) {
		this.festivalManagement = festivalManagement;
		this.locationManagement = locationManagement;
		this.currentFestival = null;
		this.currentId = 0;
		
	}
	
	@GetMapping("/festivalOverview/{festivalId}")
	public String festivalDetail(@PathVariable Long festivalId, Model model) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		Streamable<Festival> festivals = festivalManagement.findAll();

		if (festival.isPresent()) {
			Festival current = festival.get();

			if (current.getLocation() != null)
				System.out.println(current.getLocation().getName());

			System.out.println(festivalId);
			model.addAttribute("festival", current);
			String startDate = current.getStartDate().toString();
			startDate = startDate.substring(0, startDate.length() - 10);
			String endDate = current.getEndDate().toString();
			endDate = endDate.substring(0, endDate.length() - 10);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
			if (current.getLocation() != null) {
				System.out.println(current.getLocation().getName());
				model.addAttribute("location", current.getLocation());
			}
			currentId = festivalId;
			currentFestival = current;
			return "festivalDetail";
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
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
	String selectLocationPre(Model model, @RequestParam("location") Long locationId) {
		Optional<Location> location = locationManagement.findById(locationId);
		
		if (location.isPresent()) {
			Location current = location.get();
		
			currentFestival.setLocation(current);
			festivalManagement.saveFestival(currentFestival);
			System.out.println(currentFestival.getName());
			System.out.println(currentFestival.getId());
			System.out.println(currentFestival.getLocation().getName());
			long id = currentFestival.getId();
			return "redirect:/festivalOverview/"+id;
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
	@GetMapping("/locationPre1")
	// TODO: @PreAuthorize("hasRole('BOSS')")
	String locationPre1(Model model, RedirectAttributes ra) {
		ra.addFlashAttribute("currentFestival", currentFestival);
		ra.addFlashAttribute("fm", festivalManagement);
		System.out.println(currentFestival.getName());
		return "redirect:locationOverview";
	}
	
	
	
	@GetMapping("/financesPre1")
	// TODO: @PreAuthorize("hasRole('BOSS')")
	String financesPre1(Model model, RedirectAttributes ra) {
		ra.addFlashAttribute("currentFestival", currentFestival);
		System.out.println(currentFestival.getName());
		return "redirect:finances";
	}
}
