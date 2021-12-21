package festivalmanager.festival;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
	
	public Schedule(TimeSlot timeSlot, Show show, Stage stage, LocalDate date) {
		this.setTimeSlot(timeSlot);
		this.setShow(show);
		this.setDate(date);
		this.setStage(stage);
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
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

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Person getSecurity() {
		return security;
	}

	public void setSecurity(Person security) {
		this.security = security;
	}
}
