package festivalmanager.planning;


import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import festivalmanager.utils.UtilsManagement;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.util.Streamable;
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
import festivalmanager.location.Location;


@Controller
public class PlanEquipmentController {
	private final PlanEquipmentManagement planEquipmentManagement;
	private final FestivalManagement festivalManagement;
	private final EquipmentManagement equipmentManagement;
	private final UtilsManagement utilsManagement;

	
	public PlanEquipmentController(PlanEquipmentManagement planEquipmentManagement, FestivalManagement festivalManagement,
								   EquipmentManagement equipmentManagement, UtilsManagement utilsManagement) {
		this.planEquipmentManagement = planEquipmentManagement;
		this.festivalManagement = festivalManagement;
		this.equipmentManagement = equipmentManagement;
		this.utilsManagement = utilsManagement;

	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Equipment-Auswahl";
	}
	
	@ModelAttribute("festival")
	public Festival getFestival(@PathVariable("festivalId") long festivalId) {
		return festivalManagement.findById(festivalId).orElse(null);
	}
	
	@ModelAttribute("location")
	public Location getLocation(@PathVariable("festivalId") long festivalId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			return festival.get().getLocation();
		}
		return null;
	}
		
	@ModelAttribute("equipmentsMap")
	public Map<Equipment, Long> getEquipmentsMap(@PathVariable("festivalId") long festivalId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Map<Equipment, Long> equipmentsMap = new HashMap<>();
			
			for (Equipment anEquipment : equipmentManagement.findAllEquipments()) {				
				if(!anEquipment.getType().equals(EquipmentType.STAGE)) {
					long amount = festival.get().getEquipments().getOrDefault(anEquipment.getId(), (long) 0);
					equipmentsMap.put(anEquipment, amount);
				}
			}
								
			return equipmentsMap;
		}
		return null;
	}
	
	@ModelAttribute("equipmentStage")
	public Equipment getStage(@PathVariable("festivalId") long festivalId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {	
			for (Equipment anEquipment : equipmentManagement.findAllEquipments()) {				
				if(anEquipment.getType().equals(EquipmentType.STAGE)) {
					System.out.println(anEquipment.getName());
					return  anEquipment;
				}
			}
		}
		return null;
	}

	// shows Equipments Overview
	@GetMapping("/equipments/{festivalId}")  
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String equipments(Model model, EquipmentRentingForm equipmentRentingForm, NewStageForm newStageForm) {

			utilsManagement.setCurrentPageLowerHeader("equipment");
			utilsManagement.prepareModel(model);
			return "equipments.html";
	}
	
	 
	@PostMapping("equipments/{festivalId}/addStage")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String addStage(@Valid NewStageForm newStageForm, Errors result, Model model, 
								EquipmentRentingForm equipmentRentingForm, @PathVariable("festivalId") long festivalId) {
				
		Optional<Festival> festivalOP = festivalManagement.findById(utilsManagement.getCurrentFestivalId());
		
		if(!result.hasErrors() && !festivalOP.isPresent()) {
			SalespointIdentifier equipmentsId = newStageForm.getEquipmentsId();
			String name = newStageForm.getName();
			
			Optional<Equipment> equipmentOP = equipmentManagement.findEquipmentById(equipmentsId);
						
			if(!equipmentOP.isPresent()) {
				throw new ResponseStatusException(
						HttpStatus.NOT_FOUND, "entity not found"
				);
			}
			
			Festival festival = festivalOP.get();
			Equipment equipment = equipmentOP.get();
			
			// Stage with same name already exists
			for(Stage aStage : festival.getStages()) {
				if(aStage.getName().equals(name)){
					result.rejectValue("name", null, "Bühne mit diesem Namen existiert bereits.");	
					
					utilsManagement.prepareModel(model);
					return "equipments.html";
				}
			}
			
			
			// maximum stage capacity reached
			if(festival.getStages().size() < festival.getLocation().getStageCapacity()) {
				planEquipmentManagement.rentStage(name, equipment, festival);
				return "redirect:/equipments/" + festivalId;
			} else {
				result.rejectValue("name", null, "Die maximale Bühnenkapazität wurde erreicht.");
				utilsManagement.prepareModel(model);
				return "equipments.html";
			}	
		}	
		utilsManagement.prepareModel(model);
		return "equipments.html";
	}
	
	
	@GetMapping("equipments/{festivalId}/remove/{id}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String getRemoveStageDialog(@PathVariable("id") SalespointIdentifier id, Model model, 
										EquipmentRentingForm equipmentRentingForm, NewStageForm newStageForm ) {
		model.addAttribute("dialog", "remove stage");
		
		Optional<Stage> stage = equipmentManagement.findStageById(id);
		if(stage.isPresent()) {
			Stage current = stage.get();
			model.addAttribute("stage", current);
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
		

		utilsManagement.prepareModel(model);
		return "equipments.html";
	}
	
	
	@PostMapping("equipments/{festivalId}/remove/{id}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String removeStage(@PathVariable("id") SalespointIdentifier id) {
		
		Optional<Festival> festival = festivalManagement.findById(utilsManagement.getCurrentFestivalId());
		if (festival.isPresent()) {
			Optional<Stage> opStage = equipmentManagement.findStageById(id);
			if(opStage.isPresent()) {
				Stage stage = opStage.get(); 
				System.out.println(stage);
				planEquipmentManagement.unrentStage(stage,festival.get().getId());
				System.out.println("after call");
			}
			return "redirect:/equipments/" + festival.get().getId();
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
			
	}
	
	
	@PostMapping("/equipments/{festivalId}/rentEquipmentAmount")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String rentEquipmentAmount(Model model, @Valid EquipmentRentingForm equipementRentingForm, Errors result, 
										@PathVariable("festivalId") long festivalId, NewStageForm newStageForm) {
	
		if(result.hasErrors()) {
			utilsManagement.prepareModel(model);
			return "equipments.html";
		}
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent() && equipmentManagement.findEquipmentById(equipementRentingForm.getEquipmentsId()).isPresent()) {

			//System.out.println(equipmentsId + "     "+ equipmentsAmount);			
			planEquipmentManagement.rentEquipment(equipementRentingForm.getEquipmentsId(), equipementRentingForm.getAmount(), festival.get());
			return "redirect:/equipments/" + festival.get().getId();
			
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
	
}


