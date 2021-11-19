package prototype.location;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import prototype.festival.Festival;

@Controller
public class LocationController {
	
	private final LocationManagement locationManagement;
	
	
	public LocationController(LocationManagement locationManagement) {
		this.locationManagement = locationManagement;
	}
	
	// shows Locations Overview
	@GetMapping("/locationOverview")  
	public String locationOverview(Model model, @ModelAttribute("currentFestival") Festival currentFestival) {
		
		model.addAttribute("locationList", locationManagement.findAll());
		
		// required for second nav-bar
		model.addAttribute("festival", currentFestival);
		
		return "locationOverview"; 
	} 
	 
//	@GetMapping("/selectLocation")
//	public String selectLocation(Model model, @ModelAttribute("currentFestival") Festival currentFestival, @ModelAttribute("location") Location location) {
//		System.out.println(currentFestival.getName());
//		currentFestival.setLocation(location);
//		System.out.println(currentFestival.getLocation().getName());
//
//		long id = currentFestival.getId();
//		
//		return "redirect:/festivalOverview/"+id;
//	}
}
