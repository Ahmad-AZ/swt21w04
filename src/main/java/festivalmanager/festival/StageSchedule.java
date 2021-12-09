package festivalmanager.festival;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Schedule.TimeSlot;
import festivalmanager.hiring.Show;

@Entity
public class StageSchedule implements Serializable{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4489866224720215803L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private Stage stage;
	
	@OneToMany
	private List<Schedule> schedules = new ArrayList<>();
	
	public StageSchedule() {}
	
	public StageSchedule(Stage stage) {
		this.setStage(stage);
	}
	
	public List<Schedule> getSchedules(){
		return schedules;
	}
	public boolean setSchedule(Schedule schedule) {
		// if Timeslot is already filled replace
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getTimeSlot().equals(schedule.getTimeSlot())) {
				schedules.set(schedules.indexOf(aSchedule), schedule);
				return true;
			}
		}
		// else add new
		return schedules.add(schedule);
	}
	
	public Show getShowAtTS1() {
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getTimeSlot().equals(TimeSlot.TS1)) {
				return aSchedule.getShow();
			}
		}
		return null;
	}
	

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
