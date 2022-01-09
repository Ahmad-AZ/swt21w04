package festivalmanager.festival;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import festivalmanager.Equipment.Stage;
import festivalmanager.hiring.Show;
import festivalmanager.staff.Person;

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
	
	public Schedule(TimeSlot timeSlot, Show show, Stage stage, LocalDate date, Person security) {
		this.date = date;
		this.timeSlot = timeSlot;
		this.stage = stage;
		this.show = show;
		this.security = security;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public Show getShow() {
		return show;	
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public Stage getStage() {
		return stage;
	}

	public LocalDate getDate() {
		return date;
	}

	public Person getSecurity() {
		return security;
	}

	public void setSecurity(Person security) {
		this.security = security;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

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
