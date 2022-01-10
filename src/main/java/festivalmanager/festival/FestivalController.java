package festivalmanager.festival;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

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

import festivalmanager.utils.UtilsManagement;

@Controller
public class FestivalController {

	private final FestivalManagement festivalManagement;
	private UtilsManagement utilsManagement;

	
	public FestivalController(FestivalManagement festivalManagement,
							  UtilsManagement utilsManagement) {
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Festivalübersicht";
	}
	
	@GetMapping("/festivalOverview/{festivalId}")
	public String festivalDetail(@PathVariable Long festivalId, Model model, StringInputForm stringInputForm) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();

			System.out.println(festivalId);
			model.addAttribute("festival", current);
			model.addAttribute("artists", current.getArtist());
			if (current.getLocation() != null) {
				System.out.println(current.getLocation().getName());
				model.addAttribute("location", current.getLocation());
			}
			utilsManagement.prepareModel(model, festivalId);
			return "festivalDetail";
		} else {
			return "redirect:/festivalOverview";
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
		model.addAttribute("title", "Karte");

		utilsManagement.prepareModel(model, festivalId);
		return "/mapVisitorView";
	}
	
	
	@PostMapping("/newFestival")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String createNewFestival(@Validated NewFestivalForm form, Errors result, Model model) {
				
		if (form.getEndDate().isBefore(form.getStartDate())) {
			result.rejectValue("endDate", null, "Das Enddatum liegt vor dem Startdatum.");
		}
		// Festival with same name already exists
		for(Festival aFestival : festivalManagement.findAll()) {
			if(aFestival.getName().equals(form.getName())){
				result.rejectValue("name", null, "Festival mit diesem Namen existiert bereits.");	
			}
		}
		
		if (result.hasErrors()) {
			model.addAttribute("dateNow", LocalDate.now());
			return "newFestival";
		}

		// save Festival if no error appears
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
	
	
	
	@GetMapping("/festivalOverview/{festivalId}/editName")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String getEditFestivalNameDialog(@PathVariable("festivalId") Long festivalId,
											StringInputForm stringInputForm, Model model) {
		model.addAttribute("dialog", "edit name");
		
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			model.addAttribute("festival", current);
			model.addAttribute("artists", current.getArtist());
			model.addAttribute("location", current.getLocation());
		}	
		utilsManagement.prepareModel(model, festivalId);
		return "festivalDetail";

	}
	
	
	@PostMapping("/editFestivalName/{festivalId}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String editFestivalName(@PathVariable("festivalId") Long festivalId,
								   @Validated StringInputForm stringInputForm,
								   Errors result, Model model) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();

			if (current.getStartDate().isBefore(LocalDate.now().plusDays(14))) {
				result.rejectValue("name", null, "Das Festival beginnt in weniger als 14 Tage");
			}
			
			// Festival with same name already exists
			for(Festival aFestival : festivalManagement.findAll()) {
				if(aFestival.getName().equals(stringInputForm.getName())){
					result.rejectValue("name", null, "Festival mit diesem Namen existiert bereits.");	
				}
			}
						
			model.addAttribute("festival", current);
			model.addAttribute("artists", current.getArtist());
			model.addAttribute("location", current.getLocation());

			utilsManagement.prepareModel(model, festivalId);
					
			// not perfect
			if (result.hasErrors()) {
				model.addAttribute("dialog", "edit name");
				return "festivalDetail";
			}
			
			current.setName(stringInputForm.getName());
			festivalManagement.saveFestival(current);
		}
		return "redirect:/festivalOverview/"+ festivalId;
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

		utilsManagement.prepareModel(model, id);
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

}
