package festivalmanager.festival;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import festivalmanager.hiring.Artist;
import festivalmanager.hiring.HiringManagement;
import festivalmanager.location.LocationManagement;

import java.util.Optional;

@Service
@Transactional
public class FestivalManagement {
	private final FestivalRepository festivals;
	private final LocationManagement LocationManagement;
	private final HiringManagement hiringManagement;
	
	public FestivalManagement(FestivalRepository festivals, LocationManagement LocationManagement, HiringManagement hiringManagement) {
		this.festivals = festivals;
		this.LocationManagement = LocationManagement;
		this.hiringManagement = hiringManagement;
	}
	
	public Festival createFestival(NewFestivalForm form) {
		// save Festival in Repository
		return festivals.save(new Festival(form.getName(), form.getStartDate(), form.getEndDate()));
	}
		
	public Festival saveFestival(Festival festival) {
		return festivals.save(festival);
	}
	
	public void deleteFestival(Festival festival) {
		// delete Location booking
		if(festival.getLocation() != null) {
			festival.getLocation().removeBooking(festival.getStartDate(), festival.getEndDate());
			LocationManagement.saveLocation(festival.getLocation());
		}
		if(!festival.artistsIsEmpty()) {
			for(Artist anArtist : festival.getArtist()) {
				anArtist.removeBooking(festival.getStartDate(), festival.getEndDate());
				hiringManagement.saveArtist(anArtist);
			}
		}
		festivals.delete(festival);
	}
	
	public Streamable<Festival> findAll() {
		return festivals.findAll();
	}

	public Optional<Festival> findById(Long id) {
		return festivals.findById(id);
	}
}
