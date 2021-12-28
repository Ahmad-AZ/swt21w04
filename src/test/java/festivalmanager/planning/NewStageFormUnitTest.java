package festivalmanager.planning;

import org.junit.jupiter.api.Test;
import org.salespointframework.core.SalespointIdentifier;

import static org.assertj.core.api.Assertions.assertThat;

public class NewStageFormUnitTest {

	@Test
	void newStageForm() {
		SalespointIdentifier id = new SalespointIdentifier();
		NewStageForm form = new NewStageForm("name", id);
		assertThat(form.getName()).isEqualTo("name");
		assertThat(form.getEquipmentsId()).isEqualTo(id);
	}
}
