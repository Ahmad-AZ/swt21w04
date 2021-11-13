package prototype.location;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocationController {
	
	private final LocationManagement locationManagement;
	
	public LocationController(LocationManagement locationManagement) {
		this.locationManagement = locationManagement;
	}
	
	// shows Locations Overview
	@GetMapping("/locationOverview")
	public String locationOverview(Model model) {

		model.addAttribute("LocationList", locationManagement.findAll());

		return "locationOverview"; 
	}
}
