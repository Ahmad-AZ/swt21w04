package festivalmanager.hiring;


import com.mysema.commons.lang.Assert;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
		if (this.findByName(form.getName()) == null){
			return artists.save(new Artist(form.getName()));
		}
		return null;
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

	public Artist findByName(String name){
		List<Artist> artistList = (List<Artist>) artists.findAll();
		for (Artist artist: artistList) {
			if (artist.getName() == name){
				return artist;
			}
		}
		return null;
	}
}
