package prototype.hiring;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface OffersArtistRepository extends CrudRepository<OfferArtist, Long> {
	@Override
	Streamable<OfferArtist> findAll();

}
