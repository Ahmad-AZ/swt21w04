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
	
	@Test 
	void createNewFestival() {
		
		Long vc = (long) 1500;
		Long sc = (long) 6;
		Double ppD = (double) 1550.90;
		NewLocationForm form = new NewLocationForm("name", "adress",  ppD, vc, sc, null, null);
		Location savedLocation = lm.createLocation(form);
		
		Optional<Location> location = lm.findById(savedLocation.getId());
		if (location.isPresent()) {
			Location current = location.get();
			
			assertThat(current.getName()).isEqualTo("name");
			assertThat(current.getAdress()).isEqualTo("adress");
			assertThat(current.getPricePerDay()).isEqualTo(Money.of(1550.90, EURO));
			assertThat(current.getVisitorCapacity()).isEqualByComparingTo((long) 1500);
			assertThat(current.getStageCapacity()).isEqualByComparingTo((long) 6);
			assertThat(current.getImage()).isEqualTo("Blank_image");
			assertThat(current.getGroundView()).isEqualTo("Blank_groundview");
			
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
	
}
