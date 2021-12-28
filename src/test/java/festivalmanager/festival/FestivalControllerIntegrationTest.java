package festivalmanager.festival;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.location.Location;


public class FestivalControllerIntegrationTest extends AbstractIntegrationTests{

	@Autowired FestivalController controller;
	@Autowired FestivalManagement festivalManagement ;
	


	// Test festivals, festivalList not Null
	@Test
	void allowsAuthenticatedAccessToFestivalOverview() {

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
		
//		returnedView = controller.createNewFestival(new NewFestivalForm("name", LocalDate.of(2021, 12, 22), LocalDate.of(2021, 12, 26)), null, model);
//		assertThat(returnedView).isEqualTo("festivalOverview");
//		
	}
	
//	@Test
//	@WithMockUser(roles = "ADMIN")
//	void allowsAuthenticatedAccessToCreateFestival() {
//		
//		ExtendedModelMap model = new ExtendedModelMap();
//		
//		String returnedView = controller.createNewFestival(new NewFestivalForm("name", LocalDate.of(2021, 12, 22), LocalDate.of(2021, 12, 22)), null, model);
//		assertThat(returnedView).isEqualTo("festivalOverview");
//		
//		// reject same name
//		returnedView = controller.createNewFestival(new NewFestivalForm("name", LocalDate.now(), LocalDate.now().plusDays(5)), null, model);
//		assertThat(returnedView).isEqualTo("newFestival");
//		assertThat(model.getAttribute("result")).isNotNull();
//		
//		// reject wrong dates
//		returnedView = controller.createNewFestival(new NewFestivalForm("name2", LocalDate.now().plusDays(4), LocalDate.now()), null, model);
//		assertThat(returnedView).isEqualTo("newFestival");
//		assertThat(model.getAttribute("result")).isNotNull();
//		
//	}
	
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
	public void festivalDetailWithoutLocationSuccessTest() {

		Model model = new ExtendedModelMap();

		Festival festival = new Festival();
		festivalManagement.saveFestival(festival);

		String returnedView = controller.festivalDetail(festival.getId(), model, new StringInputForm(""));
		assertThat(returnedView).isEqualTo("festivalDetail");
		
		assertThat(model.getAttribute("location")).isNull();
		assertThat(model.getAttribute("artists")).isNull();
		assertThat(model.getAttribute("festival")).isNotNull();
	}
	
	@Test
	public void festivalDetailWithLocationSuccessTest() {

		Model model = new ExtendedModelMap();

		Festival festival = new Festival();
		festival.setLocation(new Location());
		festivalManagement.saveFestival(festival);

		String returnedView = controller.festivalDetail(festival.getId(), model, new StringInputForm(""));
		assertThat(returnedView).isEqualTo("festivalDetail");
		
		assertThat(model.getAttribute("location")).isNotNull();
		assertThat(model.getAttribute("artists")).isNull();
		assertThat(model.getAttribute("festival")).isNotNull();
	}
	
	@Test
	public void festivalDetailFailureTest() {

		Model model = new ExtendedModelMap();

		Festival festival = new Festival();		

		String returnedView = controller.festivalDetail(festival.getId(), model, new StringInputForm(""));
		assertThat(returnedView).isEqualTo("redirect:/festivalOverview");
	}
	
	@Test
	void festivalMapVisitorViewTest() {
		Model model = new ExtendedModelMap();

		Festival festival = new Festival();	
		festival.setLocation(new Location());
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.getMapVisitorView(festival.getId(), model);
		assertThat(returnedView).isEqualTo("/mapVisitorView");
		
		assertThat(model.getAttribute("location")).isNotNull();
		assertThat(model.getAttribute("festival")).isNotNull();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void getEditFestivalDialogTest() {
		Model model = new ExtendedModelMap();

		Festival festival = new Festival();	

		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.getEditFestivalNameDialog(festival.getId(), new StringInputForm(""), model);
		assertThat(returnedView).isEqualTo("festivalDetail");
		
		assertThat(model.getAttribute("festival")).isNotNull();
		assertThat(model.getAttribute("dialog")).isEqualTo("edit name");

	}

//	@Test
//	@WithMockUser(roles = "ADMIN")
//	void festivalEditFestivalNameTest() {
//		Model model = new ExtendedModelMap();
//
//		Festival festival = new Festival();	
//
//		festivalManagement.saveFestival(festival);
//		
//		String returnedView = controller.editFestivalName(festival.getId(), new StringInputForm(""), null, model);
//
//	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void getRemoveFestivalDialogSuccessTest() {
		Model model = new ExtendedModelMap();

		Festival festival = new Festival("Fest", LocalDate.now(), LocalDate.now());	

		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.getRemoveFestivalDialog(festival.getId(), model);
		assertThat(returnedView).isEqualTo("festivalOverview");
		
		assertThat(model.getAttribute("dialog")).isEqualTo("remove_festival");
		assertThat(model.getAttribute("currentName")).isEqualTo(festival.getName());
		assertThat(model.getAttribute("currentId")).isEqualTo(festival.getId());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void getRemoveFestivalDialogFailureTest() {
		Model model = new ExtendedModelMap();

		Festival festival = new Festival("Fest", LocalDate.now(), LocalDate.now());	
		
		String returnedView = controller.getRemoveFestivalDialog(festival.getId(), model);
		assertThat(returnedView).isEqualTo("festivalOverview");
		
		assertThat(model.getAttribute("dialog")).isEqualTo("remove_festival");
		assertThat(model.getAttribute("currentName")).isEqualTo("");
		assertThat(model.getAttribute("currentId")).isEqualTo(festival.getId());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void removeFestivalSuccessTest() {

		Festival festival = new Festival("Fest", LocalDate.now(), LocalDate.now());	
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.removeFestival(festival.getId());
		assertThat(returnedView).isEqualTo("redirect:/festivalOverview");
		
		assertThat(festivalManagement.findById(festival.getId()).isPresent()).isFalse();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void removeFestivalFailureTest() {

		Festival festival = new Festival("Fest", LocalDate.now(), LocalDate.now());
		festivalManagement.saveFestival(festival);
		
		Festival wrongFestival = new Festival("Fest", LocalDate.now(), LocalDate.now());	
		
		String returnedView = controller.removeFestival(wrongFestival.getId());
		assertThat(returnedView).isEqualTo("redirect:/festivalOverview");
		
		assertThat(festivalManagement.findById(festival.getId()).isPresent()).isTrue();
	}
	
}
