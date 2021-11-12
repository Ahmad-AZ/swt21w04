package prototype.Equipment;

import org.salespointframework.catalog.Product;

import javax.persistence.Entity;

@Entity
public class Equipment extends Product {
	public static enum EquipmentType {
		STAGE, LIGHT;
	}

	private EquipmentType equipmentType;
	private String name;

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

	@Override
	public String getName() {
		return name;
	}

}
