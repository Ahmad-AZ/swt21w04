package festivalmanager.planning;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.location.Location;
import festivalmanager.location.LocationManagement;
import festivalmanager.utils.UtilsManagement;

/**
 * A class used to pass on values computed in {@link festivalmanager.planning.PlanLocationManagement}
 * to the locationOverview.html and locationDetailPlan.html
 * 
 * @author Adrian Scholze
 */
@Controller
public class PlanLocationController {
	
	private final PlanLocationManagement planLocationManagement;
	private final LocationManagement locationManagement;
	private final FestivalManagement festivalManagement;
	private final UtilsManagement utilsManagement;
	
	/**
	 * Creates a new {@link PlanLocationController} with the given {@link FestivalManagement}, {@link UtilsManagement},
	 * {@link LocationManagment} and {@link PlanLocationManagement}.
	 *
	 * @param festivalManagement must not be {@literal null}.
	 * @param utilsManagement must not be {@literal null}.
	 * @param locationManagement must not be {@literal null}.
	 * @param planLocationManagement must not be {@literal null}.
	 */
	public PlanLocationController(PlanLocationManagement planLocationManagement, 
									LocationManagement locationManagement, 
									FestivalManagement festivalManagement,
								    UtilsManagement utilsManagement) {
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");
		Assert.notNull(utilsManagement, "UtilsManagement must not be null!");
		Assert.notNull(locationManagement, "LocationManagement must not be null!");
		Assert.notNull(planLocationManagement, "PlanLocationManagement must not be null!");
		this.planLocationManagement = planLocationManagement;
		this.locationManagement = locationManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
	}

	/**
	 * Used to pass on the title of the location overview page to the page header
	 * 
	 * @return title attribute for the location overview tab
	 */
	@ModelAttribute("title")
	public String getTitle() {
		return "Location-Auswahl";
	}
	
	/**
	 * Generates a page which shows an overview about all existing {@link Location}s
	 * the booked one (if exists) is marked
	 * 
	 * @param model
	 * @param festivalId
	 * @return the location overview page
	 */
	@GetMapping("/locationOverview/{festivalId}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String locationOverview(Model model, @PathVariable("festivalId") long festivalId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			Location bookedLocation = current.getLocation();
			model.addAttribute("locationList", locationManagement.findAll());
			if(bookedLocation != null) {
				model.addAttribute("bookedLocationId", bookedLocation.getId());
			} else {
				model.addAttribute("bookedLocationId", 0);
			}
			model.addAttribute("festival", current);
		}
		utilsManagement.prepareModel(model, festivalId);
		return "/locationOverview"; 			
	} 
	
	/**
	 * Generates a page which shows basic informations for the current {@link Location}
	 * 
	 * @param festivalId
	 * @param locationId
	 * @param model
	 * @return the location detail plan page for the location belonging to locationId
	 */
	@GetMapping("/locationOverview/{festivalId}/detail/{locationId}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String locationDetail(@PathVariable("festivalId") long festivalId, 
									@PathVariable("locationId") Long locationId, Model model) {
		Optional<Location> location = locationManagement.findById(locationId);
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		
		if (location.isPresent() && festival.isPresent()) {
			Location currentLocation = location.get();
			Festival currentFestival = festival.get();

			model.addAttribute("location", currentLocation);
			model.addAttribute("hasBookings", currentLocation.hasBookings());			
			
			// to hide book Button if Location is booked
			if (currentFestival.getLocation() != null) {
				model.addAttribute("currentlyBooked", currentFestival.getLocation().getId() == currentLocation.getId());
			} else {
				model.addAttribute("currentlyBooked", false);
			}
		}
		utilsManagement.prepareModel(model, festivalId);
		return "locationDetailPlan";
	}
	
	/**
	 * Call methods to book the current {@link Location} for the {@link Festival} if it is free.
	 * 
	 * @param festivalId
	 * @param locationId
	 * @param currentlyBooked
	 * @param ra
	 * @return location overview page
	 */
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
			
			// unbook currently booked location
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
			}
		}
		// reload locationOverview page
		return "redirect:/locationOverview/" + festivalId;
	}
	
	/**
	 * Call methods to unbook the {@link Location} from the {@link Festival}
	 * 
	 * @param festivalId
	 * @return location overview page
	 */
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
		}
		// reload locationOverview page
		return "redirect:/locationOverview/" + festivalId;
	}
	
	
}
