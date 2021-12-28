package festivalmanager.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.salespointframework.core.Currencies.EURO;

import java.time.LocalDate;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import festivalmanager.AbstractIntegrationTests;


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
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void locationDetailTest() {
		Model model = new ExtendedModelMap();

		Location location = new Location();
		lm.saveLocation(location);

		String returnedView = controller.locationDetail(location.getId(), model);
		assertThat(returnedView).isEqualTo("locationDetail");
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToAddLocation() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		String returnedView = controller.newLocation(model, new NewLocationForm("Location", "Adresse", "2112.50",
															Long.valueOf(1234), Long.valueOf(1234), null, null));
		assertThat(returnedView).isEqualTo("newLocation");
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void getRemoveLocationDialogSuccessTest() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Location location = new Location("Location", "Adresse", Money.of(123.30, EURO),
				(long) 234, (long) 234, "loc_image", "gv_image");
		lm.saveLocation(location);

		String returnedView = controller.getRemoveLocationDialog(location.getId(), model);
		assertThat(returnedView).isEqualTo("/locations");
		
		assertThat(model.getAttribute("dialog")).isEqualTo("remove_location");
		assertThat(model.getAttribute("currentName")).isEqualTo(location.getName());
		assertThat(model.getAttribute("currentId")).isEqualTo(location.getId());
		assertThat(model.getAttribute("locationHasBookings")).isEqualTo(false);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void getRemoveLocationDialogWithBookingsSuccessTest() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Location location = new Location("Location", "Adresse", Money.of(123.30, EURO),
										(long) 234, (long) 234, "loc_image", "gv_image");
		location.addBooking(LocalDate.now(), LocalDate.now().plusDays(7));
		lm.saveLocation(location);

		String returnedView = controller.getRemoveLocationDialog(location.getId(), model);
		assertThat(returnedView).isEqualTo("/locations");
		
		assertThat(model.getAttribute("dialog")).isEqualTo("remove_location");
		assertThat(model.getAttribute("currentName")).isEqualTo(location.getName());
		assertThat(model.getAttribute("currentId")).isEqualTo(location.getId());
		assertThat(model.getAttribute("locationHasBookings")).isEqualTo(true);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void getRemoveLocationDialogFailureTest() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Location location = new Location("Location", "Adresse",
										Money.of(123.30, EURO), (long) 234, (long) 234, "loc_image", "gv_image");
		
		String returnedView = controller.getRemoveLocationDialog(location.getId(), model);
		assertThat(returnedView).isEqualTo("/locations");
		
		assertThat(model.getAttribute("dialog")).isEqualTo("remove_location");
		assertThat(model.getAttribute("currentName")).isEqualTo("");
		assertThat(model.getAttribute("currentId")).isEqualTo(location.getId());
		assertThat(model.getAttribute("locationHasBookings")).isNull();
	}
	
}
