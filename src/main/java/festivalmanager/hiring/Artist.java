package festivalmanager.hiring;

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

/**
 * @author Tuan Giang Trinh
 */
@Entity
public class Artist {
	@Id@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	@Lob()
	private Money price;
	private int stageTechnician;

	@OneToMany(cascade = CascadeType.ALL)
	private List<BookingArtist> bookingArtists = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<Show> shows;

	/**
	 * constructor of an artist
	 * @param name
	 * @param price
	 * @param stageTechnician
	 */
	public Artist(@NotNull String name, @NotNull Money price, @NotNull int stageTechnician) {
		this.name = name;
		this.price = price;
		this.stageTechnician = stageTechnician;
		this.shows = new ArrayList<>();
	}
	public Artist() {
		this.shows = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Money getPrice() {
		return price;
	}

	public void setPrice(Money price) {
		this.price = price;
	}

	public int getStageTechnician() {
		return stageTechnician;
	}

	public void setStageTechnician(int stageTechnician) {
		this.stageTechnician = stageTechnician;
	}

	/**
	 * return all shows of this artist
	 * @return shows
	 */
	public Iterable<Show> getShows() {
		return this.shows;
	}

	/**
	 * return show with the given Id
	 * @param showId
	 * @return Show
	 */
	public Show getShow(long showId) {
		for(Show aShow : shows) {
			if(aShow.getId() == showId) {
				return aShow;
			}
		}
		return null;
	}

	/**
	 * add new show to the artist
	 * @param show
	 */
	public void addShow(Show show){
		shows.add(show);
	}

	/**
	 * add booking time of the artist, return true if it is added successfully
	 * @param startDate
	 * @param endDate
	 * @return boolean
	 */
	public boolean addBooking(LocalDate startDate, LocalDate endDate) {
		Interval festivalDateInterval = Interval.from(startDate.atStartOfDay()).to(endDate.atTime(23,59));
		for (BookingArtist aBooking : bookingArtists) {
			Interval aBookingDateInterval = Interval.from(aBooking.getStartDate().atStartOfDay())
					.to(aBooking.getEndDate().atTime(23, 59));
			if (aBookingDateInterval.overlaps(festivalDateInterval)) {
				System.out.println("Künstlerbelegt");
				return false;
			}
		}
		System.out.println("Künstler nicht belegt");
		BookingArtist bookingArtist = new BookingArtist(startDate, endDate);
		return bookingArtists.add(bookingArtist);
	}

	/**
	 * delete a booking of the artist, return true if it is deleted successfully
	 * @param startDate
	 * @param endDate
	 * @return boolean
	 */

	public boolean removeBooking(LocalDate startDate, LocalDate endDate) {
		for (BookingArtist aBooking : bookingArtists) {
			if (aBooking.getStartDate().equals(startDate) && aBooking.getEndDate().equals(endDate)) {
				return bookingArtists.remove(aBooking);
			}
		}
		return false;
	}

	/**
	 * return all the booking of the artist
	 * @return Iterable
	 */
	public Iterable<BookingArtist> getBookingArtist() {
		return bookingArtists;
	}

	/**
	 * return true if the artist as booking
	 * @return boolean
	 */
	public boolean hasBookingArtist() {
		return !(bookingArtists.isEmpty());
	}

}
