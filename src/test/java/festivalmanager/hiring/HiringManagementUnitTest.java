package festivalmanager.hiring;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.salespointframework.core.Currencies.EURO;

public class HiringManagementUnitTest {

	HiringManagement lm = mock(HiringManagement.class);
	final ArtistRepository artists = mock(ArtistRepository.class);
	Artist artist = mock(Artist.class);

	@Test
	void createnewArtist() {
		when(lm.createAritst(any())).thenReturn(artist);

		Double price = (double) 100.00;
		Integer stageTechnician = (int)5;
		NewArtistForm form = new NewArtistForm("name", price, stageTechnician);
		Artist savedArtist = lm.createAritst(form);

		verify(lm, times(1)).createAritst(eq(form));
		Optional<Artist> artistOP = lm.findById(savedArtist.getId());
		if (artistOP.isPresent()) {
			Artist current = artistOP.get();
			assertThat(current.getName()).isEqualTo("name");
			assertThat(current.getPrice()).isEqualTo(Money.of(100.00, EURO));
			assertThat(current.getStageTechnician()).isEqualTo(5);
		}
	}

	@Test
	void editArtist() {
		Double price = (double) 100.00;
		Integer stageTechnician = (int)5;
		NewArtistForm form1 = new NewArtistForm("name", price, stageTechnician);
		Artist savedArtist = lm.createAritst(form1);

		NewArtistForm form2 = new NewArtistForm("newName", 200.00, 6);
		when(lm.editArtist(savedArtist, form2)).thenReturn(artist);

//		verify(lm, times(1)).editArtist(eq(savedArtist), eq(form2));
		Optional<Artist> artistOptional = lm.findById(artist.getId());
		if (artistOptional.isPresent()) {
			Artist current = artistOptional.get();
			assertThat(current.getName()).isEqualTo("newName");
			assertThat(current.getPrice()).isEqualTo(Money.of(200.00, EURO));
			assertThat(current.getStageTechnician()).isEqualTo(6);
		}
	}

	@Test
	void saveArtist() {
		lm.saveArtist(artist);
		Optional<Artist> savedArtist = artists.findById(artist.getId());
		if (savedArtist.isPresent()) {
			Artist current = savedArtist.get();
			assertThat(current).isEqualTo(artist);
		}
	}

	@Test
	void removeArtist() {
		lm.saveArtist(artist);
		lm.removeArtist(artist.getId());
		Optional<Artist> artistOptional = artists.findById(artist.getId());
		assertThat(artistOptional).isEmpty();
	}

}
