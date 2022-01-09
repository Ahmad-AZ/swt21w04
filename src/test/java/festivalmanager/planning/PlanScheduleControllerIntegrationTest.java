package festivalmanager.planning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;

public class PlanScheduleControllerIntegrationTest extends AbstractIntegrationTests {
	@Autowired PlanScheduleController controller;
	@Autowired PlanScheduleManagement planScheduleManagement ;
	@Autowired FestivalManagement festivalManagement;
	@Autowired EquipmentManagement equipmentManagement;

	
	// Test planSchedule
	@Test
	void allowsAuthenticatedAccessToControllerWithExistingFestival() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.schedule(model, festival.getId());
		assertThat(returnedView).isEqualTo("schedule.html");
		assertThat(model.getAttribute("dayList")).isNotNull();
		assertThat(model.getAttribute("timeSlotList")).isNotNull();
		assertThat(model.getAttribute("festival")).isNotNull();
	}
	
	// Test planSchedule
	@Test
	void allowsAuthenticatedAccessToControllerWithoutExistingFestival() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		
		String returnedView = controller.schedule(model, festival.getId());
		assertThat(returnedView).isEqualTo("schedule.html");
		assertThat(model.getAttribute("dayList")).isNull();
		assertThat(model.getAttribute("timeSlotList")).isNull();
		assertThat(model.getAttribute("festival")).isNull();
	}
	
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void getShwoSelectDialogTestWithExistingFestival() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);
		
		String returnedView = controller.getShowSelectDialog(LocalDate.now(), mock(SalespointIdentifier.class), "TS1", festival.getId(), model);
		assertThat(returnedView).isEqualTo("schedule.html");
		assertThat(model.getAttribute("dialog")).isEqualTo("edit schedule");
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void getShwoSelectDialogTestWithoutExistingFestival() {

		ExtendedModelMap model = new ExtendedModelMap();
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		
		String returnedView = controller.getShowSelectDialog(LocalDate.now(), mock(SalespointIdentifier.class), "TS1", festival.getId(), model);
		assertThat(returnedView).isEqualTo("redirect:/schedule/" + festival.getId());
		assertThat(model.getAttribute("dialog")).isNull();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void chooseShowWithExistingFestivalAndStage() {
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		festivalManagement.saveFestival(festival);
		
		Stage stage = new Stage();
		equipmentManagement.saveStage(stage);
		
		String returnedView = controller.chooseShow(LocalDate.now(), stage.getId(), "TS1", (long) 1, (long) 3, festival.getId());
		assertThat(returnedView).isEqualTo("redirect:/schedule/"+ festival.getId());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void chooseShowWithoutExistingFestivalAndStage() {
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
			
		Stage stage = new Stage();
		
		String returnedView = controller.chooseShow(LocalDate.now(), stage.getId(), "TS1", (long) 1, (long) 3, festival.getId());
		assertThat(returnedView).isEqualTo("redirect:/schedule/"+ festival.getId());
	}
}