package festivalmanager.Equipment;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.javamoney.moneta.Money;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.SalespointIdentifier;

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
	private int length, width;

	public Equipment(){}

	public Equipment(String name, Money rentalPerDay, int length, int width, EquipmentType type){
		this.name = name;
		this.rentalPerDay = rentalPerDay;
		this.length = length;
		this.width = width;
		this.type = type;
	}

	public Money getRentalPerDay() {
		return rentalPerDay;
	}

	public int getLength() {
		return length;
	}

	public int getWidth() {
		return width;
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
