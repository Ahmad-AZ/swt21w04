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

	public boolean bookArtist(Artist artist, Festival festival) {

		return false;
	}

	protected Streamable<Artist> getArtistList() {
		return artistList;
	}

	protected HiringManagement getHiringManagement() {
		return hiringManagement;
	}

	protected FestivalManagement getFestivalManagement() {
		return festivalManagement;
	}

}