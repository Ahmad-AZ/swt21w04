package festivalmanager.planning;


import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.EquipmentManagement;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;


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
	
	// shows Locations Overview
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
				equipmentsMap.put(anEquipment, amount);
			}

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
	
	@GetMapping("/rentEquipmentAmount")
	public String rentEquipmentAmount(Model model,
					  @RequestParam("equipmentsId") long equipmentsId,
					  @RequestParam("equipmentsAmount") long equipmentsAmount) {
		System.out.println("inputid" + equipmentsId);
		planEquipmentManagement.rentEquipment(equipmentsId, equipmentsAmount, currentFestival);
		
		return "redirect:/equipmentsPre1";
	}
}
