package festivalmanager.Equipment;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.javamoney.moneta.Money;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.SalespointIdentifier;

/**
 * The class for Equipment, which could be STAGE, CATERING_STALL or TOILET
 *
 * @author Tuan Giang and Adrian
 */
@Entity
public class Equipment extends AbstractEntity<SalespointIdentifier>{
	
	public static enum EquipmentType {
		STAGE, CATERING_STALL, TOILET
	}
	
	@Id 
	private SalespointIdentifier id = new SalespointIdentifier(UUID.randomUUID().toString());
	
	private String name;
	@Lob
	private Money rentalPerDay;
	
	private EquipmentType type;

	public Equipment(){}

	public Equipment(String name, Money rentalPerDay, EquipmentType type){
		this.name = name;
		this.rentalPerDay = rentalPerDay;
		this.type = type;
	}

	public Money getRentalPerDay() {
		return rentalPerDay;
	}

	public String getName() {
		return name;
	}

	public SalespointIdentifier getId(){
		return id;
	}

	public EquipmentType getType() {
		return type;
	}

}
