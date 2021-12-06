package festivalmanager.festival;

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

import festivalmanager.festival.StageSchedule.TimeSlot;

@Entity
public class DaySchedule {
		
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private LocalDate day;
	
	
	// StageId, StageScheduleId
	@OneToMany
	private List<StageSchedule> stageSchedules = new ArrayList<>();
	
	public DaySchedule(LocalDate day) {
		this.setDay(day);
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}
	
	public List<StageSchedule> getdaySchedule(){
		return stageSchedules;
	}
	
	public Map<TimeSlot, Long> getStageSchedule(Long stageId){
		for (StageSchedule aStageSchedule : stageSchedules) {
			if(aStageSchedule.getStageId() == stageId) {
				return aStageSchedule.getStageSchedule();
			}
		}
		return null;
	}
	
	public long getId() {
		return id;
	}
}
