package festivalmanager.catering;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author Robert Menzel
 */
@Controller
public class CateringController {

	@GetMapping("/catering")
	public String cateringPage(Model model) {
		return "catering";
	}

}
