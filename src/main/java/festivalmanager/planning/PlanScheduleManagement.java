package festivalmanager.planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.Schedule.TimeSlot;
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
		
	public boolean setShow(LocalDate date, Stage stage, String timeSlotString, long showId, Festival festival, long personId) {

		TimeSlot timeSlot = TimeSlot.valueOf(timeSlotString);
		//System.out.println(timeSlot);
		
		Show show = festival.getShow(showId);		
		Person person = staffManagement.findById(personId).orElse(null);

		boolean success = festival.addSchedule(timeSlot, show, stage, date, person);
		festivalManagement.saveFestival(festival);

		return success;

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
