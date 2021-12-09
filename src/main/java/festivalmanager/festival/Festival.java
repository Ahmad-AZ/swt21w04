package festivalmanager.festival;


import javax.persistence.*;

import org.salespointframework.time.Interval;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Schedule.TimeSlot;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.Show;
import festivalmanager.location.Location;

import java.time.LocalDate;
import java.util.*;

@Entity
public class Festival {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	@OneToMany(cascade =  CascadeType.ALL)
	private Set<Artist> artists;
	
	@OneToOne()
	private Location location;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Schedule> schedules = new HashSet<>();
	
	@ElementCollection
	private Map<Long, Long> rentedEquipments = new HashMap<>();
	
	@OneToMany()
	private List<Stage> stages = new ArrayList<>();

	public Festival(String name, LocalDate startDate, LocalDate endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = null;
		this.artists = new HashSet<Artist>();
	}

	public Festival(String name) {
		this.name = name;
		this.location = null;
		this.artists = new HashSet<Artist>();
	}
	
	public Festival() {
		
	}
	
	public long getId() {
		return id;
	}


	public LocalDate getStartDate() {
		return startDate;
	} 

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public List<LocalDate> getFestivalInterval() {
		List<LocalDate> dateList = new ArrayList<>();
		LocalDate currentDate = startDate;
		Interval festivalInterval = Interval.from(startDate.atStartOfDay()).to(endDate.atTime(23, 5));
		while(festivalInterval.contains(currentDate.atTime(12, 00))) {
			dateList.add(currentDate);
			currentDate = currentDate.plusDays(1);
			System.out.println( currentDate);
		}
		
		return dateList;
	}

	public String getName() {
		return name;
	}
	
	public Location getLocation() {
		return location; 
	}
	public Iterable<Artist> getArtist(){
		return this.artists;
	}
	
	public void setLocation(Location location) {
		this.location=location;
	}
	public void addArtist(Artist artist){
		artists.add(artist);
	}
	public boolean artistsIsEmpty(){
		return this.artists.isEmpty();
	}

	public void setEquipments(long id, long amount) {
		rentedEquipments.put(id, amount);
	}
	
	public Map<Long, Long> getEquipments(){
		return rentedEquipments;
	}

	public void deleteAll() {
		this.artists = new HashSet<>();
	}
	public Set<Schedule> getSchedules(){
		return schedules;
	}
	
	public boolean addSchedule(TimeSlot timeSlot, Show show, Stage stage, LocalDate date) {

		return schedules.add(new Schedule(timeSlot, show, stage, date));
	}
	
	public Schedule getSchedule(TimeSlot timeSlot, Stage stage, LocalDate date) {
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getDate().equals(date) && aSchedule.getStage().equals(stage) && aSchedule.getTimeSlot().equals(timeSlot)) {
				return aSchedule;
			}
		}
		return null;
	}
	
	public String getScheduleShowName(TimeSlot timeSlot, Stage stage, LocalDate date) {
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getDate().equals(date) && aSchedule.getStage().equals(stage) && aSchedule.getTimeSlot().equals(timeSlot)) {
				return aSchedule.getShow().getName();
			}
		}
		return "Keine";
	}
	
	public boolean containsSchedule(TimeSlot timeSlot, Stage stage, LocalDate date) {
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getDate().equals(date) && aSchedule.getStage().equals(stage) && aSchedule.getTimeSlot().equals(timeSlot)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean removeSchedule(TimeSlot timeSlot, Stage stage, LocalDate date) {
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getDate().equals(date) && aSchedule.getStage().equals(stage) && aSchedule.getTimeSlot().equals(timeSlot)) {
				return schedules.remove(aSchedule);
			}
		}
		return false;
	}
	

	public Iterable<Stage> getStages(){
		return stages;
	}
	
	public boolean addStage(Stage stage) {
		return stages.add(stage);
	}
	
	public boolean removeStage(Stage stage) {
		return stages.remove(stage);
	}
	

//	public void deleteArtist(Artist artist){
//		Iterator <Artist> i = artists.iterator();
//		while (i.hasNext()) { 
//			if (i.next().getId() == artist.getId()){
//				artists.remove(i);
//			}
//		}
//		for (Artist artist1 : artists) {
//			if (artist1.getId() == artist.getId()){
//				artists.remove(artist1);
//			}
//		}
//
//	}
}
