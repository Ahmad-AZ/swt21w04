package festivalmanager.location;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.Assert;
/**
 * class of {@link Booking}
 *
 * @author Adrian Scholze
 */
@Entity
@Table(name = "BOOKINGS")
public class Booking implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Id @GeneratedValue long id;
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	@SuppressWarnings("unused")
	private Booking() {}
	
	/**
	 * Creates a new {@link Booking} with the given start and end date.
	 *
	 * @param date must not be {@literal null}.
	 * @param endDate must not be {@literal null}.
	 */
	public Booking(LocalDate startDate, LocalDate endDate) {
		Assert.notNull(startDate, "start date must not be null!");
		Assert.notNull(endDate, "end date must not be null!");
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/**
	 * Returns bookings id.
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Returns bookings end date.
	 * 
	 * @return endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * Returns bookings start date.
	 * 
	 * @return startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

}
