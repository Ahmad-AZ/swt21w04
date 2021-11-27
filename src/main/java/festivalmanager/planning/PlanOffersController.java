package festivalmanager.planning;

import festivalmanager.festival.Festival;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.HiringManagement;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Controller
public class PlanOffersController {
	private final PlanOffersManagement planOffersManagement;
	private final HiringManagement hiringManagement;
	private Festival currentFestival;

	public PlanOffersController(PlanOffersManagement planOffersManagement, HiringManagement hiringManagement) {
		this.planOffersManagement = planOffersManagement;
		this.hiringManagement = hiringManagement;
		this.currentFestival = null;
	}
	@GetMapping("/artistOverview")
	public String artistOverview(Model model, @ModelAttribute("currentFestival") Festival currentFestival) {
		this.currentFestival = currentFestival;
		model.addAttribute("artistList", hiringManagement.findAll());
//		System.out.println(currentFestival.artistsIsEmpty());
//		if (!currentFestival.artistsIsEmpty()) {
//			System.out.println("nafsd");
//		}
////			Iterator<Artist> iterator = currentFestival.getArtist().iterator();
////			while (iterator.hasNext()) {
////				model.addAttribute("bookedArtistId",iterator.next().getId());
////			}
////		}
//		else{
//			model.addAttribute("bookedArtistId", 0);
//
//		}
		// required for second nav-bar
		model.addAttribute("festival", currentFestival);

		return "artistOverview";
	}
	@GetMapping("/artistOverview/{artistId}")
	public String artistDetail(@PathVariable Long artistId, Model model) {
		Optional<Artist> artist = hiringManagement.findById(artistId);

		if (artist.isPresent()) {
			Artist current = artist.get();

			model.addAttribute("artist", current);

			// to hide book Button if artist is booked
//			if (!currentFestival.getArtist().isEmpty()) {
//				for (Artist artist1: currentFestival.getArtist()) {
//					if (artist1.getId() == current.getId()) {
//						model.addAttribute("currentlyBooked", true);
//					}
//				}
//			}
//			else {
			model.addAttribute("currentlyBooked", false);
//			}

			// required for second nav-bar
			model.addAttribute("festival", currentFestival);

			return "artistDetail";

		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	@PostMapping("/bookArtist")
	public String bookArtist(@RequestParam("artist") Long artistId, @RequestParam("currentlyBooked") boolean currentlyBooked, RedirectAttributes ra) {
		Optional<Artist> artist = hiringManagement.findById(artistId);
		if (artist.isPresent()) {
			Artist current = artist.get();
//			currentFestival.addArtist(current);
			return "redirect:/artistPre1";
		}
		else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}

}
