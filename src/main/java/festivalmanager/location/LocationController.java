package festivalmanager.location;

import static org.salespointframework.core.Currencies.EURO;

import java.util.Optional;

import javax.money.format.MonetaryParseException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.javamoney.moneta.Money;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * A class used to pass on values computed in {@link LocationManagement}
 * to the locations.html, locationDetail.html and pages to create or change attributes
 * 
 * @author Adrian Scholze
 */
@Controller
public class LocationController {
	
	private final LocationManagement locationManagement;
	
	/**
	 * Creates a new {@link LocationController} with the given {@link LocationManagement}
	 *
	 * @param locationManagement must not be {@literal null}.

	 */
	public LocationController(LocationManagement locationManagement) {
		Assert.notNull(locationManagement, "LocationManagement must not be null!");
		this.locationManagement = locationManagement;
	}

	/**
	 * Used to pass on the title of the location page to the page header
	 * 
	 * @return title attribute for the location tab
	 */
	@ModelAttribute("title")
	public String getTitle() {
		return "Locations";
	}

	/**
	 * Generates a page which shows an overview about all existing {@link Location}s
	 * 
	 * @param model
	 * @return the locations page
	 */
	@GetMapping("/locations")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String locations(Model model) {
		model.addAttribute("locationList", locationManagement.findAll());
		return "locations";
	}
	
	/**
	 * Generates a page which has input fields to change parameters of the chosen {@link Location}
	 * 
	 * @param locationId
	 * @param model
	 * @return the edit location page for the location belonging to locationId
	 */
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
		}
		return "locationEdit";
			
	}
	
	/**
	 * Generates a page which shows basic informations for the current {@link Location}
	 * 
	 * @param locationId
	 * @param model
	 * @return the location detail page for the location belonging to locationId
	 */
	@GetMapping("/locations/{locationId}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String locationDetail(@PathVariable Long locationId, Model model) {
		Optional<Location> location = locationManagement.findById(locationId);
		
		if (location.isPresent()) {
			Location current = location.get();

			model.addAttribute("location", current);
			model.addAttribute("hasBookings", current.hasBookings());
		}
		return "locationDetail";
		
	}
	
	/**
	 * Takes given {@link NewLocationForm} and call methods to create a new {@link Location} instance.
	 * Rejects errors if {@link Errors} occur.
	 * 
	 * @param newLocationForm
	 * @param result
	 * @return if no error occur the locations page, else the new location page
	 */
	@PostMapping("/newLocation")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String createNewLocation(@Validated NewLocationForm newLocationForm, Errors result) {
		if (!result.hasErrors()) {		
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
					result.rejectValue("name", null, "Location mit diesem Namen existiert bereits.");	
				}
			}
			if(price.isLessThan(Money.of(0, EURO))) {
				result.rejectValue("pricePerDay", null, "muss größer-gleich 0 sein");	
			}
		}
		if (result.hasErrors()) {
			return "newLocation";
		} else {
			// save Festival if no error appears
			locationManagement.createLocation(newLocationForm);
			return "redirect:/locations";
		}
	}
	
	/**
	 * Generates a page which contains a {@link NewLocationForm} to fill out.
	 * Access only for admin, planner and manager.
	 * 
	 * @param model
	 * @param newLocationForm
	 * @return the new location page
	 */
	@GetMapping("/newLocation")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String newLocation(Model model, NewLocationForm newLocationForm) {
		return "newLocation";
	}
	
	/**
	 * Takes given {@link NewLocationForm} and call method to edit {@link Locations}s paramters.
	 * Rejects errors if {@link Errors} occur.
	 * 
	 * @param locationId
	 * @param form
	 * @param result
	 * @param model 
	 * @return the locations page
	 */
	@PostMapping("/locations/{locationId}/edit/saveLocation")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String saveLocation(@Validated NewLocationForm form,
							   Errors result, @RequestParam("location") Long locationId, Model model) {
		
		Optional<Location> location = locationManagement.findById(locationId);
		if (!location.isPresent()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
		Location current = location.get();	
		if (!result.hasErrors()) {
        			
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
			}
			// Location with same name already exists
			for(Location aLocation : locationManagement.findAll()) {
				if(aLocation.getName().equals(form.getName()) && !aLocation.getName().equals(current.getName())){
					result.rejectValue("name", null, "Location mit diesem Namen existiert bereits.");	
				}
			}
		}
		if(!result.hasErrors()) {
			locationManagement.editLocation(current, form);
			return "redirect:/locations";
		}
		
		// result has errors
		model.addAttribute("location", current);
		model.addAttribute("pricePerDay", current.getPricePerDay().getNumber().toString());
		return "locationEdit";
			
	}
	
	/**
	 * Handles exception belonging to to large image files, and redirect user hint 
	 * 
	 * @param httpRequest
	 * @param e
	 * @param ra
	 * @return the new location or location edit page for the {@link Location}
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String catchException(HttpServletRequest httpRequest, MaxUploadSizeExceededException e, RedirectAttributes ra) {
		ra.addFlashAttribute("message", "Bilder sind zu groß.");
		String url = httpRequest.getRequestURL().toString();
		if(url.contains("/saveLocation")) {
		    return "redirect:"+httpRequest.getRequestURL().toString().replaceAll("/saveLocation","");
		}
		if(url.contains("/newLocation")) {
		    return "redirect:/newLocation";
		}
		return "redirect:/error";
	}
	
	/**
	 * Generates a page which shows an overview about all existing {@link Locations}s
	 * and has an dialog to confirm the deletion of the {@link Location} instance.
	 * 
	 * @param id
	 * @param model
	 * @return the locations page with the dialog
	 */
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
	
	/**
	 * Call methods to delete {@link Location} instance with the given id if it has no {@link Booking}s, 
	 * and returns the locations page
	 * 
	 * @param festivalId
	 * @return locations page
	 */
	@PostMapping("/locations/remove")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String removeLocation(@Valid @RequestParam("id") @NotEmpty Long locationId,
								 RedirectAttributes ra) {
		Optional<Location> current = locationManagement.findById(locationId);
		if (current.isPresent()) {
			if(current.get().hasBookings()) {
				//result.rejectValue("delete", null, "Die Location ist noch gebucht.");
				ra.addFlashAttribute("message", "Die Location ist noch gebucht.");
				return "redirect:/locations/remove/"+ locationId;
			} else {
				locationManagement.removeLocation(locationId);
			}
		} 
		return "redirect:/locations";
	}
	
}
