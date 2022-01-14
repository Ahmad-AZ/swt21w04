package festivalmanager.planning;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.ArtistRepository;
import festivalmanager.hiring.HiringManagement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of business logic related to {@link Artist} and {@link Festival}
 *
 * @author Tuan Giang Trinh
 */
@Service
@Transactional
public class PlanOffersManagement {

	private final HiringManagement hiringManagement;
	private final FestivalManagement festivalManagement;

	/**
	 * Create a new {@link PlanOffersManagement}
	 * @param hiringManagement
	 * @param festivalManagement
	 */
	public PlanOffersManagement(HiringManagement hiringManagement, FestivalManagement festivalManagement) {
		this.hiringManagement = hiringManagement;
		this.festivalManagement = festivalManagement;
	}

	/**
	 * book the {@link Artist} for the {@link Festival}
	 * @param artist
	 * @param festival
	 * @return true if book artist success
	 */
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

	/**
	 * delete an already booked {@link Artist} of the {@link Festival}
	 * @param artist
	 * @param festival
	 * @return true if delete artist success
	 */
	public void unbookArtist(Artist artist, Festival festival) {

		festival.deleteAllArtists();
		festivalManagement.saveFestival(festival); 
		artist.removeBooking(festival.getStartDate(), festival.getEndDate());
		hiringManagement.saveArtist(artist);
	}
}
