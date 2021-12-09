package festivalmanager.planning;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.HiringManagement;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
public class PlanOffersController {
	private final PlanOffersManagement planOffersManagement;
	private final HiringManagement hiringManagement;
	private Festival currentFestival;
	private final FestivalManagement festivalManagement;

	public PlanOffersController(PlanOffersManagement planOffersManagement, HiringManagement hiringManagement, FestivalManagement festivalManagement) {
		this.planOffersManagement = planOffersManagement;
		this.hiringManagement = hiringManagement;
		this.festivalManagement = festivalManagement;
		this.currentFestival = null;
	}
	@GetMapping("/artistOverview")
	public String artistOverview(Model model, @ModelAttribute("currentFestival") Festival currentFestival) {
		Optional<Festival> festival = festivalManagement.findById(currentFestival.getId());
		if (festival.isPresent()) {
			Festival current = festival.get();
			this.currentFestival = current;
			model.addAttribute("artistList", hiringManagement.findAll());
			if (!current.artistsIsEmpty()){
				model.addAttribute("bookedArtistId", current.getArtist());
			}
			else {
				model.addAttribute("noArtist", true);
			}
			model.addAttribute("festival", current);
			System.out.println(current.artistsIsEmpty());
			if (!current.artistsIsEmpty()) {
				Iterator<Artist> iterator = current.getArtist().iterator();
				while (iterator.hasNext()) {
					model.addAttribute("bookedArtist", iterator.next().getId());
				}
			}
			else{
				model.addAttribute("bookedArtist", 0);
			}

			return "artistOverview";
		}
		else{
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	@GetMapping("/artistOverview/{artistId}")
	public String artistDetail(@PathVariable Long artistId, Model model) {
		Optional<Artist> artist = hiringManagement.findById(artistId);

		if (artist.isPresent()) {
			Artist current = artist.get();

			model.addAttribute("artist", current);
			model.addAttribute("hasBookings", current.hasBookingArtist());

			if (!currentFestival.artistsIsEmpty()){
				for (Artist artist1 : currentFestival.getArtist()) {
					model.addAttribute("ArtistCurrentlyBooked", artist1.getId() == current.getId());
				}
			}
			else{
				model.addAttribute("ArtistCurrentlyBooked", false);
			}

			model.addAttribute("festival", currentFestival);

			return "artistDetailPlan";

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
			if (currentlyBooked) {
				planOffersManagement.unbookArtist(current, currentFestival);
			}
			else {
				boolean success = planOffersManagement.bookArtist(current, currentFestival);
				System.out.println("book artist success " + success);
				if(!success) {
					ra.addFlashAttribute("message", "Künstler ist im diesem Zeitraum belegt");
					return "redirect:/artistOverview"+ current.getId();
				}
			}
			return "redirect:/artistPre1";
		}
		else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	@GetMapping("/artistOverview/unbook")
	public String unbookArtist(){
		for (Artist artist1 : currentFestival.getArtist()){
			Optional<Artist> artist = hiringManagement.findById(artist1.getId());
			if (artist.isPresent()) {
				Artist current = artist.get();
				planOffersManagement.unbookArtist(current, currentFestival);
			}
			else{
				throw new ResponseStatusException(
						HttpStatus.NOT_FOUND, "entity not found"
				);
			}
		}
		return "redirect:/artistPre1";
	}
}
