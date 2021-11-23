package festivalmanager.Equipment;

import org.javamoney.moneta.Money;

public class Catering extends Equipment {
	public Catering(String name, Money rentalPerDay, int length, int width){
		super(name, rentalPerDay, length, width);
	}
}
