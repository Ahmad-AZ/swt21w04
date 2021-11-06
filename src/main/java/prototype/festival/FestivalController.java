package prototype.festival;

import org.springframework.stereotype.Controller;

@Controller
public class FestivalController {

	private final FestivalManagement festivalManagement;

	public FestivalController(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
	}
}
