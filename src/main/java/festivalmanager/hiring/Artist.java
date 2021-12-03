package festivalmanager.hiring;

import festivalmanager.location.Booking;
import org.javamoney.moneta.Money;
import org.salespointframework.time.Interval;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.salespointframework.core.Currencies.EURO;

@Entity
public class Artist {
	@Id@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	@Lob()
	private Money price;

	@OneToMany(cascade = CascadeType.ALL)
	private List<BookingArtist> bookingArtists = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<Show> shows;

	public Artist(@NotNull String name, @NotNull Money price) {
		this.name = name;
		this.price = price;
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

	public void addShow(Show show){
		shows.add(show);
	}

	public boolean addBooking(LocalDate startDate, LocalDate endDate) {
		Interval festivalDateInterval = Interval.from(startDate.atStartOfDay()).to(endDate.atTime(23,59));
		for (BookingArtist aBooking : bookingArtists) {
			Interval aBookingDateInterval = Interval.from(aBooking.getStartDate().atStartOfDay()).to(aBooking.getEndDate().atTime(23, 59));
			if (aBookingDateInterval.overlaps(festivalDateInterval)) {
				System.out.println("Künstlerbelegt");
				return false;
			}
		}
		System.out.println("Künstler nicht belegt");
		BookingArtist bookingArtist = new BookingArtist(startDate, endDate);
		return bookingArtists.add(bookingArtist);
	}

	public boolean removeBooking(LocalDate startDate, LocalDate endDate) {
		for (BookingArtist aBooking : bookingArtists) {
			if (aBooking.getStartDate().equals(startDate) && aBooking.getEndDate().equals(endDate)) {
				return bookingArtists.remove(aBooking);
			}
		}
		return false;
	}

	public Iterable<BookingArtist> getBookingArtist() {
		return bookingArtists;
	}

	public boolean hasBookingArtist() {
		return !(bookingArtists.isEmpty());
	}

}
