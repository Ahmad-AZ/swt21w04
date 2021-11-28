package festivalmanager.planning;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import festivalmanager.festival.Festival;
import festivalmanager.location.Location;
import festivalmanager.location.LocationManagement;

@Controller
public class PlanLocationController {

	private final PlanLocationManagement planLocationManagement;
	private final LocationManagement locationManagement;
	private Festival currentFestival;

	public PlanLocationController(PlanLocationManagement planLocationManagement,
			LocationManagement locationManagement) {
		this.planLocationManagement = planLocationManagement;
		this.locationManagement = locationManagement;
		this.currentFestival = null;

	}

	// shows Locations Overview
	@GetMapping("/locationOverview")
	public String locationOverview(Model model, @ModelAttribute("currentFestival") Festival currentFestival) {
		this.currentFestival = currentFestival;
		Location bookedLocation = currentFestival.getLocation();
		model.addAttribute("locationList", locationManagement.findAll());
		if (bookedLocation != null) {
			model.addAttribute("bookedLocationId", bookedLocation.getId());
		} else {
			model.addAttribute("bookedLocationId", 0);
		}

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
			System.out.println(current.getImage());
			System.out.println(current.getGroundView());
			model.addAttribute("location", current);
			System.out.println(current.getBookings().toString());

			// to hide book Button if Location is booked
			if (currentFestival.getLocation() != null) {
				model.addAttribute("currentlyBooked", currentFestival.getLocation().getId() == current.getId());
			} else {
				model.addAttribute("currentlyBooked", false);
			}

			// required for second nav-bar
			model.addAttribute("festival", currentFestival);

			return "locationDetail";

		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	// @PostMapping("/bookLocation")
	// public String bookLocation(@RequestParam("location") Long locationId,
	// RedirectAttributes ra) {
	// Optional<Location> location = locationManagement.findById(locationId);
	// if (location.isPresent()) {
	// Location current = location.get();
	// System.out.println(currentFestival.getName());
	// boolean success = planLocationManagement.bookLocation(current,
	// currentFestival);
	// System.out.println(success);
	//// System.out.println(currentFestival.getLocation().getName());
	// if(!success) {
	// ra.addFlashAttribute("message", "Location ist im Festivalzeitraum belegt");
	// return "redirect:/locationOverview/"+ current.getId();
	// }
	//
	// long id = current.getId();
	// // reload locationOverview page
	// return "redirect:/locationPre1";
	//
	// } else {
	// throw new ResponseStatusException(
	// HttpStatus.NOT_FOUND, "entity not found"
	// );
	// }
	// }

	@PostMapping("/bookLocation")
	public String bookLocation(@RequestParam("location") Long locationId,
			@RequestParam("currentlyBooked") boolean currentlyBooked, RedirectAttributes ra) {
		Optional<Location> location = locationManagement.findById(locationId);
		if (location.isPresent()) {
			Location current = location.get();

			// unbook curetnly booked location
			System.out.println("currentlyBooked boolean:" + currentlyBooked);
			if (currentlyBooked) {
				planLocationManagement.unbookLocation(current, currentFestival);
			}
			// book Location
			else {
				boolean success = planLocationManagement.bookLocation(current, currentFestival);
				System.out.println("success:" + success);

				if (!success) {
					ra.addFlashAttribute("message", "Location ist im Festivalzeitraum belegt");
					return "redirect:/locationOverview/" + current.getId();
				}
				System.out.println("actually booked location:" + currentFestival.getLocation().getName());
			}

			// long id = current.getId();
			// reload locationOverview page
			return "redirect:/locationPre1";

		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	@GetMapping("/locationOverview/unbook")
	public String unbookLocation() {
		Optional<Location> location = locationManagement.findById(currentFestival.getLocation().getId());
		if (location.isPresent()) {
			Location current = location.get();
			planLocationManagement.unbookLocation(current, currentFestival);

			// reload locationOverview page
			return "redirect:/locationPre1";
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found");
		}
	}
}
