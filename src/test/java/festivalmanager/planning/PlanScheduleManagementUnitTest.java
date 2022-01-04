package festivalmanager.planning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.util.Streamable;

import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Show;
import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;

public class PlanScheduleManagementUnitTest {
	FestivalManagement festivalManagement = mock(FestivalManagement.class);
	StaffManagement staffManagement = mock(StaffManagement.class);
	PlanScheduleManagement planscheduleManagement = new PlanScheduleManagement(festivalManagement, staffManagement);
	
	Stage stage = mock(Stage.class);
	Festival festival = mock(Festival.class);
	
	
	@Test 
	void setShowSuccessTest() {
			
		when(festival.getShow(any(long.class))).thenReturn(mock(Show.class));
		when(staffManagement.findById(any(long.class))).thenReturn(Optional.of(mock(Person.class)));
		when(festival.addSchedule(any(), any(), any(), any(), any())).thenReturn(true);
		
		assertThat(planscheduleManagement.setShow(LocalDate.now(), stage, "TS1", 1, festival, 2)).isEqualTo(true);
	}
	
	@Test 
	void setShowFailureTest() {
			
		when(festival.getShow(any(long.class))).thenReturn(mock(Show.class));
		when(staffManagement.findById(any(long.class))).thenReturn(Optional.of(mock(Person.class)));
		when(festival.addSchedule(any(), any(), any(), any(), any())).thenReturn(false);
		
		assertThat(planscheduleManagement.setShow(LocalDate.now(), stage, "TS1", 1, festival, 2)).isEqualTo(false);
	}
	
	@Test
	void getAvailableSecurityTest() {
		Person person1 = mock(Person.class);
		Person person2 = mock(Person.class);
		
		List<Person> personsUA = new ArrayList<>();
		personsUA.add(person1);
		personsUA.add(person2);
		
		when(festival.getUnavailableSecuritys(any(), any(), any(SalespointIdentifier.class))).thenReturn(personsUA);
		
		List<Person> persons = new ArrayList<>();
		persons.add(person1);
		persons.add(person2);
		persons.add(mock(Person.class));
		persons.add(mock(Person.class));
		
		when(staffManagement.findByFestivalIdAndRole(any(long.class), any(String.class))).thenReturn(Streamable.of(persons));
		
//		for(Person aPerson : staffManagement.findByFestivalIdAndRole((long) 1,"ddd")) {
//			System.out.println(aPerson);
//		}
//		System.out.println(persons);
		
		assertThat(planscheduleManagement.getAvailableSecurity(festival, LocalDate.now(), "TS1", mock(SalespointIdentifier.class))).hasSize(2);
	}
}

