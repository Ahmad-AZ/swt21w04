package prototype.location;

import java.util.Optional;

import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import prototype.festival.Festival;
import prototype.festival.FestivalManagement;
import prototype.planning.PlanLocation;
import prototype.planning.Planning;

@Controller
public class LocationController {
	
	private final LocationManagement locationManagement;
	private Festival currentFestival;
	private PlanLocation planning;
	private FestivalManagement festivalManagement;
	
	public LocationController(LocationManagement locationManagement) {
		this.locationManagement = locationManagement;
		this.currentFestival = null;
		
	}
	
	// shows Locations Overview
	@GetMapping("/locationOverview")  
	public String locationOverview(Model model, @ModelAttribute("currentFestival") Festival currentFestival, 
			@ModelAttribute("fm") FestivalManagement fm) {
		this.currentFestival = currentFestival;
		this.festivalManagement = fm;
		planning = new PlanLocation(currentFestival, festivalManagement, locationManagement);
		model.addAttribute("locationList", locationManagement.findAll());
		
		// required for second nav-bar
		model.addAttribute("festival", currentFestival);
		
		return "locationOverview"; 
	} 
	
	@GetMapping("/locationOverview/{locationId}")
	public String locationDetail(@PathVariable Long locationId, Model model) {
		Optional<Location> location = locationManagement.findById(locationId);
		
		if (location.isPresent()) {
			Location current = location.get();

			System.out.println(locationId);
			model.addAttribute("location", current);
			
			// required for second nav-bar
			model.addAttribute("festival", currentFestival);
			 
			return "locationDetail";
			
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	 
	@PostMapping("/bookLocation")
	public String selectLocation(@RequestParam("location") Long locationId) {
		Optional<Location> location = locationManagement.findById(locationId);
		
		if (location.isPresent()) {
			Location current = location.get();
			System.out.println(currentFestival.getName());
			planning.bookLocation(current);
			System.out.println(currentFestival.getLocation().getName());
	
			long id = current.getId();
			return "redirect:/locationOverview/"+id;
			
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
}
