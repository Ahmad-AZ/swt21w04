package prototype.location;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;



public class Location extends Product {
	private  @Id @GeneratedValue long id;

	private String adress;

	private int visitorCapacity;
	private int stageCapacity;


	@OneToMany(cascade = CascadeType.ALL)
	private List<Booking> bookings = new ArrayList<>();

	
	public Location(String name, String adress, Money pricePerDay, int visitorCapacity, int stageCapacity) {
		super(name, pricePerDay);
		
		this.adress = adress;
		this.visitorCapacity = visitorCapacity;
		this.stageCapacity = stageCapacity;
	}

	public String getAdress() {
		return adress;
	}

	public int getVisitorCapacity() {
		return visitorCapacity;
	}

	public int getStageCapacity() {
		return stageCapacity;
	}


	public List<Booking> getBookings() {
		return bookings;
	}
}
