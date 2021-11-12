package prototype.festival;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(10)
public class FestivalInitializer implements DataInitializer {

	private static final Logger LOG = (Logger) LoggerFactory.getLogger(FestivalInitializer.class);
	
	private FestivalManagement festivalManagement;
	
	public FestivalInitializer(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
	}
	
	  
	@Override
	public void initialize() {
		LOG.info("Creating default Festivals");
		festivalManagement.createFestival(new NewFestivalForm("firstFestival"));  
	}

}
