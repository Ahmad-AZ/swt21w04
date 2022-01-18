package festivalmanager.festival;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.util.Assert;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Schedule.TimeSlot;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.Show;
import festivalmanager.location.Location;
import festivalmanager.staff.Person;

/**
 * class of {@link Festival}
 *
 * @author Adrian Scholze
 */
@Entity
public class Festival {
	

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	@ManyToMany()
	private Set<Artist> artists;
	
	@OneToOne()
	private Location location;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Schedule> schedules = new HashSet<>();
	
	/**
	 * represents the non-{@link Stage} {@link Equipment}s with their currently rented amount
	 */
	@ElementCollection
	private Map<SalespointIdentifier, Long> rentedEquipments = new HashMap<>();
	
	// to delete Stages, when festival would been removed
	@OneToMany(cascade = CascadeType.ALL)
	private List<Stage> stages = new ArrayList<>();
	

	public Festival(String name, LocalDate startDate, LocalDate endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = null;
		this.artists = new HashSet<Artist>();
	}
	
	public Festival() {}
	
	public long getId() {
		return id;
	}
	
	/**
	 * changes the festival name
	 *
	 * @param name must not be {@literal null}
	 */
	public void setName(String name) {
		Assert.notNull(name, "Artist must not be null!");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public LocalDate getStartDate() {
		return startDate;
	} 

	public LocalDate getEndDate() {
		return endDate;
	}
	
	/**
	 * sets a {@link Location} for this festival
	 *
	 * @param location (could be {@literal null} if no location is 
	 */
	public void setLocation(Location location) {
		this.location=location;
	}
	
	public Location getLocation() {
		return location; 
	}
	
	public Iterable<Artist> getArtist(){
		return this.artists;
	}
	
	/**
	 * inserts a new {@link Artist} 
	 * in the list of artists currently booked for this festival
	 *
	 * @param artist must not be {@literal null}
	 */
	public void addArtist(Artist artist){
		Assert.notNull(artist, "Artist must not be null!");
		artists.add(artist);
	}


	/**
	 * returns true if no artist are booked for the festival
	 *
	 * @erturn true if artists is empty
	 */
	public boolean artistsIsEmpty(){
		return this.artists.isEmpty();
	}

	/**
	 * returns true, if the given {@link Artist} is booked for this festival
	 *
	 * @param artist must not be {@literal null}
	 * @erturn true if artists contains given artist
	 */
	public boolean getArtistBookedState(Artist artist) {
		Assert.notNull(artist, "Artist must not be null!");
		for(Artist anArtist : artists) {
			if(anArtist.getId() == artist.getId()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * returns all {@link Show}s from all currently booked {@link Artist}s
	 *
	 * @return all {@link Show} entities
	 */
	public List<Show> getShows(){
		List<Show> shows = new ArrayList<>();
		for(Artist anArtist : artists) {
			for(Show aShow : anArtist.getShows()) {
				shows.add(aShow);
			}
		}
		return shows;
	}
	

	public Show getShow(long showId){
		for(Artist anArtist : artists) {
			if(anArtist.getShow(showId) != null) {
				return anArtist.getShow(showId);
			}
		}
		return null;
	}
	
	
	public void setEquipments(SalespointIdentifier id, long amount) {
		rentedEquipments.put(id, amount);
	}
	
	public Map<SalespointIdentifier, Long> getEquipments(){
		return rentedEquipments;
	}
	

	public void deleteAllArtists() {
		for(Schedule aSchedule : schedules) {
			aSchedule.setShow(null);
		}
		this.artists = new HashSet<>();
	}
		
	public List<Person> getUnavailableSecuritys(LocalDate date, TimeSlot timeSlot, SalespointIdentifier stageId){
		List<Person> unavailableSecuritys = new ArrayList<>();
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getDate().equals(date) && aSchedule.getTimeSlot().equals(timeSlot)
					&& !aSchedule.getStage().getId().equals(stageId)) {
				unavailableSecuritys.add(aSchedule.getSecurity());
			}
		}		
		return unavailableSecuritys;
	}
	
	/**
	 * Creates a new {@link Schedule} if none exists at the given parameters
	 *
	 */
	public boolean addSchedule(TimeSlot timeSlot, Show show, Stage stage, LocalDate date, Person security) {
		// schedules contains schedule already
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getDate().equals(date)
					&& aSchedule.getStage().equals(stage) && aSchedule.getTimeSlot().equals(timeSlot)) {
				// find schedule --> change show
				aSchedule.setShow(show);
				aSchedule.setSecurity(security);
				return true;	
			}
		}
		
		// add new Schedule
		return schedules.add(new Schedule(timeSlot, show, stage, date, security));

	}
	
	public String getScheduleShowName(TimeSlot timeSlot, Stage stage, LocalDate date) {
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getDate().equals(date)
					&& aSchedule.getStage().equals(stage) && aSchedule.getTimeSlot().equals(timeSlot)) {
				if(aSchedule.getShow() != null) {
					return aSchedule.getShow().getName();
				} else {
					break;
				}
			}
		}
		return "Keine";
	}
	
	public String getScheduleSecurityName(TimeSlot timeSlot, Stage stage, LocalDate date) {
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getDate().equals(date) && aSchedule.getStage().equals(stage)
					&& aSchedule.getTimeSlot().equals(timeSlot)) {
				if(aSchedule.getSecurity() != null) {
					return aSchedule.getSecurity().getName();
				} else {
					break;
				}
			}
		}
		return "Keine";
	}
	
	public boolean removeSchedule(TimeSlot timeSlot, Stage stage, LocalDate date) {
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getDate().equals(date)
					&& aSchedule.getStage().equals(stage) && aSchedule.getTimeSlot().equals(timeSlot)) {
				System.out.println("before remove schedule");
				return schedules.remove(aSchedule);
			}
		}
		return false;
	}
	
	public void removeSecurity(long securityId) {
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getSecurity().getId() == securityId) {
				aSchedule.setSecurity(null);
			}
		}
	}
	
	public List<Stage> getStages(){
		return stages;
	}
		
	public boolean addStage(Stage stage) {
		return stages.add(stage);
	}
	
	public boolean removeStage(Stage stage) {
		// get all Schedules to remove at this stage
		List<Schedule> deleteList = new ArrayList<>();
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getStage().equals(stage)) {
				deleteList.add(aSchedule);
			}
		}
		schedules.removeAll(deleteList);
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
