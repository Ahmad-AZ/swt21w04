package festivalmanager.planning;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewStageForm {

	@NotEmpty   
	@NotNull
	@NotBlank
	private final String name;
	
	@Min(value = 0)
	@NotNull
	private final Long equipmentsId;
	
	public NewStageForm(String name, Long equipmentsId) {
		this.name = name;
		this.equipmentsId = equipmentsId;	
	}

	public String getName() {
		return name;
	}

	public Long getEquipmentsId() {
		return equipmentsId;
	}
	
	
}
