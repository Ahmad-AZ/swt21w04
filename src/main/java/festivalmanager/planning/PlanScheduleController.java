package festivalmanager.planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.salespointframework.core.SalespointIdentifier;
import org.salespointframework.time.Interval;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.Schedule.TimeSlot;
import festivalmanager.utils.UtilsManagement;


@Controller
public class PlanScheduleController {
	
	private final PlanScheduleManagement planScheduleManagement;
	private final FestivalManagement festivalManagement;
	private final UtilsManagement utilsManagement;
	
	public PlanScheduleController(PlanScheduleManagement planScheduleManagement,
								  FestivalManagement festivalManagement, UtilsManagement utilsManagement) {
		this.planScheduleManagement = planScheduleManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Programm";
	}
	
	@GetMapping("/schedule/{festivalId}")  
	public String schedule(Model model, @PathVariable("festivalId") long festivalId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();

			// gives List of all Festival days
			List<LocalDate> dayList = new ArrayList<>();
			LocalDate currentDate = current.getStartDate();
			Interval festivalInterval = Interval.from(current.getStartDate().atStartOfDay())
										.to(current.getEndDate().atTime(23, 5));
			while(festivalInterval.contains(currentDate.atTime(12, 00))) {
				dayList.add(currentDate);
				currentDate = currentDate.plusDays(1);
			}
			model.addAttribute("dayList", dayList);
			
			List<TimeSlot> tsl =  new ArrayList<>();
			tsl.add(TimeSlot.TS1);
			tsl.add(TimeSlot.TS2);
			tsl.add(TimeSlot.TS3);
			tsl.add(TimeSlot.TS4);
			tsl.add(TimeSlot.TS5);
			
			model.addAttribute("timeSlotList",tsl);
			model.addAttribute("festival", current);
		}
		utilsManagement.setCurrentPageLowerHeader("program");
		utilsManagement.prepareModel(model);
		return "schedule.html";
		
	}
	
	@GetMapping("/schedule/{festivalId}/{day}/{stageId}/{timeSlot}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String getShowSelectDialog(@PathVariable("day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, 
										@PathVariable("stageId") SalespointIdentifier stageId, 
										@PathVariable("timeSlot") String timeSlot, 
										@PathVariable("festivalId") long festivalId, Model model) {
		
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			model.addAttribute("dialog", "edit schedule");
			
			model.addAttribute("festival", current);
			model.addAttribute("date", date);
			model.addAttribute("stageId", stageId);
			model.addAttribute("timeSlot", timeSlot);
			
			model.addAttribute("showsToAdd", current.getShows());
			//System.out.println(planScheduleManagement.getAvailableSecurity(currentFestival, date, timeSlot, stageId));
			model.addAttribute("securitysToAdd", planScheduleManagement.getAvailableSecurity(current, date, timeSlot, stageId));
	
			utilsManagement.prepareModel(model);
			return "schedule.html";
		} else {
			return "redirect:/schedule/"+ festivalId;
		}
	}
	
	@PostMapping("/schedule/{festivalId}/{day}/{stageId}/{timeSlot}/editSchedule")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String chooseShow(@PathVariable("day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, 
									@PathVariable("stageId") SalespointIdentifier stageId, 
									@PathVariable("timeSlot") String timeSlot, 
									@RequestParam("show") long showId, 
									@RequestParam("person") long personId, 
									@PathVariable("festivalId") long festivalId, Model model) {
		
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			//System.out.println(showId);
			Stage stage = festival.get().getStage(stageId);
			if(stage != null) {
				planScheduleManagement.setShow(date, stage, timeSlot, showId, festival.get(), personId);
			}
		}	
		return "redirect:/schedule/"+ festivalId;

	}
	
}
