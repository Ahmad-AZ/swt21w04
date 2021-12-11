package festivalmanager.planning;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalController;
import festivalmanager.festival.FestivalManagement;

public class PlanLocationControllerIntegrationTest extends AbstractIntegrationTests {
	@Autowired PlanLocationController controller;
	@Autowired PlanLocationManagement planLocationManagement ;


//	// Test planLocation, locationList not Null
//	@Test
//	void allowsAuthenticatedAccessToController() {
//
//		ExtendedModelMap model = new ExtendedModelMap();
//
//		controller.locationOverview(model, festival.getId());
//		assertThat(model.getAttribute("locationList")).isNotNull();
//	}
//	
//	@Test
//	@WithMockUser(roles = "ADMIN")
//	void allowsAuthenticatedAccessToAddFestival() {
//
//		ExtendedModelMap model = new ExtendedModelMap();
//		
//		String returnedView = controller.newFestival(model, new NewFestivalForm(null, null, null));
//		assertThat(returnedView).isEqualTo("newFestival");
//		assertThat(model.getAttribute("dateNow")).isEqualTo(LocalDate.now());
//	}
//	
//	@Test
//	@WithMockUser(roles = "ADMIN")
//	void deleteFestivalIsAccessableForAdmin() throws Exception {
//		
//		
//		ExtendedModelMap model = new ExtendedModelMap();
//		Festival festival = new Festival();
//		planLocationManagement.saveFestival(festival);
//		
//		String returnedView = controller.getRemoveFestivalDialog(festival.getId(), model);
//		assertThat(returnedView).isEqualTo("festivalOverview");
//		assertThat(model.getAttribute("dialog")).isEqualTo("remove_festival");
//		assertThat(model.getAttribute("festival")).isEqualTo(festival);
//							
//	}
}
