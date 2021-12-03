package festivalmanager.planning;

import java.util.ArrayList;
import java.util.List;
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
import festivalmanager.Equipment.Equipments;
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
			List<Equipment> eq = new ArrayList<>();
			eq = equipmentManagement.findAll().toList();
			System.out.println("ListId: " + eq.get(0).getId());
			List<Equipments> equipmentsList = new ArrayList<>();
			//List<Equipments> festivalEquipments = current.getEquipments();
			System.out.println(equipmentManagement.findById((long) 1).get().getName());
			for(Equipment aEquipment : equipmentManagement.findAll()) {
				for(Equipments aFestivalEquipments : current.getEquipments()) {
					if(aFestivalEquipments.getEquipment().equals(aEquipment)) {
						// add currently rented amount of aEquipment to equipmentsList
						equipmentsList.add(aFestivalEquipments);
					}
				}
				// add 0 value for current aEquipment to equipmentsList
				equipmentsList.add(new Equipments(aEquipment, 0));
				System.out.println("aEquipment" + aEquipment.getId());
			}
			model.addAttribute("equipmentsList", equipmentsList);
			
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
