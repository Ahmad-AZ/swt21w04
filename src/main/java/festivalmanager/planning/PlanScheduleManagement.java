package festivalmanager.planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.Schedule.TimeSlot;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.Show;
import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;

@Service
@Transactional
public class PlanScheduleManagement {

	private final FestivalManagement festivalManagement;
	private final StaffManagement staffManagement;
	
	public PlanScheduleManagement(FestivalManagement festivalManagement,
								  StaffManagement staffManagement) {
		this.festivalManagement = festivalManagement;
		this.staffManagement = staffManagement;
	}
		
	public boolean setShow(LocalDate date, Stage stage, String timeSlotString, long showId, long festivalId, long personId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			TimeSlot timeSlot = TimeSlot.valueOf(timeSlotString);
			//System.out.println(timeSlot);
			
			// find show
			Show show = null;
			for(Artist anArtist : current.getArtist()) {
				if(anArtist.getShow(showId) != null) {
					show = anArtist.getShow(showId);
					break;
				}
			}
						
			Person person = null;
			for(Person aPerson : staffManagement.findByFestivalIdAndRole(festivalId, "SECURITY")) {
				if(aPerson.getId() == personId) {
					person = aPerson;
					break;
				}
			}
			
			boolean success = true;
			success = current.addSchedule(timeSlot, show, stage, date, person);
			festivalManagement.saveFestival(current);

			return success;
		} else {
			return false;
		}

	}
	
	public List<Person> getAvailableSecurity(Festival festival, LocalDate date, String timeSlotString, SalespointIdentifier stageId){
		TimeSlot timeSlot = TimeSlot.valueOf(timeSlotString);
		
		List<Person> securitys = new ArrayList<>();
		List<Person> unavailableSecuritys = festival.getUnavailableSecuritys(date, timeSlot, stageId);
		
		for(Person aPerson : staffManagement.findByFestivalIdAndRole(festival.getId(), "SECURITY")){
			if(!unavailableSecuritys.contains(aPerson)) {
				securitys.add(aPerson);
			}
		}
		return securitys;
	
	}
}
