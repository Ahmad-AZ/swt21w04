package festivalmanager.hiring;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
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
//	@GetMapping("/artistOverview")
//	public String artistOverview(Model model, @ModelAttribute("currentFestival") Festival currentFestival,
//								   @ModelAttribute("fm") FestivalManagement fm) {
//		this.currentFestival = currentFestival;
//		this.festivalManagement = fm;
//		model.addAttribute("artistList", hiringManagement.findAll());
//
//		// required for second nav-bar
//		model.addAttribute("festival", currentFestival);
//
//		return "artistOverview";
//	}
	@GetMapping("/artists/{artistId}")
	public String artistDetail(@PathVariable Long artistId, Model model){
		Optional<Artist> artist = hiringManagement.findById(artistId);

		if (artist.isPresent()) {
			Artist current = artist.get();
			model.addAttribute("artist", current);
			//model.addAttribute("hasBookings", current.hasBookings());

			return "artistDetail";
		}
		else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}

	@GetMapping("/artists/{artistId}/edit")
	public String artistEdit(@PathVariable Long artistId, Model model) {
		Optional<Artist> artist = hiringManagement.findById(artistId);

		if (artist.isPresent()) {
			Artist current = artist.get();

			System.out.println(artistId);
			model.addAttribute("artist", current);
			return "artistEdit";

		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
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

//	@GetMapping("/artistOverview/{artistId}")
//	public String artistDetail(@PathVariable Long artistId, Model model) {
//		Optional<Artist> artist = hiringManagement.findById(artistId);
//
//		if (artist.isPresent()) {
//			Artist current = artist.get();
//
//			System.out.println(artistId);
//			model.addAttribute("artist", current);
//
//			// required for second nav-bar
//			model.addAttribute("festival", currentFestival);
//
//			return "artistDetail";
//
//		} else {
//			throw new ResponseStatusException(
//					HttpStatus.NOT_FOUND, "entity not found"
//			);
//		}
//	}

	@PostMapping("/saveArtist")
	public String saveArtist(@Validated NewArtistForm form, Errors result, @RequestParam("artist") Long artistId, Model model) {

		Optional<Artist> artist = hiringManagement.findById(artistId);

		if (artist.isPresent()) {
			Artist current = artist.get();
			if (result.hasErrors()) {
				System.out.println("form has errors");
				model.addAttribute("artist", current);

				return "artistEdit";
			}

			hiringManagement.editArtist(current, form);
			return "redirect:/artists";

		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}

	@PostMapping("/newArtist")
	public String createNewArtist(@Validated NewArtistForm form, Errors result){
		if(result.hasErrors()){
			return "newArtist";
		}

		hiringManagement.createAritst(form);
		return "redirect:/artists";
	}

	@GetMapping("/newArtist")
	public String newArtist(Model model, NewArtistForm form) {
		return "newArtist";
	}

//	@PostMapping("/bookArtist")
//	public String selectartist(@RequestParam("artist") Long artistId) {
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
