package festivalmanager.hiring;

import festivalmanager.location.Location;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;

import java.util.Optional;

@Controller
public class HiringController {
	private final HiringManagement hiringManagement;
	private Festival currentFestival;
	private FestivalManagement festivalManagement;

	public HiringController(
//			OffersArtists offersArtists,
			ArtistRepository artistRepository,
			HiringManagement hiringManagement) {
		this.hiringManagement = hiringManagement;
//		this.offersArtists = offersArtists;
		this.currentFestival = null;
	}

	@GetMapping("/artists")
	public String artists(Model model) {
		model.addAttribute("artistList", hiringManagement.findAll());
		return "artists";
	}
	@GetMapping("/artistOverview")
	public String artistOverview(Model model, @ModelAttribute("currentFestival") Festival currentFestival,
								   @ModelAttribute("fm") FestivalManagement fm) {
		this.currentFestival = currentFestival;
		this.festivalManagement = fm;
		model.addAttribute("artistList", hiringManagement.findAll());

		// required for second nav-bar
		model.addAttribute("festival", currentFestival);

		return "artistOverview";
	}

	@GetMapping("artists/remove/{id}")
	public String getRemoveArtistDialog(@PathVariable("id") long id, Model model) {
		model.addAttribute("locatoins", hiringManagement.findAll());
		model.addAttribute("currentId", id);
		model.addAttribute("dialog", "remove_artist");

		Optional<Artist> current = hiringManagement.findById(id);
		if (current.isPresent()) {
			model.addAttribute("currentName", current.get().getName());
		} else {
			model.addAttribute("currentName", "");
		}

		return "/artists";
	}

	@PostMapping("/artists/remove")
	public String removeArtist(@RequestParam("id") Long artistId) {
		hiringManagement.removeArtist(artistId);

		return "redirect:/artists";
	}

	@GetMapping("/artistOverview/{artistId}")
	public String artistDetail(@PathVariable Long artistId, Model model) {
		Optional<Artist> artist = hiringManagement.findById(artistId);

		if (artist.isPresent()) {
			Artist current = artist.get();

			System.out.println(artistId);
			model.addAttribute("artist", current);

			// required for second nav-bar
			model.addAttribute("festival", currentFestival);

			return "artistDetail";

		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
//	@PostMapping("/bookArtist")
//	public String selectLocation(@RequestParam("artist") Long artistId) {
//		Optional<Artist> artist = hiringManagement.findById(artistId);
//
//		if (artist.isPresent()) {
//			Artist current = artist.get();
//			artistRepository.save(current);
//			boolean add = currentFestival.addArtist(current);
//			festivalManagement.saveFestival(currentFestival);
//			long id = current.getId();
//			return "redirect:/artistPre1";
//
//		} else {
//			throw new ResponseStatusException(
//					HttpStatus.NOT_FOUND, "entity not found"
//			);
//		}
//	}
}
