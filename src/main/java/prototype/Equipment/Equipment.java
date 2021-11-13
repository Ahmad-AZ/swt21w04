package prototype.Equipment;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Equipment {

	private @Id	@GeneratedValue long id;

	public static enum EquipmentType {
		STAGE, LIGHT;
	}

	private EquipmentType equipmentType;
	private String name;

	public Equipment(){}

	public Equipment(String name, EquipmentType equipmentType){
		this.name = name;
		this.equipmentType = equipmentType;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}


	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public String getName() {
		return name;
	}

	public long getId(){
		return id;
	}

}
