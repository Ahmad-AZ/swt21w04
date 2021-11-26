package festivalmanager.hiring;


import com.mysema.commons.lang.Assert;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class HiringManagement {
	private final ArtistRepository artists;

	public HiringManagement(ArtistRepository artists) {
		Assert.notNull(artists, "ArtistRepository must not be null");
		this.artists = artists;
	}
	public Streamable<Artist> findAll(){
		return artists.findAll();
	}

	public Optional<Artist> findById(Long id){
		return artists.findById(id);
	}
}
