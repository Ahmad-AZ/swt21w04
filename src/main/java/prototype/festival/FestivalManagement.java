package prototype.festival;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class FestivalManagement {
	private final FestivalRepository festivals;
	
	FestivalManagement(FestivalRepository festival) {
		this.festivals = festival;
	}
	
	public Festival createFestival(NewFestivalForm form) {
		// save Festival in Repository
		return festivals.save(new Festival(form.getName(), form.getStartDate(), form.getEndDate()));
	}
	
	public Festival saveFestival(Festival festival) {
		return festivals.save(festival);
	}
	
	public Streamable<Festival> findAll() {
		return festivals.findAll();
	}

	public Optional<Festival> findById(Long id) {
		return festivals.findById(id);
	}
}
