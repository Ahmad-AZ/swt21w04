package festivalmanager.planning;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.HiringManagement;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.salespointframework.core.Currencies.EURO;

public class PlanOffersControllerIntergrationTest extends AbstractIntegrationTests {
	@Autowired
	PlanOffersController controller;
	@Autowired
	PlanOffersManagement planOffersManagement;
	@Autowired
	FestivalManagement festivalManagement;
	@Autowired
	HiringManagement hiringManagement;

	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToController() {
		ExtendedModelMap model = new ExtendedModelMap();

		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);

		controller.artistOverview(model, festival.getId());
		assertThat(model.getAttribute("artistList")).isNotNull();
		assertThat(model.getAttribute("noArtist")).isEqualTo(true);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void ControllerAccessWithArtistBookedTest() {

		Artist artist = new Artist("name", Money.of(50, EURO), 50);
		hiringManagement.saveArtist(artist);
		ExtendedModelMap model = new ExtendedModelMap();

		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festival.addArtist(artist);
		festivalManagement.saveFestival(festival);
		Set<Artist> artists = new HashSet<>();
		artists.add(artist);

		controller.artistOverview(model, festival.getId());
		assertThat(model.getAttribute("artistList")).isNotNull();
		assertThat(model.getAttribute("bookedArtistId")).isEqualTo(artists);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void artistDetailArtistBookedTest() {
		ExtendedModelMap model = new ExtendedModelMap();
		Artist artist = new Artist();
		hiringManagement.saveArtist(artist);
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));

		festival.addArtist(artist);
		festivalManagement.saveFestival(festival);

		String returnedView = controller.artistDetail(artist.getId(), festival.getId(),  model);
		assertThat(returnedView).isEqualTo("artistDetailPlan");
		assertThat(model.getAttribute("artist")).isEqualTo(artist);
		assertThat(model.getAttribute("ArtistCurrentlyBooked")).isEqualTo(true);
		assertThat(model.getAttribute("hasBookings")).isEqualTo(false);

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void bookArtistSuccessTest() {
		Artist artist = new Artist();
		hiringManagement.saveArtist(artist);

		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);

		String returnedView = controller.bookArtist(artist.getId(), false, festival.getId(), null);
		assertThat(returnedView).isEqualTo("redirect:/artistOverview/"+festival.getId());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void unbookArtistInDetailTest() {

		Artist artist = new Artist();
		artist.addBooking(LocalDate.now(), LocalDate.now().plusDays(4));
		hiringManagement.saveArtist(artist);

		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festival.addArtist(artist);
		festivalManagement.saveFestival(festival);

		String returnedView = controller.bookArtist(artist.getId(),true, festival.getId(), null);
		assertThat(returnedView).isEqualTo("redirect:/artistOverview/" + festival.getId());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void unbookArtistInOverviewTest() {

		Artist artist = new Artist();
		artist.addBooking(LocalDate.now(), LocalDate.now().plusDays(4));
		hiringManagement.saveArtist(artist);

		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festival.addArtist(artist);
		festivalManagement.saveFestival(festival);

		String returnedView = controller.unbookArtist(festival.getId());
		assertThat(returnedView).isEqualTo("redirect:/artistOverview/" + festival.getId());
	}
}

