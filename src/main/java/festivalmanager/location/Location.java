package festivalmanager.location;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.javamoney.moneta.Money;


@Entity
public class Location{
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private String name;
	
	private String image;
	private String groundView;
	
	@Lob()
	private Money pricePerDay;
	private String adress;

	private long visitorCapacity;
	private long stageCapacity; 

 
	@OneToMany(cascade = CascadeType.ALL)
	private List<Booking> bookings = new ArrayList<>();

	
	public Location(String name, String adress, Money pricePerDay, long visitorCapacity, long stageCapacity, String image, String groundView) {
		this.setName(name);
		this.setPricePerDay(pricePerDay);
		this.setAdress(adress);
		this.setVisitorCapacity(visitorCapacity);
		this.setStageCapacity(stageCapacity); 
		this.setImage(image);
		this.setGroundView(groundView);
	}
	
	public Location() {
		
	}
	
	public long getId() {
		return id;
	}
	
	public boolean addBooking(LocalDate startDate, LocalDate endDate) {
		Booking booking = new Booking(startDate, endDate);
		return bookings.add(booking);
	}
	
	public boolean removeBooking(LocalDate startDate, LocalDate endDate) {
		for (Booking aBooking : bookings) { 
			if(aBooking.getStartDate().equals(startDate) && aBooking.getEndDate().equals(endDate)) {
				return bookings.remove(aBooking);
			}
		}
		return false;
		
	}
 
	public List<Booking> getBookings() {
		return bookings;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public long getVisitorCapacity() {
		return visitorCapacity;
	}

	public void setVisitorCapacity(long visitorCapacity) {
		this.visitorCapacity = visitorCapacity;
	}

	public long getStageCapacity() {
		return stageCapacity;
	}

	public void setStageCapacity(long stageCapacity) {
		this.stageCapacity = stageCapacity;
	}

	public Money getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(Money pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getGroundView() {
		return groundView;
	}

	public void setGroundView(String groundView) {
		this.groundView = groundView;
	}
}
