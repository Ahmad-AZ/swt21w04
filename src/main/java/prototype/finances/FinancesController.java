package prototype.finances;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import prototype.festival.Festival;


@Controller
class FinancesController {

	@GetMapping("/finances")
	// TODO: @PreAuthorize("hasRole('PLANNER')")
	String finances(Model model,
					@ModelAttribute("currentFestival") Festival currentFestival) {

		String name = currentFestival.getName();
		System.out.println(name);
		System.out.println(currentFestival.getId());
		return "finances";
	}
}