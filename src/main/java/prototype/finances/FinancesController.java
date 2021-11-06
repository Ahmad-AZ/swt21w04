package prototype.finances;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
class FinancesController {

	@GetMapping("/finances")
	@PreAuthorize("hasRole('PLANNER')")
	String finances(Model model) {

		return "finances";
	}
}