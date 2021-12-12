package festivalmanager.planning;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.HiringManagement;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlanOffersManagementUnitTest {
	FestivalManagement festivalManagement = mock(FestivalManagement.class);
	HiringManagement hiringManagement = mock(HiringManagement.class);
	PlanOffersManagement planOffersManagement = new PlanOffersManagement(hiringManagement, festivalManagement);

	Artist artist = mock(Artist.class);
	Festival festival = mock(Festival.class);

	@Test
	void bookArtistSucess() {
		when(artist.addBooking(any(), any())).thenReturn(true);

		assertThat(planOffersManagement.bookArtist(artist, festival)).isTrue();
	}
	@Test
	void bookArtistReserved() {
		when(artist.addBooking(any(), any())).thenReturn(false);
		assertThat(planOffersManagement.bookArtist(artist, festival)).isFalse();
	}
}
