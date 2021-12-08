package festivalmanager.planning;

import java.time.LocalDate;
import java.util.Optional;

import org.salespointframework.time.Interval;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import festivalmanager.festival.DaySchedule;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.StageSchedule;
import festivalmanager.location.Location;

@Controller
public class PlanScheduleController {
	
	private final PlanScheduleManagement planScheduleManagement;
	private final FestivalManagement festivalManagement;
	private long currentFestivalId;
	
	public PlanScheduleController(PlanScheduleManagement planScheduleManagement, FestivalManagement festivalManagement) {
		this.planScheduleManagement = planScheduleManagement;
		this.festivalManagement = festivalManagement;
	}
	
	@GetMapping("/schedule")  
	public String schedule(Model model, @ModelAttribute("currentFestivalId") long currentFestivalId) {
		if(currentFestivalId != 0) {
			this.currentFestivalId = currentFestivalId;
		}
		Optional<Festival> festival = festivalManagement.findById(currentFestivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();

			
			model.addAttribute("dayList", current.getFestivalInterval());
			model.addAttribute("stageList", current.getStages());
			
//			for(DaySchedule aDaySchedule : current.getDaySchedules()) {
//				for(StageSchedule aStageSchedule : aDaySchedule.getdaySchedules()) {
//					
//				}
//			}
			
			// required for secound nav-bar
			model.addAttribute("festival", current);
			return "schedule";
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
	@GetMapping("/schedule/{day}/{stageId}/{timeSlot}")
	public String setShow(@PathVariable("day") String day, @PathVariable("stageId") long stageId, @PathVariable("timeSlot") String timeSlot) {
		System.out.println(day);
		System.out.println(stageId);
		System.out.println(timeSlot);

		return "redirect:/schedulePre1";
	}
	
	
	
	
	
}
