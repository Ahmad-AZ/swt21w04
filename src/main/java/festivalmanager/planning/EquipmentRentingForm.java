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
	
	/**
	 * Creates a new {@link EquipmentRentingForm} with the given amount and equipments id.
	 *
	 * @param amount must not be {@literal null}.
	 * @param equipmentsId must not be {@literal null}.
	 */
	public EquipmentRentingForm(Long amount, SalespointIdentifier equipmentsId) {
		this.equipmentsId = equipmentsId;
		this.amount = amount;
	}
	
	/**
	 * Returns forms amount.
	 * 
	 * @return amount
	 */
	public Long getAmount() {
		return amount;
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
