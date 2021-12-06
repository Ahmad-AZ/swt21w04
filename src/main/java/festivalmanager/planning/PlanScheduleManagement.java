package festivalmanager.planning;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
