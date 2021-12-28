package festivalmanager.festival;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Schedule.TimeSlot;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.Show;
import festivalmanager.staff.Person;

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
		shows.add(new Show());
		shows.add(new Show());
		when(artist.getShows()).thenReturn(shows);
		festival.addArtist(artist);
		assertThat(festival.getShows()).hasSize(2);
	}
	
	@Test
	void FestivalAddSchedule() {
		festival = new Festival("Beispiel", LocalDate.of(2021, 12, 24), LocalDate.of(2021, 12, 26));
		Show show = new Show("BeispielShow",Duration.ofMinutes(49));
		Stage stage = new Stage();
		assertThat(festival.addSchedule(TimeSlot.TS1, show, stage, LocalDate.of(2021, 12, 26), null)).isTrue();
		assertThat(festival.getScheduleShowName(TimeSlot.TS1, stage, LocalDate.of(2021, 12, 26))).isEqualTo(show.getName());
		assertThat(festival.getScheduleSecurityName(TimeSlot.TS1, stage, LocalDate.of(2021, 12, 26))).isEqualTo("Keine");
		
		Show show1 = new Show("testShow", Duration.ofMinutes(49));
		assertThat(festival.addSchedule(TimeSlot.TS1, show1, stage, LocalDate.of(2021, 12, 26), null)).isTrue();
		assertThat(festival.getScheduleShowName(TimeSlot.TS1, stage, LocalDate.of(2021, 12, 26))).isEqualTo(show1.getName());
		assertThat(festival.getScheduleSecurityName(TimeSlot.TS1, stage, LocalDate.of(2021, 12, 26))).isEqualTo("Keine");
	}
	
}
