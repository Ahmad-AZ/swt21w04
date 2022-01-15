package festivalmanager.planning;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.salespointframework.core.SalespointIdentifier;

/**
 * the form used to rent new {@link Stage} for a {@link Festival}
 *
 * @author Adrian Scholze
 */
public class NewStageForm {

	@NotEmpty   
	@NotNull
	@NotBlank
	private final String name;
	
	@NotNull
	private final SalespointIdentifier equipmentsId;
	
	public NewStageForm(String name, SalespointIdentifier equipmentsId) {
		this.name = name;
		this.equipmentsId = equipmentsId;	
	}

	public String getName() {
		return name;
	}

	public SalespointIdentifier getEquipmentsId() {
		return equipmentsId;
	}
	
	
}
