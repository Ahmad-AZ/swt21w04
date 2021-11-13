package prototype.festival;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface FestivalRepository extends CrudRepository<Festival, Long> {
	
	@Override
	Streamable<Festival> findAll();

}
