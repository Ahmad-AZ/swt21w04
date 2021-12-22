package festivalmanager.Equipment;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.javamoney.moneta.Money;

import festivalmanager.Equipment.Equipment.EquipmentType;

@Entity
public class Stage extends Equipment implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1863825782551414591L;
	


	public Stage() {}
	
	public Stage(String name, Money rentalPerDay, int length, int width) {
		super(name, rentalPerDay, length, width, EquipmentType.STAGE);
	}

}
