package prototype.finances;

import org.javamoney.moneta.Money;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import prototype.festival.Festival;

import static org.salespointframework.core.Currencies.EURO;


@Controller
class FinancesController {

	private Finances finances;

	FinancesController() {
		this.finances = new Finances();
	}

	@GetMapping("/finances")
	// TODO: @PreAuthorize("hasRole('PLANNER')")
	String finances(Model model,
					@ModelAttribute("currentFestival") Festival currentFestival) {

		Money cost = finances.getCost(currentFestival);
		Money revenue = finances.getRevenue(currentFestival);
		Money profit = Money.of(0, EURO);
		profit = profit.subtract(cost);
		profit = profit.add(revenue);

		model.addAttribute("cost", cost.getNumber().doubleValue());
		model.addAttribute("revenue", revenue.getNumber().doubleValue());
		model.addAttribute("profit", profit.getNumber().doubleValue());

		return "finances";
	}

	//@PostMapping("/finances")
	//ticketPriceForm(Model model @RequestParam("")) {
		//model.addAttribute();
	//}

}