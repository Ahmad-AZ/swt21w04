package festivalmanager.location;
import static org.salespointframework.core.Currencies.EURO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;



@Component
@Order(20)
public class LocationDataInitializer implements DataInitializer{
	
	private LocationRepository locations;
	private final String UPLOAD_DIR = Paths.get("locationImages").toAbsolutePath().toString()+ "\\";
	
	public LocationDataInitializer(LocationRepository locations) {
		
		// create Folder for locationImages
		new File(UPLOAD_DIR).mkdir();
		
		Path srcPath = Paths.get("src\\main\\resources\\static\\resources\\img\\location\\Kulturpalast_image.jpg").toAbsolutePath();
		System.out.println(srcPath);
		
		
		try {
		       Path path = Paths.get(UPLOAD_DIR + "Kulturpalast_image.jpg");
		       System.out.println(path);
		       Files.copy(srcPath, path, StandardCopyOption.REPLACE_EXISTING);
		   } catch (IOException e) {
		       e.printStackTrace();
		   }
		
		this.locations = locations;
		Location l1 = new Location("Kulturpalast", "Dresden", Money.of(1550, EURO), 1500, 12, "Kulturpalast_image", "Kulturpalast_groundview");
		l1.addBooking(LocalDate.of(2021, 11, 23), LocalDate.of(2021, 12, 23));
		locations.save(l1);
		Location l2 = new Location("Hörsaalzentrum", "Dresden", Money.of(1828, EURO), 1828, 6, "HSZ_image", "HSZ_groundview");
		locations.save(l2);
		Location l3 = new Location("Rundkino", "Dresden", Money.of(1760, EURO), 1500, 8, "Rundkino_image", "Rundkino_groundview");
		locations.save(l3);
		Location l4 = new Location("OpenAir 1", "Hamburg", Money.of(2000, EURO), 10000, 3, "OpenAir_image", "OpenAir1_groundView");
		locations.save(l4);
		Location l5 = new Location("OpenAir 2", "München", Money.of(1350, EURO), 7500, 1, "OpenAir_image", "OpenAir2_groundView");
		locations.save(l5);
		Location l6 = new Location("OpenAir 3", "Dresden", Money.of(1670, EURO), 8600, 2, "OpenAir_image", "OpenAir3_groundView");
		locations.save(l6);
		
		
		
		
	}
	
	@Override 
	public void initialize() {

//		if (LocationManagement.findAll().iterator().hasNext()) {
//			return; 
//		}

		//LOG.info("Creating default catalog entries.");

		
	}

}
