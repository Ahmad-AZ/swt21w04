package festivalmanager.planning;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;

@Service
@Transactional
public class PlanScheduleManagement {

	private final FestivalManagement festivalManagement;
	
	public PlanScheduleManagement(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
	}
}
