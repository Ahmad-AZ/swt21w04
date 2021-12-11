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
}
