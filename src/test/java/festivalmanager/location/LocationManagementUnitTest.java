package festivalmanager.location;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.salespointframework.core.Currencies.EURO;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;

public class LocationManagementUnitTest {
	
	LocationManagement lm = mock(LocationManagement.class);
	
	@Test 
	void createNewLocation() {
		
		Location location = mock(Location.class);
		when(lm.createLocation(any())).thenReturn(location);
		
		Long vc = (long) 1500;
		Long sc = (long) 6;
		Double ppD = (double) 1550.90;
		NewLocationForm form = new NewLocationForm("name", "adress",  ppD, vc, sc, null, null);
		Location savedLocation = lm.createLocation(form);
		
		verify(lm, times(1)).createLocation(eq(form));
		
		Optional<Location> locationOP = lm.findById(savedLocation.getId());
		if (locationOP.isPresent()) {
			Location current = locationOP.get();
			
			assertThat(current.getName()).isEqualTo("name");
			assertThat(current.getAdress()).isEqualTo("adress");
			assertThat(current.getPricePerDay()).isEqualTo(Money.of(1550.90, EURO));
			assertThat(current.getVisitorCapacity()).isEqualByComparingTo((long) 1500);
			assertThat(current.getStageCapacity()).isEqualByComparingTo((long) 6);
			assertThat(current.getImage()).isEqualTo("Blank_image");
			assertThat(current.getGroundView()).isEqualTo("Blank_groundview");
		}
	}
	
}
