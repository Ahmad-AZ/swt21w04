package festivalmanager.planning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.location.Location;
import festivalmanager.location.LocationManagement;

public class PlanLocationManagementUnitTest {
	FestivalManagement festivalManagement = mock(FestivalManagement.class);
	LocationManagement locationManagement = mock(LocationManagement.class);
	PlanLocationManagement planLocationManagement = new PlanLocationManagement(festivalManagement, locationManagement);
	
	Location location = mock(Location.class);
	Festival festival = mock(Festival.class);
	
	@Test 
	void bookLocationSuccess() {
			
		when(location.addBooking(any(), any())).thenReturn(true);
		
		assertThat(planLocationManagement.bookLocation(location, festival)).isTrue();	
	}
	
	@Test 
	void bookLocationReserved() {
		when(location.addBooking(any(), any())).thenReturn(false);
		assertThat(planLocationManagement.bookLocation(location, festival)).isFalse();	

	}
	
	@Test
	void unbookLocation() {
		
	}
}
