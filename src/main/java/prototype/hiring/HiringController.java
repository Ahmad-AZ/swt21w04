package prototype.hiring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import prototype.festival.Festival;
import prototype.festival.FestivalManagement;

@Controller
public class HiringController {
//	private final OffersArtists offersArtists;
	private final ArtistRepository artistRepository;
	private Festival currentFestival;
	private FestivalManagement festivalManagement;

	public HiringController(
//			OffersArtists offersArtists,
			ArtistRepository artistRepository) {
//		this.offersArtists = offersArtists;
		this.artistRepository = artistRepository;
		this.currentFestival = null;
	}

	@GetMapping("/artists")
	public String artists(Model model) {
		model.addAttribute("artistsList", artistRepository);
		return "artists";
	}
}
