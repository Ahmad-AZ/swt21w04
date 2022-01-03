package festivalmanager.planning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.salespointframework.core.Currencies.EURO;

import java.time.LocalDate;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalController;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.location.Location;
import festivalmanager.location.LocationManagement;

public class PlanLocationControllerIntegrationTest extends AbstractIntegrationTests {
	@Autowired PlanLocationController controller;
	@Autowired PlanLocationManagement planLocationManagement ;
	@Autowired FestivalManagement festivalManagement;
	@Autowired LocationManagement locationManagement;

	
	@Before
	void setup() {
		
//		location = new Location();
//		locationManagement.saveLocation(location);
//		festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
//		festivalManagement.saveFestival(festival);
	}

	// Test planLocation, locationList not Null
	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToController() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);
		
		controller.locationOverview(model, festival.getId());
		assertThat(model.getAttribute("locationList")).isNotNull();
		assertThat(model.getAttribute("bookedLocationId")).isEqualTo(0);
	}
	
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void ControllerAccessWithLocationBookedTest() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Location location = new Location("Location", "Adresse",
				Money.of(123.30, EURO), (long) 234, (long) 234, "loc_image", "gv_image");
		locationManagement.saveLocation(location);
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		
		
		festival.setLocation(location);
		festivalManagement.saveFestival(festival);
		
		controller.locationOverview(model, festival.getId());
		assertThat(model.getAttribute("locationList")).isNotNull();
		assertThat(model.getAttribute("bookedLocationId")).isEqualTo(location.getId());
	}
	
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void locationDetailLocationBookedTest() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Location location = new Location();
		locationManagement.saveLocation(location);
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		
		festival.setLocation(location);
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.locationDetail(festival.getId(), location.getId(), model);
		assertThat(returnedView).isEqualTo("locationDetailPlan");
		assertThat(model.getAttribute("location")).isEqualTo(location);
		assertThat(model.getAttribute("currentlyBooked")).isEqualTo(true);
		assertThat(model.getAttribute("hasBookings")).isEqualTo(false);

	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void locationDetailLocationNotBookedTest() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Location location = new Location();
		location.addBooking(LocalDate.now(), LocalDate.now().plusDays(3));
		locationManagement.saveLocation(location);
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.locationDetail(festival.getId(), location.getId(), model);
		assertThat(returnedView).isEqualTo("locationDetailPlan");
		assertThat(model.getAttribute("location")).isEqualTo(location);
		assertThat(model.getAttribute("currentlyBooked")).isEqualTo(false);
		assertThat(model.getAttribute("hasBookings")).isEqualTo(true);
	}
	
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void bookLocationSuccessTest() {
			
		Location location = new Location();
		locationManagement.saveLocation(location);
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.bookLocation(festival.getId(), location.getId(), false, null);
		assertThat(returnedView).isEqualTo("redirect:/locationOverview/" + festival.getId());
				
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void bookLocationOccupiedTest() {
				
		Location location = new Location();
		location.addBooking(LocalDate.now(), LocalDate.now().plusDays(3));
		locationManagement.saveLocation(location);
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.bookLocation(festival.getId(), location.getId(), false, mock(RedirectAttributes.class));
		assertThat(returnedView).isEqualTo("redirect:/locationOverview/"+ festival.getId() + "/detail/" + location.getId());			
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void unbookLocationInDetailTest() {
				
		Location location = new Location();
		location.addBooking(LocalDate.now(), LocalDate.now().plusDays(4));
		locationManagement.saveLocation(location);
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festival.setLocation(location);
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.bookLocation(festival.getId(), location.getId(), true, null);
		assertThat(returnedView).isEqualTo("redirect:/locationOverview/" + festival.getId());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void unbookLocationInOverviewTest() {
				
		Location location = new Location();
		location.addBooking(LocalDate.now(), LocalDate.now().plusDays(4));
		locationManagement.saveLocation(location);
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festival.setLocation(location);
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.unbookLocation(festival.getId());
		assertThat(returnedView).isEqualTo("redirect:/locationOverview/" + festival.getId());
	}
	
}
