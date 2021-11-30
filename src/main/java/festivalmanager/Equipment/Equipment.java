package festivalmanager.Equipment;

import org.javamoney.moneta.Money;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Equipment {

	private @Id	@GeneratedValue long id;

	private String name;
	@Lob
	private Money rentalPerDay;
	private int length, width;

	public Equipment(){}

	public Equipment(String name, Money rentalPerDay, int length, int width){
		this.name = name;
		this.rentalPerDay = rentalPerDay;
		this.length = length;
		this.width = width;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setRentalPerDay(Money rentalPerDay) {
		this.rentalPerDay = rentalPerDay;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getName() {
		return name;
	}

	public long getId(){
		return id;
	}

}
