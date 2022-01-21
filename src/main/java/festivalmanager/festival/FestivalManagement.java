package festivalmanager.festival;

import java.util.Optional;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import festivalmanager.hiring.Artist;
import festivalmanager.hiring.HiringManagement;
import festivalmanager.location.LocationManagement;


/**
 * Implementation of business logic related to {@link Festival}s.
 *
 * @author Adrian Scholze
 */
@Service
@Transactional
public class FestivalManagement {
	private final FestivalRepository festivals;
	private final LocationManagement locationManagement;
	private final HiringManagement hiringManagement;
	
	/**
	 * Creates a new {@link FestivalManagement} with the given {@link FestivalRepository}, {@link HiringManagement} and
	 * {@link LocationManagement}.
	 *
	 * @param festivals must not be {@literal null}.
	 * @param locationManagement must not be {@literal null}.
	 * @param hiringManagement must not be {@literal null}.
	 */
	public FestivalManagement(FestivalRepository festivals,
							  LocationManagement locationManagement,
							  HiringManagement hiringManagement) {
		Assert.notNull(festivals, "FestivalRepository must not be null!");
		Assert.notNull(locationManagement, "LocationManagement must not be null!");
		Assert.notNull(hiringManagement, "HiringManagement must not be null!");
		this.festivals = festivals;
		this.locationManagement = locationManagement;
		this.hiringManagement = hiringManagement;
	}
	
	/**
	 * Creates a new {@link Festival} using the information given in the {@link NewFestivalForm}.
	 *
	 * @param form must not be {@literal null}.
	 * @return the new {@link Festival} instance.
	 */
	public Festival createFestival(NewFestivalForm form) {
		Assert.notNull(form, "NewFestivalForm must not be null!");
		return festivals.save(new Festival(form.getName(), form.getStartDate(), form.getEndDate()));
	}
	
	/**
	 * Saves {@link Festival} instance
	 * 
	 * @param festival must not be {@literal null}.
	 * @return the saved {@link Festival} instance.
	 */
	public Festival saveFestival(Festival festival) {
		Assert.notNull(festival, "Festival must not be null!");
		return festivals.save(festival);
	}
	
	/**
	 * Removes {@link Festival} instance with given id 
	 * 
	 * @param festival must not be {@literal null}
	 */
	public void deleteFestival(Festival festival) {
		Assert.notNull(festival, "Festival must not be null!");
		// delete Location booking
		if(festival.getLocation() != null) {
			festival.getLocation().removeBooking(festival.getStartDate(), festival.getEndDate());
			locationManagement.saveLocation(festival.getLocation());
		}

		if(!festival.artistsIsEmpty()) {
			for(Artist anArtist : festival.getArtist()) {
				anArtist.removeBooking(festival.getStartDate(), festival.getEndDate());
				hiringManagement.saveArtist(anArtist);
			}
			// required, else all artists where deleted from repo
			festival.deleteAllArtists();
		}
		festivals.delete(festival);
	}
	
	/**
	 * Returns all {@link Festival}s currently available in the system.
	 *
	 * @return all {@link Festival} entities.
	 */
	public Streamable<Festival> findAll() {
		return festivals.findAll();
	}

	/**
	 * Returns {@link Festival} with given id if it exists
	 * 
	 * @param id
	 * @return the {@link Festival} with the given id or {@literal Optional#empty()} if none found.
	 */
	public Optional<Festival> findById(Long id) {
		return festivals.findById(id);
	}
}
