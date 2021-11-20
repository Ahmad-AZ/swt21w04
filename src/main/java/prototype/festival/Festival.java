package prototype.festival;


import javax.persistence.*;

import prototype.hiring.Artist;
import prototype.location.Location;
import prototype.planning.Planning;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class Festival {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private Date startDate;
	private Date endDate;
	@OneToMany
	private Set<Artist> artists;
	
	@OneToOne()
	private Location location;
	
//	@OneToOne()
//	private Finances finances; 
	

	public Festival(String name, Date startDate, Date endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = null;
	}

	public Festival(String name) {
		this.name = name;
		this.location = null;
	}
	
	public Festival() {
		
	}
	
	public long getId() {
		return id;
	}


	public Date getStartDate() {
		return startDate;
	} 

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
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
