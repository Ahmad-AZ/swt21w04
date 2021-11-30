package festivalmanager.location;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.javamoney.moneta.Money;
import org.salespointframework.time.Interval;
import org.springframework.data.util.Streamable;

//@NamedEntityGraph(
//	    name = "graph.locationBookings",
//	    attributeNodes = @NamedAttributeNode("booking")
//	)

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
	private List<Area> areas = new ArrayList<>();

 
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
//		//Test Interval overleap functionality
//		Interval a = Interval.from(LocalDateTime.of(2021, 11, 1, 0, 0)).to(LocalDateTime.of(2021, 11, 25, 23, 59));
//		Interval b = Interval.from(LocalDateTime.of(2021, 11, 5, 0, 0)).to(LocalDateTime.of(2021, 11, 14, 23, 59));
//		boolean intervaltry = b.overlaps(a);
//		System.out.println("Interval try:"+ intervaltry);
		
		Interval festivalDateInterval = Interval.from(startDate.atStartOfDay()).to(endDate.atTime(23,59));
		for (Booking aBooking : bookings) { 
			Interval aBookingDateInterval = Interval.from(aBooking.getStartDate().atStartOfDay()).to(aBooking.getEndDate().atTime(23,59));
			// startDate or endDate in an existing BookingInterval
			if(aBookingDateInterval.overlaps(festivalDateInterval)) {
				System.out.println("Location belegt");
				return false;
			}
		}
		System.out.println("nicht belegt");
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
 
	public Iterable<Booking> getBookings() {
		return bookings;
	}
	
	public boolean hasBookings() {
		return !(bookings.isEmpty());
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
