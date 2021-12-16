package festivalmanager.planning;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

class EquipmentRentingForm {
	
	@Min(value = 0, message="Anzahl darf nicht negativ sein") 
	@NotNull
	private final Long amount;

	@Min(value = 0)
	@NotNull
	private final Long equipmentsId;
	
	public EquipmentRentingForm(Long amount, Long equipmentsId) {
		this.equipmentsId = equipmentsId;
		this.amount = amount;
	}
	
	public Long getAmount() {
		return amount;
	}
	
	public Long getEquipmentsId() {
		return equipmentsId;
	}
}
