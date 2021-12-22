package festivalmanager.planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import festivalmanager.utils.UtilsManagement;

import org.salespointframework.core.SalespointIdentifier;
import org.salespointframework.time.Interval;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.Schedule.TimeSlot;


@Controller
public class PlanScheduleController {
	
	private final PlanScheduleManagement planScheduleManagement;
	private final FestivalManagement festivalManagement;
	private final UtilsManagement utilsManagement;
	private long currentFestivalId;
	private Festival currentFestival;
	
	public PlanScheduleController(PlanScheduleManagement planScheduleManagement, FestivalManagement festivalManagement, UtilsManagement utilsManagement) {
		this.planScheduleManagement = planScheduleManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Programm";
	}
	

	@GetMapping("/schedule")  
	public String schedule(Model model) {
			this.currentFestivalId = utilsManagement.getCurrentFestivalId();
		Optional<Festival> festival = festivalManagement.findById(currentFestivalId);
		if (festival.isPresent()) {
			Festival current = festival.get();
			currentFestival = current;

			// gives List of all Festival days
			List<LocalDate> dayList = new ArrayList<>();
			LocalDate currentDate = current.getStartDate();
			Interval festivalInterval = Interval.from(current.getStartDate().atStartOfDay()).to(current.getEndDate().atTime(23, 5));
			while(festivalInterval.contains(currentDate.atTime(12, 00))) {
				dayList.add(currentDate);
				currentDate = currentDate.plusDays(1);
			}
			model.addAttribute("dayList", dayList);
			
			model.addAttribute("stageList", current.getStages());
			
			List<TimeSlot> tsl =  new ArrayList<>();
			tsl.add(TimeSlot.TS1);
			tsl.add(TimeSlot.TS2);
			tsl.add(TimeSlot.TS3);
			tsl.add(TimeSlot.TS4);
			tsl.add(TimeSlot.TS5);
			
			model.addAttribute("timeSlotList",tsl);
			model.addAttribute("festival", current);
			utilsManagement.setCurrentFestivalId(currentFestival.getId());
			utilsManagement.setCurrentPageLowerHeader("program");
			utilsManagement.prepareModel(model);
			return "/schedule";
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}
	}
	
	@GetMapping("/schedule/{day}/{stageId}/{timeSlot}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String getShowSelectDialog(@PathVariable("day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, 
										@PathVariable("stageId") SalespointIdentifier stageId, 
										@PathVariable("timeSlot") String timeSlot, Model model) {
		
		model.addAttribute("dialog", "edit schedule");
		
		model.addAttribute("festival", currentFestival);
		model.addAttribute("date", date);
		model.addAttribute("stageId", stageId);
		model.addAttribute("timeSlot", timeSlot);
		
		model.addAttribute("showsToAdd", planScheduleManagement.getShows(currentFestivalId));
		System.out.println(planScheduleManagement.getAvailableSecurity(currentFestival, date, timeSlot));
		model.addAttribute("securitysToAdd", planScheduleManagement.getAvailableSecurity(currentFestival, date, timeSlot));

		utilsManagement.prepareModel(model);
		return "/schedule";
	}
	
	@PostMapping("/schedule/{day}/{stageId}/{timeSlot}/editSchedule")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
	public String chooseShow(@PathVariable("day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, 
									@PathVariable("stageId") SalespointIdentifier stageId, 
									@PathVariable("timeSlot") String timeSlot, 
									@RequestParam("show") long showId, 
									@RequestParam("person") long personId, Model model) {

		System.out.println(showId);
		
		Stage stage = planScheduleManagement.getStages(currentFestival, stageId);
		if(stage != null) {
			planScheduleManagement.setShow(date, stage, timeSlot, showId, currentFestivalId, personId);
			
			
			System.out.println("afterall");
		}
		
		

		return "redirect:/schedule";
	}
	
	
	
//	@GetMapping("/schedule/{day}/{stageId}/{timeSlot}/security")
//	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
//	public String getSecuritySelectDialog(@PathVariable("day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, 
//										@PathVariable("stageId") long stageId, 
//										@PathVariable("timeSlot") String timeSlot, Model model) {
//			
//			model.addAttribute("festival", currentFestival);
//			model.addAttribute("date", date);
//			model.addAttribute("stageId", stageId);
//			model.addAttribute("timeSlot", timeSlot);
//				
//			model.addAttribute("securitysToAdd", planScheduleManagement.getAvailableSecurity(currentFestival, date, timeSlot));
//
//		
//		
//		utilsManagement.prepareModel(model);
//		return "/schedule";
//	}
	
//	@PostMapping("/schedule/{day}/{stageId}/{timeSlot}/chooseShow")
//	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER') || hasRole('MANAGER')")
//	public String chooseSecurity(@PathVariable("day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, 
//									@PathVariable("stageId") long stageId, 
//									@PathVariable("timeSlot") String timeSlot, 
//									@RequestParam("security") long personId, Model model) {
//		
//		System.out.println(personId);
//		planScheduleManagement.setSecurity(date, stageId, timeSlot, personId, currentFestivalId);
//		System.out.println("afterall");
//		return "redirect:/schedule";
//	}
	
	
	
	
}
