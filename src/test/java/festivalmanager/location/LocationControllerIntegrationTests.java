package festivalmanager.location;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import static org.salespointframework.core.Currencies.EURO;
import festivalmanager.AbstractIntegrationTests;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;



public class LocationControllerIntegrationTests extends AbstractIntegrationTests{
	
	@Autowired LocationController controller;
	@Autowired LocationManagement lm ;

	@Test
	void rejectsUnauthenticatedAccessToController() {

		assertThatExceptionOfType(AuthenticationException.class) //
				.isThrownBy(() -> controller.locations(new ExtendedModelMap()));
	}

	// Test locations, locationList not Null
	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToController() {

		ExtendedModelMap model = new ExtendedModelMap();

		controller.locations(model);
		assertThat(model.getAttribute("locationList")).isNotNull();
		
		// not sure if that works later with more Festivals
//		Iterable<Object> object = (Iterable<Object>) model.asMap().get("locationList");
//		assertThat(object).hasSize(6);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void locationEditSuccessTest() {

		Model model = new ExtendedModelMap();


		Money price = Money.of(1111, EURO);
		Location location = new Location();
		location.setPricePerDay(price);
		lm.saveLocation(location);
		//LocationRepository lr = mock(LocationRepository.class);

		Long locationId = location.getId();
		System.out.println(locationId);
		// when(lm.findById(any())).thenReturn(Optional.of(location));
		String returnedView = controller.locationEdit(locationId, model);
		assertThat(returnedView).isEqualTo("locationEdit");
		
		assertThat(model.getAttribute("location")).isNotNull();
		assertThat(model.getAttribute("pricePerDay")).isEqualTo(price.getNumber().doubleValue());
	}
		

	
	
}
