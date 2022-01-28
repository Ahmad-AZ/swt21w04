package festivalmanager.Equipment;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.javamoney.moneta.Money;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.util.Assert;

/**
 * The class for Equipment, which could be STAGE, CATERING_STALL or TOILET
 *
 * @author Tuan Giang and Adrian Scholze
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

	/**
	 * Creates a new {@link Equipment} with the given name and rental per day
	 * and {@link EquipmentType}.
	 *
	 * @param name must not be {@literal null}.
	 * @param rentalPerDay must not be {@literal null}.	
	 * @param type
	 */
	public Equipment(String name, Money rentalPerDay, EquipmentType type){
		Assert.notNull(rentalPerDay, "Rental per day must not be null!");
		Assert.notNull(name, "Name must not be null!");
		this.name = name;
		this.rentalPerDay = rentalPerDay;
		this.type = type;
	}

	/**
	 * Returns equipments rental per day.
	 * 
	 * @return rentalPerDay
	 */
	public Money getRentalPerDay() {
		return rentalPerDay;
	}

	/**
	 * Returns equipments name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns equipments id
	 * 
	 * @return id
	 */
	public SalespointIdentifier getId(){
		return id;
	}

	/**
	 * Returns equipments type
	 * 
	 * @return type
	 */
	public EquipmentType getType() {
		return type;
	}

}
