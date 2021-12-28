package festivalmanager.festival;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;

import festivalmanager.hiring.Artist;
import festivalmanager.hiring.Show;
import festivalmanager.location.Location;

public class FestivalUnitTest {
	 
    private Festival festival;

	@Test 
	void setFestivalName() {
		festival = new Festival("Beispiel", LocalDate.of(2021, 12, 24), LocalDate.of(2021, 12, 26));
		festival.setName("Beispiel1");

		assertThat(festival.getName()).isEqualTo("Beispiel1");
	}
	
	@Test
	void getFestivalShows() {
		festival = new Festival("Beispiel", LocalDate.of(2021, 12, 24), LocalDate.of(2021, 12, 26));
		Artist artist = mock(Artist.class);
		List<Show> shows = new ArrayList<>();
		shows.add(new Show("s1", Duration.ofMinutes(49)));
		shows.add(new Show("s2", Duration.ofMinutes(49)));
		when(artist.getShows()).thenReturn(shows);
		festival.addArtist(artist);
		//assertThat(festival.getArtist().)
	}
}
