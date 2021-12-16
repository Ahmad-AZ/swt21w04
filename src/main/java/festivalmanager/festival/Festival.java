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
	@ManyToMany(cascade =  CascadeType.ALL)
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
	
	public void setName(String name) {
		this.name = name;
	}


	public LocalDate getStartDate() {
		return startDate;
	} 

	public LocalDate getEndDate() {
		return endDate;
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
	// not really required
	public boolean artistsIsEmpty(){
		return this.artists.isEmpty();
	}

	public void setEquipments(long id, long amount) {
		rentedEquipments.put(id, amount);
	}
	
	public Map<Long, Long> getEquipments(){
		return rentedEquipments;
	}
	
	// rename to deleteAllArtists
	public void deleteAll() {
		this.artists = new HashSet<>();
	}
	public Set<Schedule> getSchedules(){
		return schedules;
	}
	
	public boolean addSchedule(Schedule schedule) {

		return schedules.add(schedule);
	}
	
	// not really required
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
	
	// not really required
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
	

	public List<Stage> getStages(){
		return stages;
	}
	
	public boolean addStage(Stage stage) {
		return stages.add(stage);
	}
	
	public boolean removeStage(Stage stage) {
		return stages.remove(stage);
	}
	
	public long getRequiredSecurityCount() {
		if (location != null) {
			long visitorCapacity = location.getVisitorCapacity();
			return visitorCapacity / 100;
		}
		return 0;
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
