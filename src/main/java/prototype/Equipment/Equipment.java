package prototype.Equipment;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import javax.persistence.Entity;

@Entity
public class Equipment extends Product {
	public static enum EquipmentType {
		STAGE, LIGHT;
	}

	private EquipmentType equipmentType;
	private String name;

	@SuppressWarnings({ "unused", "deprecation" })
	private Equipment(){}

	public Equipment(String name, Money price, EquipmentType equipmentType){
		super(name, price);
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
