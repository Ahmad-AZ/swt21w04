package festivalmanager.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.salespointframework.core.Currencies.EURO;

import java.time.LocalDate;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void locationEditSuccessTest() {

		Model model = new ExtendedModelMap();


		Money price = Money.of(1111, EURO);
		Location location = new Location();
		location.setPricePerDay(price);
		lm.saveLocation(location);

		Long locationId = location.getId();


		String returnedView = controller.locationEdit(locationId, model);
		assertThat(returnedView).isEqualTo("locationEdit");
		
		assertThat(model.getAttribute("location")).isNotNull();
		assertThat(model.getAttribute("pricePerDay")).isEqualTo(price.getNumber().doubleValue());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void locationEditFailureTest() {

		Model model = new ExtendedModelMap();

		Location location = new Location();

		Long locationId = location.getId();

		String returnedView = controller.locationEdit(locationId, model);
		assertThat(returnedView).isEqualTo("locationEdit");
		
		assertThat(model.getAttribute("location")).isNull();
		assertThat(model.getAttribute("pricePerDay")).isNull();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void locationDetailSuccessTest() {
		ExtendedModelMap model = new ExtendedModelMap();

		Location location = new Location();
		lm.saveLocation(location);

		String returnedView = controller.locationDetail(location.getId(), model);
		assertThat(returnedView).isEqualTo("locationDetail");
		assertThat(model.getAttribute("location")).isEqualTo(location);
		assertThat(model.getAttribute("hasBookings")).isEqualTo(false);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void locationDetailFailureTest() {
		ExtendedModelMap model = new ExtendedModelMap();

		Location location = new Location();
		
		String returnedView = controller.locationDetail(location.getId(), model);
		assertThat(returnedView).isEqualTo("locationDetail");
		assertThat(model.getAttribute("location")).isNull();
		assertThat(model.getAttribute("hasBookings")).isNull();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void locationDetailWithBookingsTest() {
		ExtendedModelMap model = new ExtendedModelMap();

		Location location = new Location();
		location.addBooking(LocalDate.now(), LocalDate.now().plusDays(12));
		lm.saveLocation(location);

		String returnedView = controller.locationDetail(location.getId(), model);
		assertThat(returnedView).isEqualTo("locationDetail");
		assertThat(model.getAttribute("location")).isEqualTo(location);
		assertThat(model.getAttribute("hasBookings")).isEqualTo(true);
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
	void createLocationSuccessTest() {
		
		String returnedView = controller.createNewLocation(new NewLocationForm("Location", "Adresse", "2112.50",
															Long.valueOf(1234), Long.valueOf(1234), null, null), mock(Errors.class));
		assertThat(returnedView).isEqualTo("redirect:/locations");
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void createLocationFailureTest() {
		
		Errors errors = mock(Errors.class);
		when(errors.hasErrors()).thenReturn(true);
		
		String returnedView = controller.createNewLocation(new NewLocationForm("Location", "Adresse", "2112.50",
															Long.valueOf(1234), Long.valueOf(1234), null, null), errors);
		assertThat(returnedView).isEqualTo("newLocation");
		// reject same name
		returnedView = controller.createNewLocation(new NewLocationForm("Location", "Adresse", "2112.50",
				Long.valueOf(1234), Long.valueOf(1234), null, null), errors);
		assertThat(returnedView).isEqualTo("newLocation");
		// reject negative Money
		returnedView = controller.createNewLocation(new NewLocationForm("Location2", "Adresse", "-2112.50",
				Long.valueOf(1234), Long.valueOf(1234), null, null), errors);
		assertThat(returnedView).isEqualTo("newLocation");
		// reject wrong MoneyString
		returnedView = controller.createNewLocation(new NewLocationForm("Location3", "Adresse", "noMoney",
				Long.valueOf(1234), Long.valueOf(1234), null, null), errors);
		assertThat(returnedView).isEqualTo("newLocation");
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void saveLocationSuccessTest() {
		ExtendedModelMap model = new ExtendedModelMap();
		
		Location location = new Location("Location", "Adresse",
				Money.of(123.30, EURO), (long) 234, (long) 234, "loc_image", "gv_image");
		lm.saveLocation(location);
		String returnedView = controller.saveLocation(new NewLocationForm("Location", "Adresse", "2112.50",
															Long.valueOf(1234), Long.valueOf(1234), null, null), mock(Errors.class), location.getId(), model);
		assertThat(returnedView).isEqualTo("redirect:/locations");
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void saveLocationFailureTest() {
		ExtendedModelMap model = new ExtendedModelMap();
		
		Errors errors = mock(Errors.class);
		when(errors.hasErrors()).thenReturn(true);
		
		Location existingLocation = new Location("Location", "Adresse",
				Money.of(123.30, EURO), (long) 234, (long) 234, "loc_image", "gv_image");
		lm.saveLocation(existingLocation);
		Location location = new Location("Location2", "Adresse",
				Money.of(123.30, EURO), (long) 234, (long) 234, "loc_image", "gv_image");
		lm.saveLocation(location);

		// reject same name
		String returnedView = controller.saveLocation(new NewLocationForm("Location", "Adresse", "2112.50",
				Long.valueOf(1234), Long.valueOf(1234), null, null), errors, location.getId(), model);
		assertThat(returnedView).isEqualTo("locationEdit");
		// reject negative Money
		returnedView = controller.saveLocation(new NewLocationForm("Location2", "Adresse", "-2112.50",
				Long.valueOf(1234), Long.valueOf(1234), null, null), errors, location.getId(), model);
		assertThat(returnedView).isEqualTo("locationEdit");
		// reject wrong MoneyString
		returnedView = controller.saveLocation(new NewLocationForm("Location2", "Adresse", "noMoney",
				Long.valueOf(1234), Long.valueOf(1234), null, null), errors, location.getId(), model);
		assertThat(returnedView).isEqualTo("locationEdit");
		
		
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
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void removeLocationSuccessTest() {
		
		Location location = new Location("Location2", "Adresse",
				Money.of(123.30, EURO), (long) 234, (long) 234, "loc_image", "gv_image");
		lm.saveLocation(location);

		String returnedView = controller.removeLocation(location.getId(), mock(RedirectAttributes.class));
		assertThat(returnedView).isEqualTo("redirect:/locations");
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void removeLocationNotExistsTest() {
		
		Location location = new Location("Location2", "Adresse",
				Money.of(123.30, EURO), (long) 234, (long) 234, "loc_image", "gv_image");

		String returnedView = controller.removeLocation(location.getId(), mock(RedirectAttributes.class));
		assertThat(returnedView).isEqualTo("redirect:/locations");
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void removeLocatioFailureTest() {
		
		Location location = new Location("Location2", "Adresse",
				Money.of(123.30, EURO), (long) 234, (long) 234, "loc_image", "gv_image");
		location.addBooking(LocalDate.now(), LocalDate.now().plusDays(12));
		lm.saveLocation(location);

		String returnedView = controller.removeLocation(location.getId(), mock(RedirectAttributes.class));
		assertThat(returnedView).isEqualTo("redirect:/locations/remove/"+ location.getId());
	}
	
}
