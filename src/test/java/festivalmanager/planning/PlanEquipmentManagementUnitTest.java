package festivalmanager.planning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.Test;
import org.salespointframework.core.SalespointIdentifier;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;


public class PlanEquipmentManagementUnitTest {
	FestivalManagement festivalManagement = mock(FestivalManagement.class);
	EquipmentManagement equipmentManagement = mock(EquipmentManagement.class);
	PlanEquipmentManagement planEquipmentManagement = new PlanEquipmentManagement(festivalManagement, equipmentManagement);
	
	Equipment equipment = mock(Equipment.class);
	Stage stage = mock(Stage.class);
	Festival festival = mock(Festival.class);
	
	
	@Test
	void unrentStageSuccessTest() {
		when(festival.removeStage(any())).thenReturn(true);
		assertThat(planEquipmentManagement.unrentStage(stage, festival)).isEqualTo(true);
	}
	
	@Test
	void unrentStageFailureTest() {
		assertThat(planEquipmentManagement.unrentStage(stage, festival)).isEqualTo(false);
	}
}
