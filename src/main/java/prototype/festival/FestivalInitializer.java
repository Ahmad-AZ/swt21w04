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
	private FestivalRepository festivals;
	
	public FestivalInitializer(FestivalManagement festivalManagement, FestivalRepository festivals) {
		this.festivalManagement = festivalManagement;
		this.festivals = festivals;
		Festival f1 = new Festival("firstFestival");
		festivals.save(f1);
	}

	  
	@Override
	public void initialize() {  
		LOG.info("Creating default Festivals");
		System.out.println("creating festivalsssssssssss");
		festivalManagement.createFestival(new NewFestivalForm("firstFestival"));  
	}

}
