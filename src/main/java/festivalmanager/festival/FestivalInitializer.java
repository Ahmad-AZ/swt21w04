package festivalmanager.festival;


import java.time.LocalDate;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
* Initializes default {@link Festival}s
*
* @author Adrian Scholze
*/
@Component
@Order(10)        
public class FestivalInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(FestivalInitializer.class);
	
	private FestivalRepository festivals;
	
	/**
	 * Create a new {@link FestivalInitializers}
	 * @param festivalRepository
	 */
	public FestivalInitializer(FestivalRepository festivals) {
		this.festivals = festivals;
	}
	
	/**
	 * Initialize Example {@link Festival}s 
	 * 
	 */  
	@Override
	public void initialize() {  
		if(festivals.findAll().iterator().hasNext()) {
			return;
		}
		LOG.info("Creating default festival entries.");
		Festival f1 = new Festival("Beispielfestival", LocalDate.of(2021, 12, 6), LocalDate.of(2021, 12, 11));
		Festival f2 = new Festival("adminfestival", LocalDate.of(2022, 2, 6), LocalDate.of(2022, 2, 9));
		festivals.save(f1);
		festivals.save(f2);
	}

}
