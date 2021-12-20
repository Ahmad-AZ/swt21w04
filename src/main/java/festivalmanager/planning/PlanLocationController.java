package festivalmanager.planning;

import java.util.Optional;

import festivalmanager.utils.UtilsManagement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.location.Location;
import festivalmanager.location.LocationManagement;

@Controller
public class PlanLocationController {
	
	private final PlanLocationManagement planLocationManagement;
	private final LocationManagement locationManagement;
	private final FestivalManagement festivalManagement;
	private final UtilsManagement utilsManagement;
	private Festival currentFestival;
	private long currentFestivalId;
	
	public PlanLocationController(PlanLocationManagement planLocationManagement, 
									LocationManagement locationManagement, 
									FestivalManagement festivalManagement,
								    UtilsManagement utilsManagement) {
		this.planLocationManagement = planLocationManagement;
		this.locationManagement = locationManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
		this.currentFestival = null;
		this.currentFestivalId = 0;
		
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Location-Auswahl";
	}

	// shows Locations Overview
	@GetMapping("/locationOverview/{festivalId}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String locationOverview(Model model, @PathVariable("festivalId") long festivalId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			this.currentFestival = current;
			this.currentFestivalId = festivalId;
			Location bookedLocation = current.getLocation();
			model.addAttribute("locationList", locationManagement.findAll());
			if(bookedLocation != null) {
				model.addAttribute("bookedLocationId", bookedLocation.getId());
			} else {
				model.addAttribute("bookedLocationId", 0);
			}
			
			model.addAttribute("festival", current);
			
			utilsManagement.setCurrentFestivalId(currentFestival.getId());
			utilsManagement.setCurrentPageLowerHeader("location");
			utilsManagement.prepareModel(model);
			return "/locationOverview"; 
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
				

	} 
		
	@GetMapping("/locationOverview/{festivalId}/detail/{locationId}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String locationDetail(@PathVariable("festivalId") long festivalId, 
									@PathVariable("locationId") Long locationId, Model model) {
		Optional<Location> location = locationManagement.findById(locationId);
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		
		if (location.isPresent() && festival.isPresent()) {
			Location currentLocation = location.get();
			Festival currentFestival = festival.get();
			
			System.out.println(locationId);
			System.out.println(currentLocation.getImage());
			System.out.println(currentLocation.getGroundView());
			model.addAttribute("location", currentLocation);
			model.addAttribute("hasBookings", currentLocation.hasBookings());
			System.out.println(currentLocation.hasBookings());
			System.out.println(currentLocation.getBookings());				
			
			// to hide book Button if Location is booked
			if (currentFestival.getLocation() != null) {
				model.addAttribute("currentlyBooked", currentFestival.getLocation().getId() == currentLocation.getId());
			} else {
				model.addAttribute("currentlyBooked", false);
			}

			// required for second nav-bar
			model.addAttribute("festival", currentFestival);

			utilsManagement.prepareModel(model);
			return "locationDetailPlan";
			
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
		 		
	@PostMapping("/locationOverview/{festivalId}/detail/{locationId}/bookLocation")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String bookLocation(@PathVariable("festivalId") long festivalId, 
								@PathVariable("locationId") Long locationId, 
								@RequestParam("currentlyBooked") boolean currentlyBooked, RedirectAttributes ra) {
		Optional<Location> location = locationManagement.findById(locationId);
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		
		if (location.isPresent() && festival.isPresent()) {
			Location currentLocation = location.get();
			Festival currentFestival = festival.get();
			
			// unbook curetnly booked location
			System.out.println("currentlyBooked boolean:"+currentlyBooked);
			if(currentlyBooked) {
				planLocationManagement.unbookLocation(currentLocation, currentFestival);
			} else {
				// book Location
				boolean success = planLocationManagement.bookLocation(currentLocation, currentFestival);
				System.out.println("success:"+success);

				if(!success) {
					ra.addFlashAttribute("message", "Location ist im Festivalzeitraum belegt");
					return "redirect:/locationOverview/"+ festivalId + "/detail/" + locationId;
				}
				System.out.println("actually booked location:" + currentFestival.getLocation().getName());
			}
	

			// reload locationOverview page
			return "redirect:/locationOverview/" + festivalId;
			
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
	@GetMapping("/locationOverview/{festivalId}/unbook")
	public String unbookLocation(@PathVariable("festivalId") long festivalId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		
		if (festival.isPresent()) {
			Festival currentFestival = festival.get();
			Optional<Location> location = locationManagement.findById(currentFestival.getLocation().getId());
			if(location.isPresent()){
				Location currentLocation = location.get();
				planLocationManagement.unbookLocation(currentLocation, currentFestival);
			}

			// reload locationOverview page
			return "redirect:/locationOverview/" + festivalId;
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
}
