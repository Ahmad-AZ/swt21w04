package festivalmanager.festival;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import festivalmanager.hiring.Show;

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
	
	public Schedule() {}
	
	public Schedule(TimeSlot timeSlot, Show show) {
		this.setTimeSlot(timeSlot);
		this.setShow(show);
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
}
