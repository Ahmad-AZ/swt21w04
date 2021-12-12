package festivalmanager.Equipment;

import org.javamoney.moneta.Money;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Equipment {
	
	public static enum EquipmentType {
		STAGE, CATERING_STALL, TOILET
	}
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
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

	public long getId(){
		return id;
	}

	public EquipmentType getType() {
		return type;
	}

}
