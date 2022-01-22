package festivalmanager.Equipment;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.javamoney.moneta.Money;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.util.Assert;

/**
 * class of stage
 *
 * @author Adrian and Tuan Giang Trinh
 */
@Entity
public class Stage extends AbstractEntity<SalespointIdentifier> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1863825782551414591L;
	
	@Id 
	private SalespointIdentifier id = new SalespointIdentifier(UUID.randomUUID().toString());
	
	private String name;
	@Lob
	private Money rentalPerDay;
	
	public Stage() {}
	
	/**
	 * Creates a new {@link Stage} with the given name and rental per day.
	 *
	 * @param name must not be {@literal null}.
	 * @param rentalPerDay must not be {@literal null}.
	 */
	public Stage(String name, Money rentalPerDay) {
		Assert.notNull(rentalPerDay, "Rental per day must not be null!");
		Assert.notNull(name, "Name must not be null!");
		this.name=name;
		this.rentalPerDay = rentalPerDay;
	}

	@Override
	public SalespointIdentifier getId() {
		return id;
	}
	
	public Money getRentalPerDay() {
		return rentalPerDay;
	}

	public String getName() {
		return name;
	}
}
