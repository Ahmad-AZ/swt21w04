package festivalmanager.hiring;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

/**
 * A repository interface to manage {@link Artist} instances
 *
 * @author Tuan Giang Trinh
 */
public interface ArtistRepository extends CrudRepository<Artist, Long> {

	/**
	 * Re-declared {@link CrudRepository#findAll()} to return a {@link Streamable} instead of {@link Integer}
	 * @return
	 */
	@Override
	Streamable<Artist> findAll();

	/**
	 *
	 * @param id
	 * @return the {@link Artist} based the given id
	 */
	public Optional<Artist> findById(Long id);
}
