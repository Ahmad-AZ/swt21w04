package festivalmanager.festival;


import javax.persistence.*;

import festivalmanager.Equipment.Equipments;
import festivalmanager.hiring.Artist;
import festivalmanager.location.Location;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	
	@OneToMany
	private List<Equipments> rentedEquipments = new ArrayList<>(); 
	

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

	public String getName() {
		return name;
	}
	
	public Location getLocation() {
		return location; 
	}
	public Set<Artist> getArtist(){
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

	public boolean setEquipments(Equipments equipments) {
		if(rentedEquipments.contains(equipments)) {
			int index = rentedEquipments.indexOf(equipments);
			if(rentedEquipments.get(index).getId() != equipments.getId()) {
				System.out.println("equipment ids does not match ");
			}
			rentedEquipments.set(index, equipments);
			return true;
		}
		else {
			return rentedEquipments.add(equipments);
		}		
	}
	
	public Iterable<Equipments> getEquipments(){
		return rentedEquipments;
	}


}
