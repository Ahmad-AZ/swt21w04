package prototype.location;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface LocationRepository extends CrudRepository<Location, Long> {
	@Override
	Streamable <Location> findAll();
}
