package festivalmanager.festival;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import festivalmanager.Equipment.Equipment;

@Entity
public class StageSchedule {

	public static enum TimeSlot {
		TS1, TS2, TS3, TS4, TS5
	}
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private Long stageId;
	
	@ElementCollection
	private Map<TimeSlot, Long> stageSchedule = new HashMap<>();
	
	public StageSchedule(Long stageId) {
		setStageId(stageId);
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}
	
	public Map<TimeSlot, Long> getStageSchedule(){
		return stageSchedule;
	}

}
