package prototype.Equipment;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Equipment {

	private @Id	@GeneratedValue long id;


	private EquipmentType equipmentType;
	private String name;
	@Lob
	private Money money;

	public Equipment(){}

	public Equipment(String name,
					 Money money,
					 EquipmentType equipmentType){
		this.name = name;
		this.equipmentType = equipmentType;
		this.money = money;
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
