package festivalmanager.Equipment;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.javamoney.moneta.Money;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.SalespointIdentifier;

/**
 * class of stage
 *
 * @author Adrian and Tuan Giang
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
	
	public Stage(String name, Money rentalPerDay) {
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
