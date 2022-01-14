package festivalmanager.hiring;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * booking of the {@link Artist}
 *
 * @author Tuan Giang Trinh
 */
@Entity
@Table(name = "BOOKARTIST")
public class BookingArtist implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Id @GeneratedValue long id;

	private LocalDate startDate;
	private LocalDate endDate;

	private BookingArtist() {}

	/**
	 * Create the {@link BookingArtist} with booking date
	 * @param startDate
	 * @param endDate
	 */
	public BookingArtist(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public long getId() {
		return id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
}
