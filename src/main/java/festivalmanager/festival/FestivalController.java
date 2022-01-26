package festivalmanager.festival;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import festivalmanager.messaging.MessageManagement;
import festivalmanager.utils.UtilsManagement;

/**
 * A class used to pass on values computed in {@link FestivalManagement}
 * to the festivalDetail.html, festivalOverview.html and mapVisitorView.html
 * 
 * @author Adrian Scholze
 */
@Controller
public class FestivalController {

	private final FestivalManagement festivalManagement;
	private UtilsManagement utilsManagement;
	private final MessageManagement messageManagement;

	/**
	 * Creates a new {@link FestivalController} with the given {@link FestivalManagement}, {@link UtilsManagement} and
	 * {@link MessageManagement}.
	 *
	 * @param festivalManagement must not be {@literal null}.
	 * @param utilsManagement must not be {@literal null}.
	 * @param messageManagement must not be {@literal null}.
	 */
	public FestivalController(FestivalManagement festivalManagement,
							  UtilsManagement utilsManagement,
							  MessageManagement messageManagement) {
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");
		Assert.notNull(utilsManagement, "UtilsManagement must not be null!");
		Assert.notNull(messageManagement, "MessageManagement must not be null!");
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
		this.messageManagement = messageManagement;
	}

	/**
	 * Used to pass on the title of the festival overview page to the page header
	 * 
	 * @return title attribute for the festival overview tab
	 */
	@ModelAttribute("title")
	public String getTitle() {
		return "Festivalübersicht";
	}
	
	/**
	 * Generates a page which shows basic informations for the current {@link Festival}
	 * 
	 * @param festivalId
	 * @param model
	 * @param stringInputForm 
	 * @return the festival detail page for the festival belonging to festivalId
	 */
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
	
	
	/**
	 * Generates a page which shows the map for the current {@link Festival}
	 * 
	 * @param festivalId
	 * @param model
	 * @return the map visitor view page for the festival belonging to festivalId
	 */
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
	
	/**
	 * Takes given {@link NewFestivalForm} and call methods to create a new {@link Festival} instance.
	 * Rejects errors if {@link Errors} occur.
	 * 
	 * @param form
	 * @param result
	 * @param model 
	 * @return if no error occur the festival overview page, else the new festival page
	 */
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
	
	/**
	 * Generates a page which has a {@link NewFestivalForm} to fill out.
	 * Access only for admin, planner and manager.
	 * 
	 * @param model
	 * @param form
	 * @return the new festival page
	 */
	@GetMapping("/newFestival") 
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String newFestival(Model model, NewFestivalForm form) {
		model.addAttribute("dateNow", LocalDate.now());
		return "newFestival";
	}
		
	/**
	 * Generates a page which shows basic informations for the current {@link Festival} 
	 * and has an input dialog for the input of the new name
	 * 
	 * @param festivalId
	 * @param model
	 * @param stringInputForm 
	 * @return the festival detail page for the festival belonging to festivalId, with dialog
	 */
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
	
	/**
	 * Takes given {@link StringInputForm} and call method to edit {@link Festival}s name.
	 * Rejects errors if {@link Errors} occur.
	 * 
	 * @param festivalId
	 * @param stringInputForm
	 * @param result
	 * @param model 
	 * @return the festival detail page for the {@link Festival} belonging to festivalId.
	 */
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
					
			if (result.hasErrors()) {
				model.addAttribute("dialog", "edit name");
				return "festivalDetail";
			}
			
			current.setName(stringInputForm.getName());
			festivalManagement.saveFestival(current);
		}
		return "redirect:/festivalOverview/"+ festivalId;
	}
	
	
	/**
	 * Generates a page which shows an overview about all existing {@link Festival}s
	 * 
	 * @param model
	 * @return the festival overview page
	 */
	@GetMapping("/festivalOverview")
	public String festivals(Model model) {
		model.addAttribute("festivalList", festivalManagement.findAll());
		model.addAttribute("messages", messageManagement.findGlobalMessages());

		return "festivalOverview"; 
	}

	/**
	 * Generates a page which shows an overview about all existing {@link Festival}s
	 * and has an dialog to confirm the deletion of the {@link Festival} instance.
	 * 
	 * @param festivalId
	 * @param model
	 * @return the festival overview page with the dialog
	 */
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
	
	/**
	 * Call methods to delete {@link Festival} instance with the given id, 
	 * and returns the festival overview page
	 * 
	 * @param festivalId
	 * @return festival overview page
	 */
	@PostMapping("/festival/remove")
	@PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER')")
	public String removeFestival(@Valid @RequestParam("id") @NotEmpty Long festivalId) {
		Optional<Festival> current = festivalManagement.findById(festivalId);
		if (current.isPresent()) {
			festivalManagement.deleteFestival(current.get());
		} 
		return "redirect:/festivalOverview";
	}

}
