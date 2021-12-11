package festivalmanager.festival;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;



public class FestivalManagementUnitTest {
	FestivalManagement festivalManagement = mock(FestivalManagement.class);
	
	@Test 
	void createNewFestival() {
		Festival festival = mock(Festival.class);
		when(festivalManagement.createFestival(any())).thenReturn(festival);
		LocalDate startDate = LocalDate.of(2021, 12, 24);
		LocalDate endDate = LocalDate.of(2021, 12, 30);
		NewFestivalForm form = new NewFestivalForm("name", startDate, endDate);
		Festival savedFestival = festivalManagement.createFestival(form);
		
		verify(festivalManagement, times(1)).createFestival(eq(form));
		
		Optional<Festival> festivalOP = festivalManagement.findById(savedFestival.getId());
		if (festivalOP.isPresent()) {
			Festival current = festivalOP.get();
			
			assertThat(current.getName()).isEqualTo("name");
			assertThat(current.getStartDate()).isEqualTo(startDate);
			assertThat(current.getEndDate()).isEqualTo(endDate);
		}
	}
}
