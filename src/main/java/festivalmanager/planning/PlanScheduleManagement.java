package festivalmanager.planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.Equipment.EquipmentType;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Show;

@Service
@Transactional
public class PlanScheduleManagement {

	private final FestivalManagement festivalManagement;
	private final EquipmentManagement equipmentManagement;
	
	public PlanScheduleManagement(FestivalManagement festivalManagement, EquipmentManagement equipmentManagement) {
		this.equipmentManagement = equipmentManagement;
		this.festivalManagement = festivalManagement;
	}
	
	public List<Show> getShows(long festivalId){
		
		return null;
	}
	
	public List<String> getStages(long festivalId){
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			Map<Long,Long> equipmentIds = current.getEquipments();
			
			Long stageAmount = (long) 0;
			//List<Long> stageIds = new ArrayList<>();
			for(Long anId : equipmentIds.keySet()) {
				Optional<Equipment> equipment = equipmentManagement.findById(anId);
				if(equipment.isPresent()) {
					if(equipment.get().getType().equals(EquipmentType.STAGE)) {
//						stageIds.add(anId);
						stageAmount = equipmentIds.get(anId);
					}
				}
			}
			List<String> result = new ArrayList<>();
			for(int i = 1; i<=stageAmount; i++) {
				result.add("Bühne " + i);
			}
			return result;
		}else {
			return null;
		}
	}
}
