package prototype.location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.money.MonetaryAmount;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;


@Entity
public class Location{
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	@Lob()
	private Money pricePerDay;
	private String adress;

	private int visitorCapacity;
	private int stageCapacity; 

 
	@OneToMany(cascade = CascadeType.ALL)
	private List<Booking> bookings = new ArrayList<>();

	
	public Location(String name, String adress, Money pricePerDay, int visitorCapacity, int stageCapacity) {
		this.name = name;
		this.pricePerDay = pricePerDay;
		this.adress = adress;
		this.visitorCapacity = visitorCapacity;
		this.stageCapacity = stageCapacity; 
	}
	
	public Location() {
		
	}
	public long getId() {
		return id;
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

	public String getName() {
		return name;
	}

	public Money getPricePerDay() {
		return pricePerDay;
	}
	
	public boolean addBooking(Date startDate, Date endDate) {
		Booking booking = new Booking(startDate, endDate);
		return bookings.add(booking);
	}
 
	public List<Booking> getBookings() {
		return bookings;
	}
}
