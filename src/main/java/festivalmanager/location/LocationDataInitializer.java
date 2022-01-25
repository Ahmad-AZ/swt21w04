package festivalmanager.location;
import static org.salespointframework.core.Currencies.EURO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import festivalmanager.Application;
import festivalmanager.festival.FestivalInitializer;


/**
* Initializes default {@link Location}s
*
* @author Adrian Scholze
*/
@Component
@Order(20)
public class LocationDataInitializer implements DataInitializer{
	private static final Logger LOG = LoggerFactory.getLogger(FestivalInitializer.class);

	private LocationRepository locations;
	private final String uploadDir = Paths.get(Application.UPLOAD_DIR).toAbsolutePath().toString()+ "/";
	
	/**
	 * Create a new {@link LocationDataInitializer}
	 * @param locations
	 */
	public LocationDataInitializer(LocationRepository locations) {
		this.locations = locations; 
		
	}
	
	/**
	 * Initialize Example {@link Locations}s 
	 * 
	 */  
	@Override 
	public void initialize() {

		if (locations.findAll().iterator().hasNext()) {
			return; 
		}
		LOG.info("Creating default location entries.");
		// create Folder for locationImages
		new File(uploadDir).mkdir();
		String srcString = "src/main/resources/static/resources/img/location/";
		
		String[] filenames = {"Kulturpalast_image.jpg", "Kulturpalast_groundview.jpg", 
								"HSZ_image.jpg","HSZ_groundview.jpg", "Rundkino_image.jpg", 
								"Rundkino_groundview.jpg", "OpenAir_image.jpg", "OpenAir1_groundView.jpg",
								"OpenAir2_groundView.jpg", "OpenAir3_groundView.jpg", "Blank_groundview.jpg",
								"Blank_image.jpg"};
		for(int i = 0; i<filenames.length; i++) {
			Path srcPath = Paths.get(srcString + filenames[i]).toAbsolutePath();		
			try {
			       Path path = Paths.get(uploadDir + filenames[i]);      
			       Files.copy(srcPath, path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
			       e.printStackTrace();
			}
		}
	
		Location l1 = new Location("Kulturpalast", "Dresden", Money.of(1550, EURO), 1500, 12,
									"Kulturpalast_image.jpg", "Kulturpalast_groundview.jpg");
		l1.addBooking(LocalDate.of(2021, 11, 23), LocalDate.of(2021, 12, 23));
		locations.save(l1);
		Location l2 = new Location("Hörsaalzentrum", "Dresden", Money.of(1828, EURO), 1828, 6,
									"HSZ_image.jpg", "HSZ_groundview.jpg");
		locations.save(l2);
		Location l3 = new Location("Rundkino", "Dresden", Money.of(1760, EURO), 1500, 8,
									"Rundkino_image.jpg", "Rundkino_groundview.jpg");
		locations.save(l3);
		Location l4 = new Location("OpenAir 1", "Hamburg", Money.of(2000, EURO), 10000, 3,
									"OpenAir_image.jpg", "OpenAir1_groundView.jpg");
		locations.save(l4);
		Location l5 = new Location("OpenAir 2", "München", Money.of(1350, EURO), 7500, 1,
									"OpenAir_image.jpg", "OpenAir2_groundView.jpg");
		locations.save(l5);
		Location l6 = new Location("OpenAir 3", "Dresden", Money.of(1670, EURO), 8600, 2,
									"OpenAir_image.jpg", "OpenAir3_groundView.jpg");
		locations.save(l6);

	}

}
