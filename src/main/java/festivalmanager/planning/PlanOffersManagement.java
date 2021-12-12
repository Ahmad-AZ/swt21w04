package festivalmanager.planning;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.HiringManagement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlanOffersManagement {

	private final HiringManagement hiringManagement;
	private final FestivalManagement festivalManagement;

	public PlanOffersManagement(HiringManagement hiringManagement, FestivalManagement festivalManagement) {
		this.hiringManagement = hiringManagement;
		this.festivalManagement = festivalManagement;
	}

	public boolean bookArtist(Artist artist, Festival festival) {
		artist.removeBooking(festival.getStartDate(), festival.getEndDate());
		boolean succes = artist.addBooking(festival.getStartDate(), festival.getEndDate());
		hiringManagement.saveArtist(artist);
		if(succes) {
			festival.addArtist(artist);
			festivalManagement.saveFestival(festival);
			return true;
		}
		return false;
	}

//	public void unbookArtist(Artist artist, Festival festival) {
//		festival.deleteAll();
//		festivalManagement.saveFestival(festival);
//		artist.removeBooking(festival.getStartDate(), festival.getEndDate());
//		hiringManagement.saveArtist(artist);
//	}

	public void unbookArtist(Artist artist, Festival festival) {
//		festival.deleteAll();

		festival.deleteAll();
		festivalManagement.saveFestival(festival);
		artist.removeBooking(festival.getStartDate(), festival.getEndDate());
		hiringManagement.saveArtist(artist);
	}
}
