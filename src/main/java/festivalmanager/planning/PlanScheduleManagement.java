package festivalmanager.planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.Equipment.EquipmentType;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.Schedule;
import festivalmanager.festival.Schedule.TimeSlot;
import festivalmanager.hiring.Artist;
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
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			List<Show> shows = new ArrayList<>();
			for(Artist anArtist : current.getArtist()) {
				for(Show aShow : anArtist.getShows()) {
					shows.add(aShow);
				}
			}
			return shows;
		}else {
			return null;
		}
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
	
	public boolean setShow(LocalDate date, long stageId, String timeSlotString, long showId, long festivalId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			TimeSlot timeSlot = TimeSlot.valueOf(timeSlotString);
			System.out.println(timeSlot);
			
			
			// proof stage exists for current festival
			Stage stage = this.getStages(current,stageId);			
			if(stage == null) {
				return false;
			}
			
			
			// show attribute ne null
			Show show = null;
			for(Artist anArtist : current.getArtist()) {
				for(Show aShow : anArtist.getShows()) {
					if(aShow.getId() == showId) {
						show = aShow;
					}
				}
			}
			// else clear schedule from festival
			if(show == null) {
				return current.removeSchedule(timeSlot, stage, date);
			}
			
			
			boolean success = true;
			success = current.addSchedule(timeSlot, show, stage, date);
			festivalManagement.saveFestival(current);
			return success;
		} else {
			return false;
		}

	}
	
	public Stage getStages(Festival festival, long stageId) {
		for(Stage aStage : festival.getStages()) {
			if(aStage.getId() == stageId) {
				return aStage;
			}		
		}
		return null;
	}
}
