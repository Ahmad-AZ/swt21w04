package festivalmanager.hiring;

import java.time.Duration;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

/**
 * this is the controller which connects the MVC of the website to artist
 * @author Tuan Giang Trinh
 */
@Controller
public class HiringController {
	private final HiringManagement hiringManagement;


	public HiringController(HiringManagement hiringManagement) {
		this.hiringManagement = hiringManagement;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Künstler";
	}

	@GetMapping("/artists")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String artists(Model model) {
		model.addAttribute("artistList", hiringManagement.findAll());
		return "artists";
	}

	@GetMapping("/artists/{artistId}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String artistDetail(@PathVariable Long artistId, Model model){
		Optional<Artist> artist = hiringManagement.findById(artistId);

		if (artist.isPresent()) {
			Artist current = artist.get();
			model.addAttribute("artist", current);
			model.addAttribute("hasBookings", current.hasBookingArtist());
			model.addAttribute("show", current.getShows());

			return "artistDetail";
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}

	@GetMapping("/artists/{artistId}/edit")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String artistEdit(@PathVariable Long artistId, Model model) {
		Optional<Artist> artist = hiringManagement.findById(artistId);

		if (artist.isPresent()) {
			Artist current = artist.get();

			System.out.println(artistId);
			model.addAttribute("artist", current);
			Double price = current.getPrice().getNumber().doubleValue();
			model.addAttribute("price", price);
			Integer stageTechnician = current.getStageTechnician();
			model.addAttribute("stageTechnician", stageTechnician);
			return "artistEdit";

		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}

	@GetMapping("artists/remove/{id}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
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
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String removeArtist(@RequestParam("id") Long artistId) {
		try {
			hiringManagement.removeArtist(artistId);
			return "redirect:/artists";

		} catch (Exception e) {
			return "/artistsDeleteFailed";

		}
	}
	@GetMapping("/artistsDeleteFailed")
	public String artistsDeleteFailed() {
		return "artistsDeleteFailed";
	}

	@PostMapping("/saveArtist")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String saveArtist(@Validated NewArtistForm form,
							 Errors result,
							 @RequestParam("artist") Long artistId,
							 Model model) {

		Optional<Artist> artist = hiringManagement.findById(artistId);

		if (artist.isPresent()) {
			Artist current = artist.get();

			//Artist with same name already exists
			for (Artist anArtist : hiringManagement.findAll()) {
				if(anArtist.getName().equals(form.getName()) && !anArtist.getName().equals(current.getName())) {
					result.rejectValue("name", null, "Künstler mit diesem Namen existiert bereits");
				}
			}

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
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String createNewArtist(@Validated NewArtistForm form, Errors result){
		for(Artist anArtist : hiringManagement.findAll()) {
			if(anArtist.getName().equals(form.getName())) {
				result.rejectValue("name", null, "Künstler mit diesem Namen existiert bereits");
				return "newArtist";
			}
		}

		if(result.hasErrors()){
			return "newArtist";
		}

		hiringManagement.createAritst(form);
		return "redirect:/artists";
	}

	@GetMapping("/newArtist")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String newArtist(Model model, NewArtistForm form) {
		return "newArtist";
	}

	@GetMapping("/artists/newShow/{artistId}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String newShow(@PathVariable Long artistId, Model model) {
		Optional<Artist> artist = hiringManagement.findById(artistId);

		if(artist.isPresent()) {
			Artist current = artist.get();
			model.addAttribute("artistShow", current);
			return "newShow";
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	@PostMapping("/newShow/{artistId}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String createNewShow(@PathVariable Long artistId, @Validated NewShowForm newShowForm, Model model) {
		Optional<Artist> artist = hiringManagement.findById(artistId);
		model.addAttribute("artistId", artistId);

		if(artist.isPresent()) {
			Artist current = artist.get();
			current.addShow(new Show(newShowForm.getName(), Duration.ofMinutes(newShowForm.getPerformance())));
			hiringManagement.saveArtist(current);
			return "redirect:/artists/"+ artistId;
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
}
