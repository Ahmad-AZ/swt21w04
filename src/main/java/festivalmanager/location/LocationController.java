package festivalmanager.location;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import festivalmanager.staff.Person;
import festivalmanager.staff.forms.RemoveStaffForm;

@Controller
public class LocationController {
	
	private final LocationManagement locationManagement;
	
	public LocationController(LocationManagement locationManagement) {
		this.locationManagement = locationManagement;
	}
	
	
	@GetMapping("/locations")
	public String locations(Model model) {
		model.addAttribute("locationList", locationManagement.findAll());
		return "locations";
	}
	
	@GetMapping("/locations/{locationId}/edit")
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
	
	@GetMapping("/locations/{locationId}") 
	public String locationDetail(@PathVariable Long locationId, Model model) {
		Optional<Location> location = locationManagement.findById(locationId);
		
		if (location.isPresent()) {
			Location current = location.get();

			model.addAttribute("location", current);
			model.addAttribute("hasBookings", current.hasBookings());			
			 
			return "locationDetail";
			
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
	@PostMapping("/newLocation")
	public String createNewLocation(@Validated NewLocationForm form, Errors result) {
		
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
	public String saveLocation(@Validated NewLocationForm form, Errors result, @RequestParam("location") Long locationId, Model model) {
		
		Optional<Location> location = locationManagement.findById(locationId);
		
		if (location.isPresent()) {
			Location current = location.get();
			if (result.hasErrors()) {
				System.out.println("form has errors");
				model.addAttribute("location", current);
				Double pricePerDay = current.getPricePerDay().getNumber().doubleValue();
				System.out.println(pricePerDay);
				model.addAttribute("pricePerDay", pricePerDay);
				return "locationEdit";
			}
			
			locationManagement.editLocation(current, form);
			return "redirect:/locations";
			
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
	@GetMapping("locations/remove/{id}")
	public String getRemoveLocationDialog(@PathVariable("id") long id, Model model) {
		model.addAttribute("locations", locationManagement.findAll());
		model.addAttribute("currentId", id);
		model.addAttribute("dialog", "remove_location");

		Optional<Location> current = locationManagement.findById(id);
		if (current.isPresent()) {
			model.addAttribute("currentName", current.get().getName());
			model.addAttribute("locationHasBookings", current.get().hasBookings());

		} else {
			model.addAttribute("currentName", "");
		}

		return "/locations";
	}
	
	@PostMapping("/locations/remove")
	public String removeLocation(@Valid @RequestParam("id") @NotEmpty Long locationId, @ModelAttribute("delete") String dummy, Errors result) {
		Optional<Location> current = locationManagement.findById(locationId);
		if (current.isPresent()) {
			if(current.get().hasBookings()) {
				result.rejectValue("delete", null, "Die Location ist noch gebucht.");
				return "/locations/remove/"+ locationId;
			} else {
				locationManagement.removeLocation(locationId);
			}
		} 
		return "redirect:/locations";
	}
	
}
