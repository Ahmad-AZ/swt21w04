package prototype.festival;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FestivalController {

	private final FestivalManagement festivalManagement;

	public FestivalController(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
	}
	
	@GetMapping("/festival")
	public String festival(Model model) {

		model.addAttribute("festivals", festivalManagement.findAll());

		return "festival";
	}
}
