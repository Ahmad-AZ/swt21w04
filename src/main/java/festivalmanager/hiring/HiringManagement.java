package festivalmanager.hiring;


import com.mysema.commons.lang.Assert;
import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.salespointframework.core.Currencies.EURO;

@Service
@Transactional
public class HiringManagement {
	private final ArtistRepository artists;

	public HiringManagement(ArtistRepository artists) {
		Assert.notNull(artists, "ArtistRepository must not be null");
		this.artists = artists;

	}

	public Artist createAritst(NewArtistForm form){
		Assert.notNull(form, "form must not be null");
		Money price = Money.of(form.getPrice(), EURO);

		return artists.save(new Artist(form.getName(), price, form.getStageTechnician()));
	}

	public void createShow(NewShowForm form, Artist artist) {
		artist.addShow(new Show(form.getName()));
	}

	public Streamable<Artist> findAll(){
		return artists.findAll();
	}

	public Optional<Artist> findById(Long id){
		return artists.findById(id);
	}

	public void removeArtist(Long artistId) {
		artists.deleteById(artistId);
	}

	public Artist editArtist(Artist artist, NewArtistForm form) {
		artist.setName(form.getName());
		artist.setPrice(Money.of(form.getPrice(), EURO));
		artist.setStageTechnician(form.getStageTechnician());

		return artists.save(artist);
	}

	public Artist saveArtist(Artist artist) {
		return artists.save(artist);
	}

}
