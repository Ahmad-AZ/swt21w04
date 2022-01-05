package festivalmanager.planning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.location.Location;
import festivalmanager.location.LocationManagement;

import static org.salespointframework.core.Currencies.EURO;

@AutoConfigureMockMvc
public class PlanEquipmentControllerIntegrationTest extends AbstractIntegrationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired PlanEquipmentController controller;
	@Autowired PlanEquipmentManagement planEquipmentManagement;
	@Autowired FestivalManagement festivalManagement;
	
	@Autowired LocationManagement locationManagement;

	@Test
	@WithMockUser(roles = "ADMIN")
	void EquipmentsPageTest() throws Exception{

		ExtendedModelMap model = new ExtendedModelMap();

		//assertThat(model.getAttribute("equipmentStage")).isNotNull();
		
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		controller.equipments(model, mock(EquipmentRentingForm.class), mock(NewStageForm.class), festival.getId());
		Location location = new Location();
		locationManagement.saveLocation(location);
		festival.setLocation(location);
		festivalManagement.saveFestival(festival);
		
		mockMvc.perform(get("/equipments/{festivalId}", festival.getId()))
		        .andExpect(status().isOk());
	}
}
