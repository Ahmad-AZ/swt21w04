package festivalmanager.planning;


import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

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
	private Festival currentFestival;
	private long currentFestivalId;
	
	public PlanEquipmentController(PlanEquipmentManagement planEquipmentManagement, FestivalManagement festivalManagement, EquipmentManagement equipmentManagement) {
		this.planEquipmentManagement = planEquipmentManagement;
		this.festivalManagement = festivalManagement;
		this.equipmentManagement = equipmentManagement;
		this.currentFestivalId = 0;
	}
	
	// shows Equipments Overview
	@GetMapping("/equipments")  
	public String equipments(Model model, @ModelAttribute("currentFestivalId") long currentFestivalId) {
		if(currentFestivalId != 0) {
			this.currentFestivalId = currentFestivalId;
		}
		
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
			
			// required for secound nav-bar
			model.addAttribute("festival", current);
			return "equipments";
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}	
	}
	
//	@PostMapping("/addStage")
//	public String stages(@Valid StageEquipment stageForm, Errors errors) {
//		if(errors.hasErrors()) {
//			System.out.println(errors);
//			return "equipments";
//		} 
//		System.out.println("here");
//		planEquipmentManagement.rentStage(stageForm.toStage(), currentFestival);
//		
//		return "redirect:/equipmentsPre1";
//	}
	 
	@PostMapping("/addStage")
	public String addStages(@RequestParam("equipmentsId") @NotNull long equipmentsId, @RequestParam("name") @NotEmpty String name) {
		
		Equipment equipment = equipmentManagement.findById(equipmentsId).get();
		
		boolean success = planEquipmentManagement.rentStage(name, equipment, currentFestivalId);
		if(success == false) {
			
		}
		return "redirect:/equipmentsPre1";
		
	}
	
	@GetMapping("equipments/remove/{id}")
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
		
		
		
		
		return "redirect:/equipmentsPre1";
	}
	
	
	@PostMapping("/rentEquipmentAmount")
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
		return "redirect:/equipmentsPre1";
	}
	
	
	
//	interface StageEquipment {
//
//		@NotEmpty
//		String getName();
//		
//		@NotEmpty
//		@Min(value = 0) 
//		Double getRentalPerDay();
//		
//		@NotEmpty
//		@Min(value = 0) 
//		Integer getLength();
//		
//		@NotEmpty
//		@Min(value = 0) 
//		Integer getWidth();
//		
//		default Stage toStage() {
//			return new Stage(getName(), getRentalPerDay(), getLength(), getWidth());
//		}
//	}
}


