package prototype.finances;

import org.javamoney.moneta.Money;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import prototype.festival.Festival;

import static org.salespointframework.core.Currencies.EURO;


@Controller
@SessionScope
class FinancesController {


	private Finances finances;
	private Festival currentFestival;

	private long nCampingTickets;
	private long nOneDayTickets;
	private long soldCampingTickets;
	private long soldOneDayTickets;

	// Ticket prices will be stored permanently in the Ticket Class in the real application,
	// but the Ticket class is not a part of this prototype
	private Money priceCampingTickets;
	private Money priceOneDayTickets;


	FinancesController() {
		this.finances = new Finances();
		this.currentFestival = null;
		resetAttributes();
	}


	private void resetAttributes() {
		nCampingTickets = 0;
		nOneDayTickets = 0;
		soldCampingTickets = 0;
		soldOneDayTickets = 0;
		priceCampingTickets = Money.of(0, EURO);
		priceOneDayTickets = Money.of(0, EURO);
	}


	@GetMapping("/finances")
	@Scope("session")
	// TODO: @PreAuthorize("hasRole('PLANNER')")
	String financesPage(Model model,
					@ModelAttribute("currentFestival") Festival currentFestival) {

		if(this.currentFestival != null &&
				this.currentFestival.getId() != currentFestival.getId()) {

			resetAttributes();
		}
		this.currentFestival = currentFestival;

		Money cost = finances.getCost(currentFestival);
		Money revenue = finances.getRevenue(currentFestival);
		Money profit = finances.getProfit(cost, revenue);

		String costStr = String.format("%.2f", cost.getNumber().doubleValue());
		String revenueStr = String.format("%.2f", revenue.getNumber().doubleValue());
		String profitStr = String.format("%.2f", profit.getNumber().doubleValue());
		String priceCampingTicketsStr = String.format("%.2f", priceCampingTickets.getNumber().doubleValue());
		String priceOneDayTicketsStr = String.format("%.2f", priceOneDayTickets.getNumber().doubleValue());

		model.addAttribute("cost", costStr);
		model.addAttribute("revenue", revenueStr);
		model.addAttribute("profit", profitStr);

		model.addAttribute("priceCampingTickets", priceCampingTicketsStr);
		model.addAttribute("priceOneDayTickets", priceOneDayTicketsStr);
		model.addAttribute("nCampingTickets", this.nCampingTickets);
		model.addAttribute("nOneDayTickets", this.nOneDayTickets);
		model.addAttribute("soldCampingTickets", this.soldCampingTickets);
		model.addAttribute("soldOneDayTickets", this.soldCampingTickets);

		return "finances";
	}


	@GetMapping("/setTicketNumber")
	@Scope("session")
	public String ticketNumberForm(Model model,
								  @RequestParam("nCampingTickets") long nCampingTickets,
								  @RequestParam("nOneDayTickets") long nOneDayTickets) {

		this.nCampingTickets = nCampingTickets;
		this.nOneDayTickets = nOneDayTickets;

		model.addAttribute("nCampingTickets", nCampingTickets);
		model.addAttribute("nOneDayTickets", nOneDayTickets);
		return financesPage(model, this.currentFestival);
	}


	@GetMapping("/setTicketPrice")
	@Scope("session")
	public String ticketPriceForm(Model model,
								  @RequestParam("priceCampingTickets") String priceCampingTicketsStr,
								  @RequestParam("priceOneDayTickets") String priceOneDayTicketsStr) {

		priceCampingTicketsStr = priceCampingTicketsStr.replace(",", ".");
		priceOneDayTicketsStr = priceOneDayTicketsStr.replace(",", ".");
		Double priceCampingTicketsDouble = Double.parseDouble(priceCampingTicketsStr);
		Double priceOneDayTicketsDouble = Double.parseDouble(priceOneDayTicketsStr);

		this.priceCampingTickets = Money.of(priceCampingTicketsDouble, EURO);
		this.priceOneDayTickets = Money.of(priceOneDayTicketsDouble, EURO);

		priceCampingTicketsStr = String.format("%.2f", priceCampingTickets.getNumber().doubleValue());
		priceOneDayTicketsStr = String.format("%.2f", priceOneDayTickets.getNumber().doubleValue());

		model.addAttribute("priceCampingTickets", priceCampingTicketsStr);
		model.addAttribute("priceOneDayTickets", priceOneDayTicketsStr);
		return financesPage(model, this.currentFestival);
	}


}