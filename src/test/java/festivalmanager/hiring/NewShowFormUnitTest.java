package festivalmanager.hiring;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class NewShowFormUnitTest {

	@Test
	void newShowForm() {
		NewShowForm form = new NewShowForm("name", 134);
		assertThat(form.getName()).isEqualTo("name");
		assertThat(form.getPerformance()).isEqualTo(134);
	}

}
