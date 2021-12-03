package festivalmanager.festival;


import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(10)        
public class FestivalInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(FestivalInitializer.class);
	
	private FestivalManagement festivalManagement;
	private FestivalRepository festivals;
	
	public FestivalInitializer(FestivalManagement festivalManagement, FestivalRepository festivals) {
		this.festivalManagement = festivalManagement;
		this.festivals = festivals;
		Date startDate = new GregorianCalendar(2021, 11, 11).getTime();
		Date endDate = new GregorianCalendar(2021, 11, 14).getTime();
		Festival f1 = new Festival("Beispielfestival", LocalDate.of(2021, 11, 14), LocalDate.of(2021, 12, 11));
		System.out.println("FestivalId: "+ f1.getId());
		festivals.save(f1);
	}

	  
	@Override
	public void initialize() {  
//		LOG.info("Creating default Festivals");
//		System.out.println("creating festivalsssssssssss");
//		festivalManagement.createFestival(new NewFestivalForm("firstFestival"));  
	}

}
