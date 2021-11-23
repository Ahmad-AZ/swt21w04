package festivalmanager.location;

import java.util.Optional;

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

@Controller
public class LocationController {
	
	private final LocationManagement locationManagement;
	//private Festival currentFestival;
	//private PlanLocation planning;
	//private FestivalManagement festivalManagement;
	
	public LocationController(LocationManagement locationManagement) {
		this.locationManagement = locationManagement;
		//this.currentFestival = null;
		
	}
	
	
	@GetMapping("/locations")
	public String locations(Model model) {
		model.addAttribute("locationList", locationManagement.findAll());
		return "locations";
	}
	
	@GetMapping("/locations/{locationId}")
	public String locationEdit(@PathVariable Long locationId, Model model) {
		Optional<Location> location = locationManagement.findById(locationId);
		
		if (location.isPresent()) {
			Location current = location.get();
			
			System.out.println(locationId);
			model.addAttribute("location", current);
			Double pricePerDay = current.getPricePerDay().getNumber().doubleValue();
			System.out.println(pricePerDay);
			model.addAttribute("pricePerDay", pricePerDay);
			return "locationEdit";
			
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
	@PostMapping("/newLocation")
	public String createNewLocation(@Validated NewLocationForm form, Errors result) {
		
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
			return "newLocation";
		}

		// create Festival if no error appears
		locationManagement.createLocation(form);

		return "redirect:/locations";
	}
	
	
	
	// gives NewLocationForm to fill out
	@GetMapping("/newLocation") 
	public String newLocation(Model model, NewLocationForm form) {
		return "newLocation";
	}
	
	@PostMapping("/saveLocation")
	public String saveLocation(@Validated NewLocationForm form, Errors result, @RequestParam("location") Long locationId) {
		Optional<Location> location = locationManagement.findById(locationId);
		
		if (location.isPresent()) {
			Location current = location.get();
			locationManagement.editLocation(current, form);
			return "redirect:/locations";
			
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
}
