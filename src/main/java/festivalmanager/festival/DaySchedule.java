package festivalmanager.festival;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import festivalmanager.Equipment.Stage;
import festivalmanager.hiring.Show;

@Entity
public class DaySchedule implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6550089235475594083L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private LocalDate day;
	
	@OneToMany
	private List<StageSchedule> stageSchedules = new ArrayList<>();
	
	
	public DaySchedule() {}
	
	public DaySchedule(LocalDate day) {
		this.setDay(day);
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}
	
	public List<StageSchedule> getDaySchedules(){
		return stageSchedules;
	}
	
	public StageSchedule getStageSchedule(Stage stage){
		for (StageSchedule aStageSchedule : stageSchedules) {
			if(aStageSchedule.getStage().equals(stage)) {
				return aStageSchedule;
			}
		}
		return null;
	}
	
	public StageSchedule getStageSchedule(long stageId){
		for (StageSchedule aStageSchedule : stageSchedules) {
			if(aStageSchedule.getStage().getId() == stageId) {
				return aStageSchedule;
			}
		}
		return null;
	}
	
	public boolean setStageSchedule(Stage stage, Schedule schedule) {
		// if StageSchedule already exsits
		for (StageSchedule aStageSchedule : stageSchedules) {
			if(aStageSchedule.getStage().equals(stage)) {
				return aStageSchedule.setSchedule(schedule);
			}
		}
		
		// else add new StageSchedule
		StageSchedule stageSchedule = new StageSchedule(stage);
		boolean success = stageSchedule.setSchedule(schedule);
		stageSchedules.add(stageSchedule);
		return success;
	}
	
	public long getId() {
		return id;
	}
}
