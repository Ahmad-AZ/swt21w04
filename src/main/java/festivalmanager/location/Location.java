package festivalmanager.location;

import java.time.LocalDate;
import java.util.ArrayList;
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
import org.salespointframework.time.Interval;
import org.springframework.util.Assert;

/**
 * class of {@link Location}
 *
 * @author Adrian Scholze
 */
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

	/**
	 * Creates a new {@link Location} with the given name, adress, price per day, 
	 * visitor capacity, stage capacity, image and groundview
	 *
	 * @param name must not be {@literal null}.
	 * @param adress must not be {@literal null}.
	 * @param pricePerDay must not be {@literal null}.
	 * @param visitorCapacity
	 * @param stageCapacity 
	 * @param image must not be {@literal null}
	 * @param groundView must not be {@literal null}.
	 */
	public Location(String name, String adress, Money pricePerDay, long visitorCapacity,
					long stageCapacity, String image, String groundView) {
		Assert.notNull(name, "Name must not be null!");
		Assert.notNull(adress, "Adress must not be null!");
		Assert.notNull(pricePerDay, "Price per day must not be null!");
		Assert.notNull(image, "Image must not be null!");
		Assert.notNull(groundView, "Ground view must not be null!");
		this.setName(name);
		this.setPricePerDay(pricePerDay);
		this.setAdress(adress);
		this.setVisitorCapacity(visitorCapacity);
		this.setStageCapacity(stageCapacity); 
		this.setImage(image);
		this.setGroundView(groundView);
	}
	
	public Location() {}
	
	/**
	 * Returns locations id.
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Creates new {@link Booking} for location with given date interval
	 * 
	 * @param startDate
	 * @param endDate
	 * @return {@code true}, if the booking does not overleap another
	 */
	public boolean addBooking(LocalDate startDate, LocalDate endDate) {
		
		Interval festivalDateInterval = Interval.from(startDate.atStartOfDay()).to(endDate.atTime(23,59));
		for (Booking aBooking : bookings) { 
			Interval aBookingDateInterval = Interval
											.from(aBooking.getStartDate().atStartOfDay())
											.to(aBooking.getEndDate().atTime(23,59));
			// startDate or endDate in an existing BookingInterval
			if(aBookingDateInterval.overlaps(festivalDateInterval)) {	
				return false;
			}
		}
		Booking booking = new Booking(startDate, endDate);
		return bookings.add(booking);
	}
	
	/**
	 * Removes {@link Booking} from location with given date interval
	 * 
	 * @param startDate
	 * @param endDate
	 * @return {@code true}, if the given booking could be removed
	 */
	public boolean removeBooking(LocalDate startDate, LocalDate endDate) {
		for (Booking aBooking : bookings) { 
			if(aBooking.getStartDate().equals(startDate) && aBooking.getEndDate().equals(endDate)) {
				return bookings.remove(aBooking);
			}
		}
		return false;
	}
 
	/**
	 * Returns all {@link Booking}s form the Location
	 * 
	 * @return all {@link Booking}s 
	 */
	public Iterable<Booking> getBookings() {
		return bookings;
	}
	
	/**
	 * Proves whether the Location has {@link Booking}s or not
	 * 
	 * @return {@code true}, if the location has {@link Booking}s
	 */
	public boolean hasBookings() {
		return !(bookings.isEmpty());
	}

	/**
	 * Returns locations name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets locations name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns locations adress.
	 * 
	 * @return adress
	 */
	public String getAdress() {
		return adress;
	}

	/**
	 * Sets locations adress
	 * 
	 * @param adress
	 */
	public void setAdress(String adress) {
		this.adress = adress;
	}

	/**
	 * Returns locations visitor capacity
	 * 
	 * @return visitorCapacity
	 */
	public long getVisitorCapacity() {
		return visitorCapacity;
	}

	/**
	 * Sets locations visitor capacity
	 * 
	 * @param visitorCapacity
	 */
	public void setVisitorCapacity(long visitorCapacity) {
		this.visitorCapacity = visitorCapacity;
	}

	/**
	 * Returns locations stage capacity
	 * 
	 * @return stageCapacity
	 */
	public long getStageCapacity() {
		return stageCapacity;
	}
	
	/**
	 * Sets locations stage capacity
	 * 
	 * @param stageCapacity
	 */
	public void setStageCapacity(long stageCapacity) {
		this.stageCapacity = stageCapacity;
	}

	/**
	 * Returns locations price per day
	 * 
	 * @return pricePerDay
	 */
	public Money getPricePerDay() {
		return pricePerDay;
	}

	/**
	 * Sets locations price per day
	 * 
	 * @param pricePerDay
	 */
	public void setPricePerDay(Money pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	/**
	 * Returns locations image as filename String
	 * 
	 * @return image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Sets locations image
	 * 
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Returns locations groundview as filename String
	 * 
	 * @return groundView
	 */
	public String getGroundView() {
		return groundView;
	}

	/**
	 * Sets locations groundview
	 * 
	 * @param groundView
	 */
	public void setGroundView(String groundView) {
		this.groundView = groundView;
	}
}
