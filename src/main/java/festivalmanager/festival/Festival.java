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

	private boolean announced = false;

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
	
	/**
	 * Creates a new {@link Festival} with the given name, start and end date.
	 *
	 * @param name must not be {@literal null}.
	 * @param startDate
	 * @param endDate
	 */
	public Festival(String name, LocalDate startDate, LocalDate endDate) {
		Assert.notNull(name, "Name must not be null!");
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = null;
		this.artists = new HashSet<Artist>();
	}
	
	public Festival() {}
	
	/**
	 * Returns festival id.
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * changes the festival name
	 *
	 * @param name must not be {@literal null}
	 */
	public void setName(String name) {
		Assert.notNull(name, "Name must not be null!");
		this.name = name;
	}
	
	/**
	 * Returns festival name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns festival start date.
	 * 
	 * @return startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	} 

	/**
	 * Returns festival end date.
	 * 
	 * @return endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}
	
	/**
	 * Sets a {@link Location} for this festival
	 *
	 * @param location (could be {@literal null} if no location is 
	 */
	public void setLocation(Location location) {
		this.location=location;
	}
	
	/**
	 * Returns festival location.
	 * 
	 * @return location or null
	 */
	public Location getLocation() {
		return location; 
	}
	
	/**
	 * Returns all {@link Artist}s currently booked for the festival.
	 *
	 * @return all {@link Artist} entities.
	 */
	public Iterable<Artist> getArtist(){
		return this.artists;
	}
	
	/**
	 * inserts a new {@link Artist} 
	 * in the list of artists currently booked for the festival
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
	 * returns true, if the given {@link Artist} is booked for this festival.
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
	 * Unbook all {@link Artist}s from the festival.
	 * 
	 */
	public void deleteAllArtists() {
		for(Schedule aSchedule : schedules) {
			aSchedule.setShow(null);
		}
		this.artists = new HashSet<>();
	}
	
	/**
	 * returns all {@link Show}s from all currently booked {@link Artist}s.
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
	
	/**
	 * returns {@link Show} with the given id.
	 *
	 * @param showId
	 * @return {@link Show} with given id
	 */
	public Show getShow(long showId){
		for(Artist anArtist : artists) {
			if(anArtist.getShow(showId) != null) {
				return anArtist.getShow(showId);
			}
		}
		return null;
	}
	
	/**
	 * Sets an {@link Equipment} with amount, or only the amount if the 
	 * Equipment already exists.
	 *
	 * @param id
	 * @param amount
	 */
	public void setEquipments(SalespointIdentifier id, long amount) {
		rentedEquipments.put(id, amount);
	}
	
	/**
	 * Returns map of {@link Equipment}-ids with their amounts.
	 *
	 * @return map with ids and amounts
	 */
	public Map<SalespointIdentifier, Long> getEquipments(){
		return rentedEquipments;
	}
	
	/**
	 * Returns list of {@link Person}s that are already setted for a stage
	 * at the given date and time slot, except the stage with the given stage id.
	 *
	 * @param date
	 * @param timeSlot
	 * @param stageId
	 * @return the list of available {@link Person}s
	 */	
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
	 * @param timeSlot
	 * @param show
	 * @param stage
	 * @param date
	 * @param security
	 * @return true if the add or override was successfull
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
	
	/**
	 * Returns {@link Show} name from the Schedule with the given parameters.
	 * If the Schedule does not exist or has no show returns "Keine"
	 * 
	 * @param timeSlot
	 * @param stage
	 * @param date
	 * @return name of the {@link Show} or "Keine"
	 */
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
	
	/**
	 * Returns security {@link Person} name from the Schedule with the given parameters.
	 * If the Schedule does not exist or has no security returns "Keine".
	 * 
	 * @param timeSlot
	 * @param stage
	 * @param date
	 * @return name of the security {@link Person} or "Keine"
	 */
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
	
	/**
	 * Removes {@link Scheudle} instance with given id from festival.
	 * 
	 * @param timeSlot
	 * @param stage
	 * @param date
	 * @return true if the deletion was successfull
	 */
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
	
	/**
	 * Removes all {@link Scheudle} instances that contains the
	 * security {@link Person} with the given id
	 * 
	 * @param securityId
	 */
	public void removeSecurity(long securityId) {
		for(Schedule aSchedule : schedules) {
			if(aSchedule.getSecurity().getId() == securityId) {
				aSchedule.setSecurity(null);
			}
		}
	}
	
	/**
	 * Returns all {@link Stage}s rented for the festival.
	 *
	 * @return all {@link Stage} entities in stages
	 */
	public List<Stage> getStages(){
		return stages;
	}
	
	/**
	 * Inserts a new {@link Stage} 
	 * in the list of stages currently rented for the festival
	 *
	 * @param stage must not be {@literal null}
	 * @return true if the adding was successfull
	 */
	public boolean addStage(Stage stage) {
		Assert.notNull(stage, "Stage must not be null!");
		return stages.add(stage);
	}
	
	/**
	 * Removes {@link Stage} instance from festival.
	 * And removes all {@link Schedule}s at this {@link Stage}
	 * 
	 * @param stage must not be {@literal null}
	 * @return true if the deletion was successfull
	 */
	public boolean removeStage(Stage stage) {
		Assert.notNull(stage, "Stage must not be null!");
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
	
	/**
	 * Returns required number of security {@link Person}s for the 
	 * current max. number of visitors allowed for the festivals {@link Location}.
	 * 
	 * @return number of required securitys
	 */
	public long getRequiredSecurityCount() {
		if (location != null) {
			long visitorCapacity = location.getVisitorCapacity();
			return visitorCapacity / 100;
		}
		return 0;
	}

	public boolean isAnnounced() {
		return this.announced;
	}

	public void setAnnounced(boolean announced) {
		this.announced = announced;
	}
}
