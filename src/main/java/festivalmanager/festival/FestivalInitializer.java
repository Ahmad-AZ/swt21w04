package festivalmanager.festival;


import java.time.LocalDate;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(10)        
public class FestivalInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(FestivalInitializer.class);
	
	private FestivalRepository festivals;
	
	public FestivalInitializer(FestivalRepository festivals) {
		this.festivals = festivals;
		Festival f1 = new Festival("Beispielfestival", LocalDate.of(2021, 12, 6), LocalDate.of(2021, 12, 11));
		Festival f2 = new Festival("adminfestival", LocalDate.of(2022, 2, 6), LocalDate.of(2022, 2, 9));

		System.out.println("FestivalId: "+ f1.getId());
		festivals.save(f1);
		festivals.save(f2);
	}

	  
	@Override
	public void initialize() {  
//		LOG.info("Creating default Festivals");
//		System.out.println("creating festivalsssssssssss");
//		festivalManagement.createFestival(new NewFestivalForm("firstFestival"));  
	}

}
