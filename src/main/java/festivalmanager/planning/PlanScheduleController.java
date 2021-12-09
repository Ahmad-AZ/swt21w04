package festivalmanager.planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;

import org.salespointframework.time.Interval;
import org.springframework.data.util.Streamable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import festivalmanager.festival.DaySchedule;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.Schedule.TimeSlot;
import festivalmanager.festival.StageSchedule;
import festivalmanager.location.Location;

@Controller
public class PlanScheduleController {
	
	private final PlanScheduleManagement planScheduleManagement;
	private final FestivalManagement festivalManagement;
	private long currentFestivalId;
	private Festival currentFestival;
	
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
			currentFestival = current;

			
			model.addAttribute("dayList", current.getFestivalInterval());
			model.addAttribute("stageList", current.getStages());
			
			List<TimeSlot> tsl =  new ArrayList<>();
			tsl.add(TimeSlot.TS1);
			tsl.add(TimeSlot.TS2);
			tsl.add(TimeSlot.TS3);
			tsl.add(TimeSlot.TS4);
			tsl.add(TimeSlot.TS5);
			
			model.addAttribute("timeSlotList",tsl);
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
	public String getShowSelectDialog(@PathVariable("day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("stageId") long stageId, @PathVariable("timeSlot") String timeSlot, Model model) {
		System.out.println(date);
		System.out.println(stageId);
		System.out.println(timeSlot);
		
		model.addAttribute("dialog", "choose show");
		
		model.addAttribute("festival", currentFestival);
		model.addAttribute("date", date);
		model.addAttribute("stageId", stageId);
		model.addAttribute("timeSlot", timeSlot);
		
		model.addAttribute("showsToAdd", planScheduleManagement.getShows(currentFestivalId));

		

		return "/schedule";
	}
	
	@PostMapping("/schedule/{day}/{stageId}/{timeSlot}/chooseShow")
	public String chooseShow(@PathVariable("day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, 
									@PathVariable("stageId") long stageId, 
									@PathVariable("timeSlot") String timeSlot, 
									@RequestParam("show") long showId, Model model) {
		
		System.out.println(showId);
		planScheduleManagement.setShow(date, stageId, timeSlot, showId, currentFestivalId);
		System.out.println("afterall");
		return "redirect:/schedulePre1";
	}
	
	
	
	
}
