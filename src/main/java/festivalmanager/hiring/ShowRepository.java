package festivalmanager.hiring;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

public interface ShowRepository extends CrudRepository<Show, Long> {
	@Override
	Streamable<Show> findAll();

	public Optional<Show> findById(Long id);
}
