package festivalmanager.festival;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import festivalmanager.utils.CurrentPageManagement;
import org.springframework.data.util.Streamable;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import festivalmanager.hiring.Artist;
import festivalmanager.hiring.ArtistRepository;
import festivalmanager.location.Location;
import festivalmanager.location.LocationManagement;
import festivalmanager.utils.LongOrNull;

@Controller
public class FestivalController {

	private final FestivalManagement festivalManagement;
	private CurrentPageManagement currentPageManagement;
	private Festival currentFestival;
	private long currentId;

	public FestivalController(FestivalManagement festivalManagement,
							  CurrentPageManagement currentPageManagement) {
		this.festivalManagement = festivalManagement;
		this.currentPageManagement = currentPageManagement;
		this.currentFestival = null;
		this.currentId = 0;
		
	}
	
	@GetMapping("/festivalOverview/{festivalId}")
	public String festivalDetail(@PathVariable Long festivalId, Model model) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);

		if (festival.isPresent()) {
			Festival current = festival.get();

			if (current.getLocation() != null) {
				System.out.println(current.getLocation().getName());
			}
			System.out.println(festivalId);
			model.addAttribute("festival", current);
			model.addAttribute("artists", current.getArtist());
			if (current.getLocation() != null) {
				System.out.println(current.getLocation().getName());
				model.addAttribute("location", current.getLocation());
			}
			currentId = festivalId;
			currentFestival = current;
			currentPageManagement.updateCurrentPage(model, "festivalDetail");
			return "festivalDetail";
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
	
	// Map View for Visitors
	@GetMapping("/mapVisitorView/{festivalId}")
	public String getMapVisitorView(@PathVariable("festivalId") long festivalId, Model model) {

		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			model.addAttribute("festival", festival.get());

			model.addAttribute("location", festival.get().getLocation());
		}

		currentPageManagement.updateCurrentPage(model, "map");
		return "/mapVisitorView";
	}
	
	
	@PostMapping("/newFestival")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String createNewFestival(@Validated NewFestivalForm form, Errors result, Model model) {
		
//		Streamable<F> customers = customerManagement.findAll();
//		for (Customer customer : customers) {
//			if (form.getName().equals(customer.getUserAccount().getUsername())) {
//				result.rejectValue("name", null, "Benutzername bereits vergeben");
//			}
//		}

//		if (!form.getPassword().equals(form.getPasswordReputation())) {
//			result.rejectValue("passwordReputation", null, "Passwörter stimmen nicht überein.");
//
//		}
		
		if (form.getEndDate().isBefore(form.getStartDate())) {
		result.rejectValue("endDate", null, "Das Enddatum liegt vor dem Startdatum.");

		}
		if (form.getEndDate().isBefore(LocalDate.now())) {
			result.rejectValue("endDate", null, "Das Enddatum darf nicht in der Vergangenheit liegen");

		}
		if (form.getStartDate().isBefore(LocalDate.now())) {
			result.rejectValue("startDate", null, "Das Startdatum darf nicht in der Vergangenheit liegen");

		}
		
		if (result.hasErrors()) {
			model.addAttribute("dateNow", LocalDate.now());
			return "newFestival";
		}

		// create Festival if no error appears
		festivalManagement.createFestival(form);

		return "redirect:/festivalOverview";
	}
	
	
	
	// gives NewFestivalForm to fill out
	@GetMapping("/newFestival") 
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String newFestival(Model model, NewFestivalForm form) {
		model.addAttribute("dateNow", LocalDate.now());
		return "newFestival";
	}
	
	// shows Festival Overview
	@GetMapping("/festivalOverview")
	public String festivals(Model model) {
		
		model.addAttribute("festivalList", festivalManagement.findAll());

		return "festivalOverview"; 
	}
	
	@GetMapping("/festivalOverview/remove/{id}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER')")
	public String getRemoveFestivalDialog(@PathVariable("id") long id, Model model) {
		model.addAttribute("currentId", id);
		model.addAttribute("dialog", "remove_festival");

		Optional<Festival> current = festivalManagement.findById(id);
		if (current.isPresent()) {
			model.addAttribute("currentName", current.get().getName());
			model.addAttribute("running", false);

		} else {
			model.addAttribute("currentName", "");
		}

		return "festivalOverview";
	}
	
	@PostMapping("/festival/remove")
	@PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER')")
	public String removeFestival(@Valid @RequestParam("id") @NotEmpty Long festivalId) {
		Optional<Festival> current = festivalManagement.findById(festivalId);
		if (current.isPresent()) {
//			if(current.running()) {
//				return "/festivalOverview";
//			} else {
			festivalManagement.deleteFestival(current.get());

//			}
		} 
		return "redirect:/festivalOverview";
	}
	
		
	@GetMapping("/artistPre1")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	String artistPre1(Model model, RedirectAttributes ra) {
		ra.addFlashAttribute("currentFestival", currentFestival);
		System.out.println(currentFestival.getName());
		return "redirect:artistOverview";
	}

	
	@GetMapping("/financesPre1")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	String financesPre1(Model model, RedirectAttributes ra) {
		ra.addFlashAttribute("currentFestivalId", currentFestival.getId());
		return "redirect:finances";
	}
	
	@GetMapping("/schedulePre1")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	String schedulePre1(Model model, RedirectAttributes ra) {
		ra.addFlashAttribute("currentFestivalId", currentFestival.getId());
		System.out.println(currentFestival.getName());
		return "redirect:schedule";
	}
	
	@GetMapping("/equipmentsPre1")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	String equipmentsPre1(Model model, RedirectAttributes ra) {
		ra.addFlashAttribute("currentFestivalId", currentFestival.getId());
		System.out.println(currentFestival.getName());
		return "redirect:equipments";
	}

	@GetMapping("/cateringProductCatalogPre1")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	String cateringProductCatalogPre1(Model model, RedirectAttributes ra) {
		ra.addFlashAttribute("currentFestivalId", new LongOrNull(currentFestival.getId()));
		return "redirect:cateringProductCatalog";
	}


	@GetMapping("/ticketsPre1")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	String ticketShopPre1(RedirectAttributes rd){
		rd.addFlashAttribute("festival", currentFestival);

		return "redirect:/tickets";
	}



















}
