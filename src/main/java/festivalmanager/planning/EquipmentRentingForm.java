package festivalmanager.planning;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.salespointframework.core.SalespointIdentifier;

import festivalmanager.Equipment.Equipment;
import festivalmanager.festival.Festival;

/**
 * the form used to rent an amount of an {@link Equipment} for a {@link Festival}
 *
 * @author Adrian Scholze
 */
class EquipmentRentingForm {
	
	@Min(value = 0, message="Anzahl darf nicht negativ sein") 
	@NotNull
	private final Long amount;

	@NotNull
	private final SalespointIdentifier equipmentsId;
	
	public EquipmentRentingForm(Long amount, SalespointIdentifier equipmentsId) {
		this.equipmentsId = equipmentsId;
		this.amount = amount;
	}
	
	public Long getAmount() {
		return amount;
	}
	
	public SalespointIdentifier getEquipmentsId() {
		return equipmentsId;
	}
}
