package festivalmanager.festival;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

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
		
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToCreateFestival() {
		
		ExtendedModelMap model = new ExtendedModelMap();
		
		Errors errors = (Errors) mock(Errors.class);
		when(errors.hasErrors()).thenReturn(false);
		
		String returnedView = controller.createNewFestival(new NewFestivalForm("name", LocalDate.now(), LocalDate.now().plusDays(5)), errors, model);
		assertThat(returnedView).isEqualTo("redirect:/festivalOverview");
		
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void CreateFestivalRejectWrongValuesTest() {
		
		ExtendedModelMap model = new ExtendedModelMap();
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);
		
		Errors errors = (Errors) mock(Errors.class);
		when(errors.hasErrors()).thenReturn(true);
		
		// reject same name
		String returnedView = controller.createNewFestival(new NewFestivalForm("name", LocalDate.now(), LocalDate.now().plusDays(5)), errors, model);
		assertThat(returnedView).isEqualTo("newFestival");
		//assertThat(model.getAttribute("result")).isNotNull();
		
		// reject wrong dates
		returnedView = controller.createNewFestival(new NewFestivalForm("name2", LocalDate.now().plusDays(4), LocalDate.now()), errors, model);
		assertThat(returnedView).isEqualTo("newFestival");
		//assertThat(model.getAttribute("result")).isNotNull();
		
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

	@Test
	@WithMockUser(roles = "ADMIN")
	void festivalEditFestivalNameTest() {
		Model model = new ExtendedModelMap();

		Festival festival = new Festival("name", LocalDate.now().plusDays(15), LocalDate.now().plusDays(20));
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.editFestivalName(festival.getId(), new StringInputForm("newName"), (Errors) mock(Errors.class), model);
		assertThat(returnedView).isEqualTo("redirect:/festivalOverview/"+ festival.getId());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void festivalEditFestivalNameFailureTest() {
		
		ExtendedModelMap model = new ExtendedModelMap();
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);
		
		Errors errors = (Errors) mock(Errors.class);
		when(errors.hasErrors()).thenReturn(true);
		
		// reject same name
		String returnedView = controller.editFestivalName(festival.getId(), new StringInputForm("name"), errors, model);
		assertThat(returnedView).isEqualTo("festivalDetail");
		//assertThat(model.getAttribute("result")).isNotNull();
		
		// reject startDate in less than 14 days
		returnedView = controller.editFestivalName(festival.getId(), new StringInputForm("newName"), errors, model);
		assertThat(returnedView).isEqualTo("festivalDetail");
		//assertThat(model.getAttribute("result")).isNotNull();
		
	}
	
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
