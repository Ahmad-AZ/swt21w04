package festivalmanager.finances;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalController;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.FestivalRepository;
import festivalmanager.finances.FinancesManagement;
import festivalmanager.hiring.Artist;
import festivalmanager.location.Location;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.salespointframework.core.Currencies.EURO;


class FinancesTests {


	@Test
	void testFinancesManagement() {

		FestivalRepository festivalRepository = mock(FestivalRepository.class);
		Festival testFestival = new Festival("Testfestival");
		testFestival.setStartDate(LocalDate.of(2021, 12, 3));
		testFestival.setEndDate(LocalDate.of(2021, 12, 6));
		when(festivalRepository.findById(any())).thenReturn(Optional.of(testFestival));

		FestivalManagement festivalManagement = new FestivalManagement(festivalRepository);
		festivalManagement.saveFestival(testFestival);

		Location testLocation = new Location();
		testLocation.setPricePerDay(Money.of(500, EURO));
		testFestival.setLocation(testLocation);
		Artist testArtist = new Artist();
		testFestival.addArtist(testArtist);

		FinancesManagement financesManagement = new FinancesManagement(festivalManagement);
		financesManagement.updateFestival(testFestival.getId());
		FinancesController financesController = new FinancesController(financesManagement, festivalManagement);
		Model testModel = new ExtendedModelMap();
		financesController.financesPage(testModel, testFestival.getId());

		assertThat(testModel.getAttribute("artistsCost")).isEqualTo("11010.10");
		assertThat(testModel.getAttribute("locationCost")).isEqualTo("2000.00");
		assertThat(testModel.getAttribute("cost")).isEqualTo("13010.10");
		assertThat(testModel.getAttribute("revenue")).isEqualTo("0.00");
		assertThat(testModel.getAttribute("profit")).isEqualTo("-13010.10");

	}


}
