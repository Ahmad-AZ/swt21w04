package festivalmanager.location;

import java.util.Optional;

import javax.money.format.MonetaryParseException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.javamoney.moneta.Money;
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

import static org.salespointframework.core.Currencies.EURO;


@Controller
public class LocationController {
	
	private final LocationManagement locationManagement;
	
	public LocationController(LocationManagement locationManagement) {
		this.locationManagement = locationManagement;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Locations";
	}

	@GetMapping("/locations")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String locations(Model model) {
		model.addAttribute("locationList", locationManagement.findAll());
		return "locations";
	}
	
	@GetMapping("/locations/{locationId}/edit")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String locationEdit(@PathVariable Long locationId, Model model) {
		Optional<Location> location = locationManagement.findById(locationId);
		
		if (location.isPresent()) {
			Location current = location.get();
			
			System.out.println(locationId);
			model.addAttribute("location", current);
			model.addAttribute("newLocationForm", current);
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
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
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
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String createNewLocation(@Validated NewLocationForm newLocationForm, Errors result) {
		if (result.hasErrors()) {
			return "newLocation";
		}
		
		Money price;
		try {
			
            price = Money.parse("EUR " + newLocationForm.getPricePerDay());
        } catch (MonetaryParseException ex) {
        	result.rejectValue("pricePerDay", null, "geben Sie einen gültigen Preis ein");
        	return "newLocation";
        }
		
		// Location with same name already exists
		for(Location aLocation : locationManagement.findAll()) {
			if(aLocation.getName().equals(newLocationForm.getName())){
				System.out.println("same name as an already given location");
				result.rejectValue("name", null, "Location mit diesem Namen existiert bereits.");
			}
		}
		
		if(price.isLessThan(Money.of(0, EURO))) {
			result.rejectValue("pricePerDay", null, "muss größer-gleich 0 sein");
			return "newLocation";
		}

		// save Festival if no error appears
		locationManagement.createLocation(newLocationForm);

		return "redirect:/locations";
	}
	
	
	// gives NewLocationForm to fill out
	@GetMapping("/newLocation")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String newLocation(Model model, NewLocationForm newLocationForm) {
		return "newLocation";
	}
	
	@PostMapping("/saveLocation")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String saveLocation(@Validated NewLocationForm form, Errors result, @RequestParam("location") Long locationId, Model model) {
		
		Optional<Location> location = locationManagement.findById(locationId);
		
		if (location.isPresent()) {
			Location current = location.get();
			
			// Location with same name already exists
			if (!current.getName().equals(form.getName())) {
				for (Location aLocation : locationManagement.findAll()) {
					if (aLocation.getName().equals(form.getName())) {
						result.rejectValue("name", null, "Location mit diesem Namen existiert bereits.");
					}

				}
			} else {
				System.out.println("name does not change");
			}
			if (result.hasErrors()) {
				model.addAttribute("location", current);
				model.addAttribute("pricePerDay", current.getPricePerDay().getNumber().toString());
				return "locationEdit";
	        
			}
			
			Money price;
			try {
				
	            price = Money.parse("EUR " + form.getPricePerDay());
	        } catch (MonetaryParseException ex) {
	        	result.rejectValue("pricePerDay", null, "geben Sie einen gültigen Preis ein");
				model.addAttribute("location", current);
				model.addAttribute("pricePerDay", current.getPricePerDay().getNumber().toString());
				return "locationEdit";
	        }

			
			if(price.isLessThan(Money.of(0, EURO))) {
				result.rejectValue("pricePerDay", null, "muss größer-gleich 0 sein");
				model.addAttribute("location", current);
				model.addAttribute("pricePerDay", current.getPricePerDay().getNumber().toString());
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
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
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
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
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
