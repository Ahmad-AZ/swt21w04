package prototype.hiring;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import prototype.festival.Festival;
import prototype.festival.FestivalManagement;
import prototype.location.Location;

import java.util.Optional;

@Controller
public class HiringController {
//	private final OffersArtists offersArtists;
	private final ArtistRepository artistRepository;
	private Festival currentFestival;
	private FestivalManagement festivalManagement;

	public HiringController(
//			OffersArtists offersArtists,
			ArtistRepository artistRepository) {
//		this.offersArtists = offersArtists;
		this.artistRepository = artistRepository;
		this.currentFestival = null;
	}

	@GetMapping("/artists")
	public String artists(Model model) {
		model.addAttribute("artistsList", artistRepository);
		return "artists";
	}
	@GetMapping("/artistOverview")
	public String artistOverview(Model model, @ModelAttribute("currentFestival") Festival currentFestival,
								   @ModelAttribute("fm") FestivalManagement fm) {
		this.currentFestival = currentFestival;
		this.festivalManagement = fm;
		model.addAttribute("artistList", artistRepository.findAll());

		// required for second nav-bar
		model.addAttribute("festival", currentFestival);

		return "artistOverview";
	}

	@GetMapping("/artistOverview/{artistId}")
	public String artistDetail(@PathVariable Long artistId, Model model) {
		Optional<Artist> artist = artistRepository.findById(artistId);

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
	@PostMapping("/bookArtist")
	public String selectLocation(@RequestParam("artist") Long artistId) {
		Optional<Artist> artist = artistRepository.findById(artistId);

		if (artist.isPresent()) {
			Artist current = artist.get();
			artistRepository.save(current);
			boolean add = currentFestival.addArtist(current);
			festivalManagement.saveFestival(currentFestival);
			long id = current.getId();
			return "redirect:/artistPre1";

		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
}
