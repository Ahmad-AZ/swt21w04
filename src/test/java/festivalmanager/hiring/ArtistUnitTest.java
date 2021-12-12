package festivalmanager.hiring;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ArtistUnitTest {
	private Artist artist;

	@Test
	void bookArtistSuccess() {
		artist = new Artist();
		assertThat(artist.addBooking(LocalDate.of(2021, 12, 24), LocalDate.of(2021, 12, 30))).isTrue();
	}
	@Test
	void bookArtistReserved() {
		artist = new Artist();
		artist.addBooking(LocalDate.of(2021, 12, 24), LocalDate.of(2021, 12, 30));
		assertThat(artist.addBooking(LocalDate.of(2021, 12, 21), LocalDate.of(2021, 12, 24))).isFalse();
		assertThat(artist.addBooking(LocalDate.of(2021, 12, 24), LocalDate.of(2021, 12, 30))).isFalse();
		assertThat(artist.addBooking(LocalDate.of(2021, 12, 30), LocalDate.of(2022, 01, 31))).isFalse();
		assertThat(artist.addBooking(LocalDate.of(2021, 12, 26), LocalDate.of(2021, 12, 28))).isFalse();
		assertThat(artist.addBooking(LocalDate.of(2021, 12, 21), LocalDate.of(2022, 01, 31))).isFalse();
	}
}
