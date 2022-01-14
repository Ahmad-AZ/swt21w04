package festivalmanager.hiring;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.salespointframework.core.Currencies.EURO;

/**
 * Unit tests for {@link Artist}
 *
 * @author Tuan Giang Trinh
 */
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

	@Test
	void newArtistForm() {
		NewArtistForm form = new NewArtistForm("name", 50.00,50);
		assertThat(form.getName()).isEqualTo("name");
		assertThat(form.getPrice()).isEqualTo(50.00);
		assertThat(form.getStageTechnician()).isEqualTo(50);
	}

	@Test
	void setArtist() {
		artist = new Artist("name1", Money.of(50.00, EURO), 50);
		artist.setName("name2");
		assertThat(artist.getName()).isEqualTo("name2");
		artist.setPrice(Money.of(60.0, EURO));
		assertThat(artist.getPrice()).isEqualTo(Money.of(60.0, EURO));
		Show show = new Show("showName", Duration.ofMinutes(120));
		artist.addShow(show);
		assertThat(artist.getShow(show.getId())).isEqualTo(show);
		assertThat(show.getPerformance()).isEqualTo(120);
	}
}
