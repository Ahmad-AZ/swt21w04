package festivalmanager.location;

import static org.assertj.core.api.Assertions.*;

import festivalmanager.AbstractIntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class LocationControllerIntegrationTests extends AbstractIntegrationTests{
	@Autowired LocationController controller;


	//@Test
	//void rejectsUnauthenticatedAccessToController() {
	//
	//	assertThatExceptionOfType(AuthenticationException.class) //
	//			.isThrownBy(() -> controller.locations(new ExtendedModelMap()));
	//}

	// Test locations, locationList not Null
	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToController() {

		ExtendedModelMap model = new ExtendedModelMap();

		controller.locations(model);
		assertThat(model.getAttribute("locationList")).isNotNull();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void locationEditSuccessTest() {

		Model model = new ExtendedModelMap();

		// id from Kulturpalast empirical founded
		Long id = (long) 6;
		String returnedView = controller.locationEdit(id, model);

		assertThat(returnedView).isEqualTo("locationEdit");

	}
	
	
}
