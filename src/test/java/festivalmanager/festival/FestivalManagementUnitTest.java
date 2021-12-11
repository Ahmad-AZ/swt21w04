//package festivalmanager.festival;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.salespointframework.core.Currencies.EURO;
//
//import java.util.Optional;
//
//import org.javamoney.moneta.Money;
//import org.junit.jupiter.api.Test;
//
//import festivalmanager.location.Location;
//import festivalmanager.location.LocationManagement;
//
//
//public class FestivalManagementUnitTest {
//	FestivalManagement festivalManagement = mock(FestivalManagement.class);
//	
//	@Test 
//	void createNewFestival() {
//		Festival festival = mock(Location.class);
//		when(lm.createLocation(any())).thenReturn(location);
//		
//		Long vc = (long) 1500;
//		Long sc = (long) 6;
//		Double ppD = (double) 1550.90;
//		NewLocationForm form = new NewLocationForm("name", "adress",  ppD, vc, sc, null, null);
//		Location savedLocation = lm.createLocation(form);
//		
//		verify(lm, times(1)).createLocation(eq(form));
//		
//		Optional<Location> locationOP = lm.findById(savedLocation.getId());
//		if (locationOP.isPresent()) {
//			Location current = locationOP.get();
//			
//			assertThat(current.getName()).isEqualTo("name");
//			assertThat(current.getAdress()).isEqualTo("adress");
//			assertThat(current.getPricePerDay()).isEqualTo(Money.of(1550.90, EURO));
//			assertThat(current.getVisitorCapacity()).isEqualByComparingTo((long) 1500);
//			assertThat(current.getStageCapacity()).isEqualByComparingTo((long) 6);
//			assertThat(current.getImage()).isEqualTo("Blank_image");
//			assertThat(current.getGroundView()).isEqualTo("Blank_groundview");
//		}
//	}
//}
