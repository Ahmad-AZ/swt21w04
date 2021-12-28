package festivalmanager.hiring;

import festivalmanager.AbstractIntegrationTests;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.salespointframework.core.Currencies.EURO;

public class HiringControllerIntegrationTests extends AbstractIntegrationTests {
	@Autowired HiringController controller;
	@Autowired HiringManagement lm;

	@Test
	void rejectsUnauthenticatedAccessToController() {

		assertThatExceptionOfType(AuthenticationException.class) //
				.isThrownBy(() -> controller.artists(new ExtendedModelMap()));
	}

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
	public void artistDetailTest() {
		Model model = new ExtendedModelMap();

		Artist artist = new Artist();
		lm.saveArtist(artist);

		String returnedView = controller.artistDetail(artist.getId(), model);
		assertThat(returnedView).isEqualTo("artistDetail");
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToAddLocation() {
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
}
