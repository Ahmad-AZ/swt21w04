package festivalmanager.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import festivalmanager.festival.Festival;

public class LocationUnitTest {
    private Location location;

	@Test 
	void bookLocationSuccess() {
		location = new Location();
		assertThat(location.addBooking(LocalDate.of(2021, 12, 24), LocalDate.of(2021, 12, 30))).isTrue();
	}
	
	@Test 
	void bookLocationReserved() {
		location = new Location();
		location.addBooking(LocalDate.of(2021, 12, 24), LocalDate.of(2021, 12, 30));
		assertThat(location.addBooking(LocalDate.of(2021, 12, 21), LocalDate.of(2021, 12, 24))).isFalse();
		assertThat(location.addBooking(LocalDate.of(2021, 12, 24), LocalDate.of(2021, 12, 30))).isFalse();
		assertThat(location.addBooking(LocalDate.of(2021, 12, 30), LocalDate.of(2022, 01, 31))).isFalse();
		assertThat(location.addBooking(LocalDate.of(2021, 12, 26), LocalDate.of(2021, 12, 28))).isFalse();
		assertThat(location.addBooking(LocalDate.of(2021, 12, 21), LocalDate.of(2022, 01, 31))).isFalse();		
	}
}
