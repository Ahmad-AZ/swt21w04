package prototype.hiring;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import prototype.hiring.Artist;

import java.util.Optional;

public interface ArtistRepository extends CrudRepository<Artist, Long> {
	@Override
	Streamable<Artist> findAll();

	public Optional<Artist> findById(Long id);
}
