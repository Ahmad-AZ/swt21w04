package festivalmanager.festival;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import festivalmanager.AbstractIntegrationTests;


public class FestivalControllerIntegrationTest extends AbstractIntegrationTests{

	@Autowired FestivalController controller;
	@Autowired FestivalManagement festivalManagement ;
	


	// Test festivals, festivalList not Null
	@Test
	void allowsAuthenticatedAccessToController() {

		ExtendedModelMap model = new ExtendedModelMap();

		controller.festivals(model);
		assertThat(model.getAttribute("festivalList")).isNotNull();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToAddFestival() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		String returnedView = controller.newFestival(model, new NewFestivalForm(null, null, null));
		assertThat(returnedView).isEqualTo("newFestival");
		assertThat(model.getAttribute("dateNow")).isEqualTo(LocalDate.now());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteFestivalIsAccessableForAdmin() throws Exception {
		
		
		ExtendedModelMap model = new ExtendedModelMap();
		Festival festival = new Festival();
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.getRemoveFestivalDialog(festival.getId(), model);
		assertThat(returnedView).isEqualTo("festivalOverview");
		assertThat(model.getAttribute("dialog")).isEqualTo("remove_festival");
		assertThat(model.getAttribute("currentId")).isEqualTo(festival.getId());
							
	}
	
//	@Test
//	@WithMockUser(roles = "MANAGER")
//	void preventsManagerAccessForDeleteFestival() throws Exception {
//
//		mvc.perform(get("festivalOverview/remove/4")).andExpect(status().isFound()) 
//					.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
//	}

	
	
	@Test
	public void festivalDetailSuccessTest() {

		Model model = new ExtendedModelMap();

		Festival festival = new Festival();
		festivalManagement.saveFestival(festival);

		String returnedView = controller.festivalDetail(festival.getId(), model);
		assertThat(returnedView).isEqualTo("festivalDetail");
		
		assertThat(model.getAttribute("location")).isNull();
		assertThat(model.getAttribute("artists")).isNull();
		assertThat(model.getAttribute("festival")).isNotNull();
	}
}
