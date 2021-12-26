package festivalmanager.planning;


import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import festivalmanager.utils.UtilsManagement;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.Equipment.EquipmentType;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Stage;
import festivalmanager.Equipment.Equipment;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;


@Controller
public class PlanEquipmentController {
	private final PlanEquipmentManagement planEquipmentManagement;
	private final FestivalManagement festivalManagement;
	private final EquipmentManagement equipmentManagement;
	private final UtilsManagement utilsManagement;
	private Festival currentFestival;
	private long currentFestivalId;
	
	public PlanEquipmentController(PlanEquipmentManagement planEquipmentManagement, FestivalManagement festivalManagement,
								   EquipmentManagement equipmentManagement, UtilsManagement utilsManagement) {
		this.planEquipmentManagement = planEquipmentManagement;
		this.festivalManagement = festivalManagement;
		this.equipmentManagement = equipmentManagement;
		this.utilsManagement = utilsManagement;
		this.currentFestivalId = 0;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Equipment-Auswahl";
	}

	// shows Equipments Overview
	@GetMapping("/equipments")  
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String equipments(Model model, EquipmentRentingForm equipmentRentingForm, NewStageForm newStageForm) {
		this.currentFestivalId = utilsManagement.getCurrentFestivalId();
		
		Optional<Festival> festival = festivalManagement.findById(currentFestivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			currentFestival = current;
					
			Map<Equipment, Long> equipmentsMap = new HashMap<>();
						
			for (Equipment anEquipment : equipmentManagement.findlAllEquipments()) {				
				if(anEquipment.getType().equals(EquipmentType.STAGE)) {
					model.addAttribute("equipmentStage", anEquipment);
					System.out.println(anEquipment.getName());
				} else {
					long amount = current.getEquipments().getOrDefault(anEquipment.getId(), (long) 0);
					equipmentsMap.put(anEquipment, amount);
				}
			}
						
			// show current Stage List form Festival
			model.addAttribute("stageList", current.getStages());
			
			model.addAttribute("equipmentsMap", equipmentsMap);
			
			//required for groundView
			model.addAttribute("location", current.getLocation());
			utilsManagement.setCurrentPageLowerHeader("equipment");
			utilsManagement.prepareModel(model);
			return "equipments";
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}	
	}
	
	 
	@PostMapping("/addStage")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String addStage(@Valid NewStageForm newStageForm, Errors result, Model model, 
								EquipmentRentingForm equipmentRentingForm) {
		
		
		Optional<Festival> festivalOP = festivalManagement.findById(currentFestivalId);
		if(!festivalOP.isPresent()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
		Festival festival = festivalOP.get();
		
		if(!result.hasErrors()) {
			SalespointIdentifier equipmentsId = newStageForm.getEquipmentsId();
			String name = newStageForm.getName();
			
			Optional<Equipment> equipmentOP = equipmentManagement.findEquipmentById(equipmentsId);
						
			if(!equipmentOP.isPresent()) {
				throw new ResponseStatusException(
						HttpStatus.NOT_FOUND, "entity not found"
				);
			}
			
			Equipment equipment = equipmentOP.get();
			
			// Stage with same name already exists
			for(Stage aStage : festival.getStages()) {
				if(aStage.getName().equals(name)){
					result.rejectValue("name", null, "Bühne mit diesem Namen existiert bereits.");
					
				}
			}
			
			if(!result.hasErrors()) {
				// maximum stage capacity reached
				if(festival.getStages().size() < festival.getLocation().getStageCapacity()) {
					planEquipmentManagement.rentStage(name, equipment, festival);
					
				} else {
					result.rejectValue("name", null, "Die maximale Bühnenkapazität wurde erreicht.");
				}
			}
		}
		
		if(result.hasErrors()) {
			Map<Equipment, Long> equipmentsMap = new HashMap<>();
			
			for (Equipment anEquipment : equipmentManagement.findlAllEquipments()) {				
				if(anEquipment.getType().equals(EquipmentType.STAGE)) {
					model.addAttribute("equipmentStage", anEquipment);

				} else {
					long amount = festival.getEquipments().getOrDefault(anEquipment.getId(), (long) 0);
					equipmentsMap.put(anEquipment, amount);
				}
			}
			
			// show current Stage List form Festival
			model.addAttribute("stageList", festival.getStages());
			
			model.addAttribute("equipmentsMap", equipmentsMap);
			
			//required for groundView
			model.addAttribute("location", festival.getLocation());
			utilsManagement.setCurrentPageLowerHeader("equipment");
			utilsManagement.prepareModel(model);
			return "equipments";
		}
		
		return "redirect:/equipments";
		
		
	}
	
	
	@GetMapping("equipments/remove/{id}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String getRemoveStageDialog(@PathVariable("id") SalespointIdentifier id, Model model) {
		model.addAttribute("dialog", "remove stage");
		
		Optional<Stage> stage = equipmentManagement.findStageById(id);
		if(stage.isPresent()) {
			Stage current = stage.get();
			model.addAttribute("stage", current);
			model.addAttribute("equipmentsMap", null);
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
		
		utilsManagement.setCurrentPageLowerHeader("equipment");
		utilsManagement.prepareModel(model);
		return "equipments";
	}
	
	
	@PostMapping("equipments/remove/{id}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String removeStage(@PathVariable("id") SalespointIdentifier id) {
		
		Optional<Stage> opStage = equipmentManagement.findStageById(id);
		if(opStage.isPresent()) {
			Stage stage = opStage.get(); 
			System.out.println(stage);
			planEquipmentManagement.unrentStage(stage,currentFestivalId);
			System.out.println("after call");
	
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
		return "redirect:/equipments";
	}
	
	
	@PostMapping("/rentEquipmentAmount")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String rentEquipmentAmount(Model model, @Valid EquipmentRentingForm equipementRentingForm, Errors result, RedirectAttributes attributes) {
	
		if(result.hasErrors()) {
			System.out.println(result);
			attributes.addFlashAttribute("message", "ungültige Eingabe");
			return "redirect:/equipments";
		}
		SalespointIdentifier equipmentsId = equipementRentingForm.getEquipmentsId();
		Long equipmentsAmount = equipementRentingForm.getAmount();
		System.out.println(equipmentsId + "     "+ equipmentsAmount);
		
		
		Equipment equipment = equipmentManagement.findEquipmentById(equipmentsId).get();
//		if(equipment.getType().equals(EquipmentType.STAGE)) {
//			// TODO:  for more Stage types: get number of already rented stages 
//			if(equipmentsAmount > currentFestival.getLocation().getStageCapacity()) {
//				equipmentsAmount = currentFestival.getLocation().getStageCapacity();
//			}		
//			
//		} else {
		
		planEquipmentManagement.rentEquipment(equipmentsId, equipmentsAmount, currentFestival);
//		}
		return "redirect:/equipments";
	}
	
	
}


