package festivalmanager.hiring;


import com.mysema.commons.lang.Assert;
import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.Optional;

import static org.salespointframework.core.Currencies.EURO;

/**
 * Implementation of business logic related to {@link Artist}
 *
 * @author Tuan Giang Trinh
 */
@Service
@Transactional
public class HiringManagement {
	private final ArtistRepository artists;

	/**
	 * Create a new {@link HiringManagement} with the given {@link ArtistRepository}
	 * @param artists must not be {@literal null}
	 */
	public HiringManagement(ArtistRepository artists) {
		Assert.notNull(artists, "ArtistRepository must not be null");
		this.artists = artists;

	}

	/**
	 * create a new {@link Artist} using information given in the {@link NewArtistForm}
	 * @param form must not be{@literal null}
 	 * @return the new {@link Artist} instance
	 */
	public Artist createAritst(NewArtistForm form){
		Assert.notNull(form, "form must not be null");
		Money price = Money.of(form.getPrice(), EURO);

		return artists.save(new Artist(form.getName(), price, form.getStageTechnician()));
	}
	/**
	 * create a new {@link Show} of the {@link Artist} using information given in the {@link NewShowForm}
	 * @param form must not be{@literal null}
	 * @param artist
	 * @return the new {@link Show} instance
	 */
	public void createShow(NewShowForm form, Artist artist) {
		artist.addShow(new Show(form.getName(), Duration.ofMinutes(form.getPerformance())));
	}
	/**
	 * Returns all{@link Artist}s currently available in the system
	 * @return all {@link Artist} entities
	 */
	public Streamable<Artist> findAll(){
		return artists.findAll();
	}
	/**
	 * Returns {@link Artist} currently available in the system with the given id
	 * @param id
	 * @return {@link Artist} entities
	 */
	public Optional<Artist> findById(Long id){
		return artists.findById(id);
	}

	/**
	 * remove an {@link Artist} currently available in the system with the given id
	 * @param artistId
	 */
	public void removeArtist(Long artistId) {
		artists.deleteById(artistId);
	}
	/**
	 * edit the attribute of an {@link Artist}
	 * @param artist
	 * @param form must not be {@literal null}.
	 */

	public Artist editArtist(Artist artist, NewArtistForm form) {
		artist.setName(form.getName());
		artist.setPrice(Money.of(form.getPrice(), EURO));
		artist.setStageTechnician(form.getStageTechnician());

		return artists.save(artist);
	}

	/**
	 * save the {@link Artist} to the repository
	 * @param artist
	 * @return true with it is saved successfully
	 */
	public Artist saveArtist(Artist artist) {
		return artists.save(artist);
	}

}
