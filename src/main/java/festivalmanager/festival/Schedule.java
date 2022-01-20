package festivalmanager.festival;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.util.Assert;

import festivalmanager.Equipment.Stage;
import festivalmanager.hiring.Show;
import festivalmanager.staff.Person;

/**
 * class of {@link Schedule}
 *
 * @author Adrian Scholze
 */
@Entity
public class Schedule implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6162307715703736507L;

	public static enum TimeSlot {
		TS1, TS2, TS3, TS4, TS5
	}
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private TimeSlot timeSlot;
	@OneToOne
	private Show show;
	@OneToOne
	private Stage stage;
	
	@OneToOne
	private Person security;
	
	private LocalDate date;
	
	public Schedule() {}
	
	/**
	 * Creates a new {@link Schedule} with the given timeslot, show, stage, date and security.
	 *
	 * @param timeSlot must not be {@literal null}.
	 * @param show.
	 * @param stage must not be {@literal null}
	 * @param date must not be {@literal null}.
	 * @param security.
	 */
	public Schedule(TimeSlot timeSlot, Show show, Stage stage, LocalDate date, Person security) {
		Assert.notNull(stage, "Stage must not be null!");
		Assert.notNull(date, "Date must not be null!");
		Assert.notNull(timeSlot, "Timeslot must not be null!");
		this.date = date;
		this.timeSlot = timeSlot;
		this.stage = stage;
		this.show = show;
		this.security = security;
	}

	/**
	 * Returns scheudles {@link TimeSlot}.
	 * 
	 * @return timeslot
	 */
	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	/**
	 * Returns scheudles {@link Show}.
	 * 
	 * @return show
	 */
	public Show getShow() {
		return show;	
	}

	/**
	 * Sets schedules {@link Show}
	 * 
	 * @param show
	 */
	public void setShow(Show show) {
		this.show = show;
	}

	/**
	 * Returns scheudles {@link Stage}.
	 * 
	 * @return stage
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * Returns scheudles date.
	 * 
	 * @return date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Returns scheudles security {@link Person}.
	 * 
	 * @return security
	 */
	public Person getSecurity() {
		return security;
	}

	/**
	 * Sets schedules security {@link Person}
	 * 
	 * @param security
	 */
	public void setSecurity(Person security) {
		this.security = security;
	}

	/**
	 * Returns scheudles hash code.
	 * 
	 * @return hash Code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

    /**
     * Returns {@code true} if the schedule is equal to each other
     * and {@code false} otherwise.
     * 
     * @param a an object
     * @return {@code true} if the arguments are equal to each other
     * and {@code false} otherwise
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Schedule other = (Schedule) obj;
		return id == other.id;
	}
	
	
}
