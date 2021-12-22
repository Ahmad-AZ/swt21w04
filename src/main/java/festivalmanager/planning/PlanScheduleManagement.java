package festivalmanager.planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.salespointframework.core.SalespointIdentifier;
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
	
	
	public boolean setShow(LocalDate date, Stage stage, String timeSlotString, long showId, long festivalId, long personId) {
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
			
//			// proof stage exists for current festival
//			Stage stage = this.getStages(current,stageId);			
//			if(stage == null) {
//				return false;
//			}
			
			
			Person person = null;
			for(Person aPerson : staffManagement.findByFestivalIdAndRole(festivalId, "SECURITY")) {
				if(aPerson.getId() == personId) {
					person = aPerson;
					break;
				}
			}
			
			boolean success = true;
			
			// TODO improve if,else
			// if show attribute is null set show null
//			if(show == null) {
//				success =  current.removeSchedule(timeSlot, stage, date);
//			}
//			else {
				success = current.addSchedule(timeSlot, show, stage, date, person);
				festivalManagement.saveFestival(current);
//			}
			return success;
		} else {
			return false;
		}

	}
	
	public List<Person> getAvailableSecurity(Festival festival, LocalDate date, String timeSlotString){
		TimeSlot timeSlot = TimeSlot.valueOf(timeSlotString);
		List<Person> securitys = new ArrayList<>();
		List<Person> unavailableSecuritys = new ArrayList<>();
		for(Schedule aSchedule : festival.getSchedules()) {
			if(aSchedule.getDate().equals(date) && aSchedule.getTimeSlot().equals(timeSlot)) {
				unavailableSecuritys.add(aSchedule.getSecurity());
			}
		}
		System.out.println("uaS" + unavailableSecuritys);
		for(Person aPerson : staffManagement.findByFestivalIdAndRole(festival.getId(), "SECURITY")){
			if(!unavailableSecuritys.contains(aPerson)) {
				securitys.add(aPerson);
			}
		}
		System.out.println("avS" + securitys);
		return securitys;
	
	}
	
	public boolean setSecurity(LocalDate date, Stage stage, String timeSlotString, long personId, long festivalId){
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			TimeSlot timeSlot = TimeSlot.valueOf(timeSlotString);
			System.out.println(timeSlot);
			
			
			Person person = null;
			for(Person aPerson : staffManagement.findByFestivalIdAndRole(festivalId, "SECURITY")) {
				if(aPerson.getId() == personId) {
					person = aPerson;
					break;
				}
			}
//			if(person == null) {
//				success = current.removeSecurity()
//			}
			
			boolean success = true;
			//success = current.addSchedule(timeSlot, show, stage, date);
			festivalManagement.saveFestival(current);
			
			

			return success;
		} else {
			return false;
		}
	}
			
	
	public Stage getStages(Festival festival, SalespointIdentifier stageId) {
		for(Stage aStage : festival.getStages()) {
			if(aStage.getId().equals(stageId)) {
				return aStage;
			}		
		}
		return null;
	}
}
