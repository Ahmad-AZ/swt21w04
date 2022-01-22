package festivalmanager.planning;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.salespointframework.core.SalespointIdentifier;

import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;

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
	
	/**
	 * Creates a new {@link NewFestivalForm} with the given name and equipments id
	 *
	 * @param name must not be {@literal null}.
	 * @param equipmentsId must not be {@literal null}.
	 */
	public NewStageForm(String name, SalespointIdentifier equipmentsId) {
		this.name = name;
		this.equipmentsId = equipmentsId;	
	}

	/**
	 * Returns forms name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns forms equipments id.
	 * 
	 * @return equipmentsId
	 */
	public SalespointIdentifier getEquipmentsId() {
		return equipmentsId;
	}
	
	
}
