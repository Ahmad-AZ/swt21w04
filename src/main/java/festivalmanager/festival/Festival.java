package festivalmanager.festival;


import javax.persistence.*;

import festivalmanager.hiring.Artist;
import festivalmanager.location.Location;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Festival {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	@OneToMany
	private Set<Artist> artists;
	
	@OneToOne()
	private Location location;
	
//	@OneToOne()
//	private Finances finances; 
	

	public Festival(String name, LocalDate startDate, LocalDate endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = null;
		this.artists = new HashSet<>();
	}

	public Festival(String name) {
		this.name = name;
		this.location = null;
		this.artists = new HashSet<>();
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

	public String getName() {
		return name;
	}
	
	public Location getLocation() {
		return location; 
	}
	
	public void setLocation(Location location) {
		this.location=location;
	}
	public boolean addArtist(Artist artist){
		if (artists.contains(artist)){
			return false;
		}
		artists.add(artist);
		return true;
	}
	
//	public boolean equals(Festival festival) {
//		return this.id == festival.getId();
//	}

}
