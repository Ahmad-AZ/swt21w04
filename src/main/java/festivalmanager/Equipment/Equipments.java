package festivalmanager.Equipment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Equipments {
	
	private @Id	long id;
	
	@Lob
	private Equipment equipment;
	private long amount;
	
	public Equipments() {}
	
	public Equipments(Equipment equipment, long amount) {
		this.setId(equipment.getId());
		this.setEquipment(equipment);
		this.setAmount(amount);
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
