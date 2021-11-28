package festivalmanager.location;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import static org.salespointframework.core.Currencies.EURO;
import festivalmanager.AbstractIntegrationTests;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import static org.mockito.Mockito.*;



public class LocationControllerIntegrationTests extends AbstractIntegrationTests{
	
	@Autowired LocationController controller;
	@Autowired LocationManagement lm ;

//	@Test
//	void rejectsUnauthenticatedAccessToController() {
//
//		assertThatExceptionOfType(AuthenticationException.class) //
//				.isThrownBy(() -> controller.locations(new ExtendedModelMap()));
//	}

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
		Location location = new Location();
		location.setPricePerDay(Money.of(1111, EURO));
		lm.saveLocation(location);
		//LocationRepository lr = mock(LocationRepository.class);


		// when(lm.findById(any())).thenReturn(Optional.of(location));
		String returnedView = controller.locationEdit(location.getId(), model);

		assertThat(returnedView).isEqualTo("locationEdit");

	}
	
	
}
