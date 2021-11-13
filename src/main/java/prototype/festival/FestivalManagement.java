package prototype.festival;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class FestivalManagement {
	private final FestivalRepository festivals;
	
	FestivalManagement(FestivalRepository festival) {
		this.festivals = festival;
	}
	
	public Festival createFestival(NewFestivalForm form) {
		// save Festival in Repository
		return festivals.save(new Festival(form.getName()));
	}
	
	public Festival saveFestival(Festival festival) {
		return festivals.save(festival);
	}
	
	public Streamable<Festival> findAll() {
		return festivals.findAll();
	}

}
