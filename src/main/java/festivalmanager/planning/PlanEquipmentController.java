package festivalmanager.planning;


import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import festivalmanager.utils.UtilsManagement;
import org.hibernate.validator.constraints.Range;
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
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.location.Location;


@Controller
public class PlanEquipmentController {
	private final PlanEquipmentManagement planEquipmentManagement;
	private final FestivalManagement festivalManagement;
	private final EquipmentManagement equipmentManagement;
	private final UtilsManagement utilsManagement;
	private Festival currentFestival;
	private long currentFestivalId;
	
	public PlanEquipmentController(PlanEquipmentManagement planEquipmentManagement, FestivalManagement festivalManagement, EquipmentManagement equipmentManagement, UtilsManagement utilsManagement) {
		this.planEquipmentManagement = planEquipmentManagement;
		this.festivalManagement = festivalManagement;
		this.equipmentManagement = equipmentManagement;
		this.utilsManagement = utilsManagement;
		this.currentFestivalId = 0;
	}
	
	// shows Equipments Overview
	@GetMapping("/equipments")  
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String equipments(Model model) {
		this.currentFestivalId = utilsManagement.getCurrentFestivalId();
		
		Optional<Festival> festival = festivalManagement.findById(currentFestivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			currentFestival = current;
					
			Map<Equipment, Long> equipmentsMap = new HashMap<>();
						
			for (Equipment anEquipment : equipmentManagement.findAll()) {
				long amount = current.getEquipments().getOrDefault(anEquipment.getId(), (long) 0);
				// Stages would be handled extra
				if(anEquipment.getType().equals(EquipmentType.STAGE)) {
					// Stage children Objects should not been visible here
					if(!(anEquipment.getClass().getName().equals(Stage.class.getName()))) {
						model.addAttribute("equipmentStage", anEquipment);
					}

				}else {
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
	public String addStages(@RequestParam("equipmentsId") @NotNull long equipmentsId, @RequestParam("name") @NotEmpty String name, RedirectAttributes ra) {
				
		Equipment equipment = equipmentManagement.findById(equipmentsId).get();
		
		boolean success = planEquipmentManagement.rentStage(name, equipment, currentFestivalId);
		if(success == false) {
			ra.addFlashAttribute("message", "Bühne mit diesem Namen existiert bereits");
			ra.addFlashAttribute("currentFestivalId", currentFestival.getId());
			return "redirect:/equipments";
		}
		return "redirect:/equipments";
		
	}
	
	@GetMapping("equipments/remove/{id}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String removeStage(@PathVariable("id") Long id) {
		
		Optional<Equipment> equipment = equipmentManagement.findById(id);
		if(equipment.isPresent()) {
			Equipment current = equipment.get();
	
			if(current.getType().equals(EquipmentType.STAGE)) {
				Stage stage = (Stage) current;
				planEquipmentManagement.unrentStage(stage,currentFestivalId);
			}
			else {
				System.out.println("Equipment is not Stage");
			}
		}
		else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}




		return "redirect:/equipments";
	}
	
	
	@PostMapping("/rentEquipmentAmount")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String rentEquipmentAmount(Model model, 
					  @RequestParam("equipmentsId") long equipmentsId,
					  @RequestParam("equipmentsAmount") long equipmentsAmount) {
	
		

		Equipment equipment = equipmentManagement.findById(equipmentsId).get();
		if(equipment.getType().equals(EquipmentType.STAGE)) {
			// TODO:  for more Stage types: get number of already rented stages 
			if(equipmentsAmount > currentFestival.getLocation().getStageCapacity()) {
//				reuslt.rejectValue("equipmentError", null, "Die maximale Bühnenanzahl darf nicht überschritten werden");
//				return "equipments";
				// set Amount to max if higher than max
				equipmentsAmount = currentFestival.getLocation().getStageCapacity();
			}
			// TODO: new input to give names for stages			
			
		}else {
			planEquipmentManagement.rentEquipment(equipmentsId, equipmentsAmount, currentFestival);
		}
		return "redirect:/equipments";
	}
	
	
}


