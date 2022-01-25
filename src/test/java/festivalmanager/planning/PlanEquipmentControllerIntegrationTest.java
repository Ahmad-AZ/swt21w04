package festivalmanager.planning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.Equipment.EquipmentType;
import festivalmanager.Equipment.EquipmentManagement;
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
	@Autowired EquipmentManagement equipmentManagement;

	@Test
	@WithMockUser(roles = "ADMIN")
	void EquipmentsPageSuccessTest() throws Exception {

		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		Location location = new Location();
		locationManagement.saveLocation(location);
		festival.setLocation(location);
		festivalManagement.saveFestival(festival);
		
//		String returnedView = controller.equipments(model, mock(EquipmentRentingForm.class), mock(NewStageForm.class), festival.getId());
//		assertThat(returnedView).isEqualTo("equipments.html");
//		assertThat(model.getAttribute("location")).isNotNull();
//		assertThat(model.getAttribute("festival")).isNotNull();
//		assertThat(model.getAttribute("equipmentStage")).isNotNull();
//		assertThat(model.getAttribute("equipmentsMap")).isNotNull();
		
		mockMvc.perform(get("/equipments/{festivalId}", festival.getId()))
		        .andExpect(status().isOk())
		        .andExpect(MockMvcResultMatchers.model().attribute("festival", festival))
				.andExpect(MockMvcResultMatchers.model().attribute("location", location))
				.andExpect(MockMvcResultMatchers.model().attributeExists("equipmentStage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("equipmentsMap"));
	}
	
//	@Test
//	@WithMockUser(roles = "ADMIN")
//	void EquipmentsPageFailureTest() throws Exception {
//
//		
//		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
//				
//		mockMvc.perform(get("/equipments/{festivalId}", festival.getId()))
//		        .andExpect(status().isOk())
//		        .andExpect(MockMvcResultMatchers.model().attribute("festival", nullValue()))
//				.andExpect(MockMvcResultMatchers.model().attribute("location", nullValue()))
//				.andExpect(MockMvcResultMatchers.model().attribute("equipmentStage", nullValue()))
//				.andExpect(MockMvcResultMatchers.model().attribute("equipmentsMap", nullValue()));
//	
//	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void addStageSuccessTest() throws Exception {

			
		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
		Location location = new Location();
		locationManagement.saveLocation(location);
		festival.setLocation(location);
		festivalManagement.saveFestival(festival);
		
		Equipment equipment = null;
		for (Equipment anEquipment : equipmentManagement.findAllEquipments()) {				
			if(anEquipment.getType().equals(EquipmentType.STAGE)) {
				System.out.println(anEquipment.getName());
				equipment = anEquipment;
			}
		}
		
		mockMvc.perform(post("/equipments/{festivalId}/addStage", festival.getId()).requestAttr("newStageForm", new NewStageForm("name", equipment.getId())))
		        .andExpect(status().isOk())
		        .andExpect(MockMvcResultMatchers.model().attribute("festival", festival))
				.andExpect(MockMvcResultMatchers.model().attribute("location", location))
				.andExpect(MockMvcResultMatchers.model().attributeExists("equipmentStage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("equipmentsMap"));

	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void addStageFailureTest() throws Exception {
//
//			
//		Festival festival = new Festival("name", LocalDate.now(), LocalDate.now().plusDays(4));
//		Location location = new Location();
//		locationManagement.saveLocation(location);
//		festival.setLocation(location);
//		festivalManagement.saveFestival(festival);
//		
//		Equipment equipment = null;
//		for (Equipment anEquipment : equipmentManagement.findAllEquipments()) {				
//			if(anEquipment.getType().equals(EquipmentType.STAGE)) {
//				System.out.println(anEquipment.getName());
//				equipment = anEquipment;
//			}
//		}
//		mockMvc.perform(post("/equipments/{festivalId}/addStage", festival.getId()).requestAttr("newStageForm", new NewStageForm("name", equipment.getId())))
//        		.andExpect(status().isOk());
//		
//		mockMvc.perform(post("/equipments/{festivalId}/addStage", festival.getId()).requestAttr("newStageForm", new NewStageForm("name", equipment.getId())))
//		        .andExpect(status().isOk())
//		        .andExpect(MockMvcResultMatchers.model().attribute("festival", festival))
//				.andExpect(MockMvcResultMatchers.model().attribute("location", location))
//				.andExpect(MockMvcResultMatchers.model().attributeExists("equipmentStage"))
//				.andExpect(MockMvcResultMatchers.model().attributeExists("equipmentsMap"));
//
//		ExtendedModelMap model = new ExtendedModelMap(); 
//		
//		NewStageForm nsf = new NewStageForm("name", equipment.getId());
//		
//		
//		String returnedView = controller.addStage(nsf, (Errors) mock(Errors.class), model, mock(EquipmentRentingForm.class), festival.getId() );
//		assertThat(returnedView).isEqualTo("equipments.html");
//		assertThat(model.getAttribute("location")).isNotNull();
//		assertThat(model.getAttribute("festival")).isNotNull();
//		assertThat(model.getAttribute("equipmentStage")).isNotNull();
//		assertThat(model.getAttribute("equipmentsMap")).isNotNull();
	
	}
	
	
}
