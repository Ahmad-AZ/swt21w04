package festivalmanager.planning;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.HiringManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlanOffersManagement {

	private Streamable<Artist> artistList;
	private final HiringManagement hiringManagement;
	private final FestivalManagement festivalManagement;

	public PlanOffersManagement(HiringManagement hiringManagement, FestivalManagement festivalManagement) {
		this.hiringManagement = hiringManagement;
		this.festivalManagement = festivalManagement;
		artistList = hiringManagement.findAll();
	}

	public boolean bookArtist(Artist artist, Festival festival){

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
