package festivalmanager.hiring;

import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class ShowDataInitializer implements DataInitializer {

	private ShowRepository showRepository;

	public ShowDataInitializer(ShowRepository showRepository){
		this.showRepository = showRepository;
		Show criminal = new Show("Smooth criminal");
		Show bad = new Show("bad");
		Show senorita = new Show("senorita");
		Show treat = new Show("treat you better");
		Show havana = new Show("havana");
		Show never = new Show("never be the same");
		showRepository.save(criminal);
		showRepository.save(bad);
		showRepository.save(senorita);
		showRepository.save(treat);
		showRepository.save(havana);
		showRepository.save(never);
	}
	@Override
	public void initialize() {

	}
}
