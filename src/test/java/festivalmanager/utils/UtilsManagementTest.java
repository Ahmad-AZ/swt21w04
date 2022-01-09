package festivalmanager.utils;


import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.FestivalRepository;
import festivalmanager.hiring.HiringManagement;
import festivalmanager.location.LocationManagement;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UtilsManagementTest {


	@Test
	void testUtilsManagement() {

		FestivalRepository festivalRepository = mock(FestivalRepository.class);
		FestivalManagement festivalManagement = new FestivalManagement(
				festivalRepository,
				mock(LocationManagement.class),
				mock(HiringManagement.class));

		Festival testFestival = new Festival("Test Festival",
				LocalDate.of(2023, 8, 29),
				LocalDate.of(2023, 9, 3));

		when(festivalRepository.findById(any())).thenReturn(Optional.of(testFestival));
		festivalManagement.saveFestival(testFestival);

		Model testModel = new ExtendedModelMap();

		UtilsManagement utilsManagement = new UtilsManagement(festivalManagement);
		utilsManagement.prepareModel(testModel, testFestival.getId());
		assertThat(testModel.getAttribute("festivalName")).isEqualTo("Test Festival");
		assertThat(testModel.getAttribute("festivalId")).isEqualTo(testFestival.getId());
	}

}
