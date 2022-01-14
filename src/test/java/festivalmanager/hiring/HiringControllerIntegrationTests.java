package festivalmanager.hiring;

import festivalmanager.AbstractIntegrationTests;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.salespointframework.core.Currencies.EURO;

/**
 * Integration tests for {@link HiringController} that interact with the controller directly.
 */
public class HiringControllerIntegrationTests extends AbstractIntegrationTests {
	@Autowired HiringController controller;
	@Autowired HiringManagement lm;

	/**
	 * Does not use any authentication and should raise a security exception.
	 */
	@Test
	void rejectsUnauthenticatedAccessToController() {

		assertThatExceptionOfType(AuthenticationException.class) //
				.isThrownBy(() -> controller.artists(new ExtendedModelMap()));
	}

	/**
	 * Uses {@link WithMockUser} to simulate access by a user with admin role.
	 */
	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToController() {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.artists(model);
		assertThat(model.getAttribute("artistList")).isNotNull();
	}
	@Test
	@WithMockUser(roles = "ADMIN")
	public void artistEditSuccessTest() {
		Model model = new ExtendedModelMap();
		Long id = (long) 10;
		Money price = Money.of(100, EURO);
		Artist artist = new Artist();
		artist.setPrice(price);
		lm.saveArtist(artist);

		Long artistId = artist.getId();
		String returnedView = controller.artistEdit(artistId, model);
		assertThat(returnedView).isEqualTo("artistEdit");
		assertThat(model.getAttribute("artist")).isNotNull();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void artistDetailSuccessTest() {
		ExtendedModelMap model = new ExtendedModelMap();

		Artist artist = new Artist();
		lm.saveArtist(artist);

		String returnedView = controller.artistDetail(artist.getId(), model);
		assertThat(returnedView).isEqualTo("artistDetail");
		assertThat(model.getAttribute("artist")).isEqualTo(artist);
		assertThat(model.getAttribute("hasBookings")).isEqualTo(false);

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void artistDetailWithBookingsTest() {
		ExtendedModelMap model = new ExtendedModelMap();

		Artist artist = new Artist();
		artist.addBooking(LocalDate.now(), LocalDate.now().plusDays(12));
		lm.saveArtist(artist);

		String returnedView = controller.artistDetail(artist.getId(), model);
		assertThat(returnedView).isEqualTo("artistDetail");
		assertThat(model.getAttribute("artist")).isEqualTo(artist);
		assertThat(model.getAttribute("hasBookings")).isEqualTo(true);
	}



	@Test
	@WithMockUser(roles = "ADMIN")
	public void artistDetailTest() {
		Model model = new ExtendedModelMap();

		Artist artist = new Artist();
		lm.saveArtist(artist);

		String returnedView = controller.artistDetail(artist.getId(), model);
		assertThat(returnedView).isEqualTo("artistDetail");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToAddArtist() {
		ExtendedModelMap model = new ExtendedModelMap();
		String returnedView = controller.newArtist(model, new NewArtistForm("artist", 50.00, 10));
		assertThat(returnedView).isEqualTo("newArtist");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getRemoveArtistDialogSuccessTest() {

		ExtendedModelMap model = new ExtendedModelMap();
		Artist artist = new Artist("artist", Money.of(50, EURO), 50);
		lm.saveArtist(artist);

		String returnedView = controller.getRemoveArtistDialog(artist.getId(), model);
		assertThat(returnedView).isEqualTo("/artists");

		assertThat(model.getAttribute("dialog")).isEqualTo("remove_artist");
		assertThat(model.getAttribute("currentName")).isEqualTo(artist.getName());
		assertThat(model.getAttribute("currentId")).isEqualTo(artist.getId());

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getRemoveArtistDialogFailureTest() {
		ExtendedModelMap model = new ExtendedModelMap();
		Artist artist = new Artist("Artist", Money.of(50, EURO), 50);
		String returnedView = controller.getRemoveArtistDialog(artist.getId(), model);

		assertThat(returnedView).isEqualTo("/artists");
		assertThat(model.getAttribute("dialog")).isEqualTo("remove_artist");
		assertThat(model.getAttribute("currentName")).isEqualTo("");
		assertThat(model.getAttribute("currentId")).isEqualTo(artist.getId());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createArtistSuccessTest() {

		String returnedView = controller.createNewArtist(new NewArtistForm("Artist", 50.0, 50), mock(Errors.class));
		assertThat(returnedView).isEqualTo("redirect:/artists");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void saveArtistSuccessTest() {
		ExtendedModelMap model = new ExtendedModelMap();

		Artist artist = new Artist("Artist", Money.of(123.30, EURO), 50);
		lm.saveArtist(artist);
		String returnedView = controller.saveArtist(new NewArtistForm("Artist", 123.0, 50), mock(Errors.class), artist.getId(), model);
		assertThat(returnedView).isEqualTo("redirect:/artists");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void saveArtistFailureTest() {
		ExtendedModelMap model = new ExtendedModelMap();

		Errors errors = mock(Errors.class);
		when(errors.hasErrors()).thenReturn(true);

		Artist existingArtist = new Artist("Artist", Money.of(123.30, EURO), 23);
		lm.saveArtist(existingArtist);
		Artist artist = new Artist("Artist2", Money.of(123.30, EURO), 23);
		lm.saveArtist(artist);

		// reject same name
		String returnedView = controller.saveArtist(new NewArtistForm("Artist", 25.0, 25), errors, artist.getId(), model);
		assertThat(returnedView).isEqualTo("artistEdit");
		// reject negative Money
		returnedView = controller.saveArtist(new NewArtistForm("Artist2",25.0, 26), errors, artist.getId(), model);
		assertThat(returnedView).isEqualTo("artistEdit");
		// reject wrong MoneyString
		returnedView = controller.saveArtist(new NewArtistForm("Artist2", 27.0, 28), errors, artist.getId(), model);
		assertThat(returnedView).isEqualTo("artistEdit");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getRemoveArtistDialogWithBookingsSuccessTest() {

		ExtendedModelMap model = new ExtendedModelMap();
		Artist artist = new Artist("artist", Money.of(123.30, EURO), 65);
		artist.addBooking(LocalDate.now(), LocalDate.now().plusDays(7));
		lm.saveArtist(artist);
		String returnedView = controller.getRemoveArtistDialog(artist.getId(), model);
		assertThat(returnedView).isEqualTo("/artists");
		assertThat(model.getAttribute("dialog")).isEqualTo("remove_artist");
		assertThat(model.getAttribute("currentName")).isEqualTo(artist.getName());
		assertThat(model.getAttribute("currentId")).isEqualTo(artist.getId());

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void removeArtistSuccessTest() {

		Artist artist = new Artist("Artist2", Money.of(123.30, EURO), 24);
		lm.saveArtist(artist);

		String returnedView = controller.removeArtist(artist.getId());
		assertThat(returnedView).isEqualTo("redirect:/artists");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void removeArtistFailureTest() {

		Artist artist = new Artist("Artist2", Money.of(123.30, EURO), 14);
		artist.addBooking(LocalDate.now(), LocalDate.now().plusDays(12));
		lm.saveArtist(artist);

		String returnedView = controller.removeArtist(artist.getId());
		assertThat(returnedView).isEqualTo("redirect:/artists");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void removeArtistNotExistsTest() {

		Artist artist = new Artist("Artist2", Money.of(123.30, EURO), 17);

		String returnedView = controller.removeArtist(artist.getId());
		assertThat(returnedView).isEqualTo("/artistsDeleteFailed");
	}
}
