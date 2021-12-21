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
import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;

@Service
@Transactional
public class PlanScheduleManagement {

	private final FestivalManagement festivalManagement;
	private final EquipmentManagement equipmentManagement;
	private final StaffManagement staffManagement;
	
	public PlanScheduleManagement(FestivalManagement festivalManagement, EquipmentManagement equipmentManagement, StaffManagement staffManagement) {
		this.equipmentManagement = equipmentManagement;
		this.festivalManagement = festivalManagement;
		this.staffManagement = staffManagement;
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
		} else {
			return null;
		}
	}
	
	
	public boolean setShow(LocalDate date, long stageId, String timeSlotString, long showId, long festivalId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			TimeSlot timeSlot = TimeSlot.valueOf(timeSlotString);
			System.out.println(timeSlot);
			
			// show attribute ne null
			Show show = null;
			for(Artist anArtist : current.getArtist()) {
				if(anArtist.getShow(showId) != null) {
					show = anArtist.getShow(showId);
					break;
				}
			}
			
			// proof stage exists for current festival
			Stage stage = this.getStages(current,stageId);			
			if(stage == null) {
				return false;
			}
			
			boolean success = true;
			
			// if show attribute is null clear schedule from festival
			if(show == null) {
				success = current.removeSchedule(timeSlot, stage, date);
			}
			else {
				success = current.addSchedule(timeSlot, show, stage, date);
				festivalManagement.saveFestival(current);
			}
			return success;
		} else {
			return false;
		}

	}
	
	public List<Person> getAvailableSecurity(Festival festival, LocalDate date, String timeSlotString){
		TimeSlot timeSlot = TimeSlot.valueOf(timeSlotString);
		List<Person> securitys = new ArrayList<>();
		for(Person aPerson : staffManagement.findByFestivalIdAndRole(festival.getId(), "SECURITY")){
			for(Schedule aSchedule : festival.getSchedules()) {
				if(!(aSchedule.getDate().equals(date) || aSchedule.getTimeSlot().equals(timeSlot) || aSchedule.getSecurity().equals(aPerson))) {
					securitys.add(aPerson);
				}
			}
		}		
		return securitys;
	
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
