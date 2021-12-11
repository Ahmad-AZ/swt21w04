package festivalmanager.finances;


import org.javamoney.moneta.Money;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.utils.UtilsManagement;

import static org.salespointframework.core.Currencies.EURO;


@Controller
class FinancesController {


	private Finances finances;
	private long currentFestivalId;
	private Festival currentFestival;
	private FinancesManagement financesManagement;
	private FestivalManagement festivalManagement;
	private UtilsManagement utilsManagement;

	private long nCampingTickets;
	private long nOneDayTickets;
	private long soldCampingTickets;
	private long soldOneDayTickets;

	// Ticket prices will be stored permanently in the Ticket Class in the real application,
	// but the Ticket class is not a part of this prototype
	private Money priceCampingTickets;
	private Money priceOneDayTickets;


	FinancesController(FinancesManagement financesManagement,
					   FestivalManagement festivalManagement,
					   UtilsManagement utilsManagement) {
		this.financesManagement = financesManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
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
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
	String financesPage(Model model) {

		if(this.currentFestival != null &&
				this.currentFestival.getId() != utilsManagement.getCurrentFestivalId()) {

			resetAttributes();
		}

		this.currentFestivalId = utilsManagement.getCurrentFestivalId();
		this.financesManagement.updateFestival(currentFestivalId);
		this.currentFestival = festivalManagement.findById(currentFestivalId).get();

		Money artistsCost = financesManagement.getArtistsCost();
		Money locationCost = financesManagement.getLocationCost();
		Money cost = financesManagement.getCost();
		Money revenue = financesManagement.getRevenue(currentFestival,
				priceCampingTickets, priceOneDayTickets, nCampingTickets, nOneDayTickets);
		Money profit = financesManagement.getProfit(cost, revenue);

		String artistsCostStr = String.format("%.2f", artistsCost.getNumber().doubleValue());
		String locationCostStr = String.format("%.2f", locationCost.getNumber().doubleValue());
		String costStr = String.format("%.2f", cost.getNumber().doubleValue());
		String revenueStr = String.format("%.2f", revenue.getNumber().doubleValue());
		String profitStr = String.format("%.2f", profit.getNumber().doubleValue());
		String priceCampingTicketsStr = String.format("%.2f", priceCampingTickets.getNumber().doubleValue());
		String priceOneDayTicketsStr = String.format("%.2f", priceOneDayTickets.getNumber().doubleValue());

		model.addAttribute("artistsCost", artistsCostStr);
		model.addAttribute("locationCost", locationCostStr);
		model.addAttribute("cost", costStr);
		model.addAttribute("revenue", revenueStr);
		model.addAttribute("profit", profitStr);

		model.addAttribute("priceCampingTickets", priceCampingTicketsStr);
		model.addAttribute("priceOneDayTickets", priceOneDayTicketsStr);
		model.addAttribute("nCampingTickets", this.nCampingTickets);
		model.addAttribute("nOneDayTickets", this.nOneDayTickets);
		model.addAttribute("soldCampingTickets", this.soldCampingTickets);
		model.addAttribute("soldOneDayTickets", this.soldCampingTickets);
		
		// required for second nav-bar
		model.addAttribute("festival", currentFestival);

		utilsManagement.setCurrentPageLowerHeader("finances");
		utilsManagement.prepareModel(model);
		return "finances";
	}


	@GetMapping("/setTicketNumber")
	public String ticketNumberForm(Model model,
								  @RequestParam("nCampingTickets") long nCampingTickets,
								  @RequestParam("nOneDayTickets") long nOneDayTickets) {

		if (nCampingTickets < 0 || nOneDayTickets < 0)
			return financesPage(model);

		try {
			if (nCampingTickets + nOneDayTickets > currentFestival.getLocation().getVisitorCapacity())
				return financesPage(model);
		}
		catch (NullPointerException e) {}

		this.nCampingTickets = nCampingTickets;
		this.nOneDayTickets = nOneDayTickets;

		model.addAttribute("nCampingTickets", nCampingTickets);
		model.addAttribute("nOneDayTickets", nOneDayTickets);
		return financesPage(model);
	}


	@GetMapping("/setTicketPrice")
	public String ticketPriceForm(Model model,
								  @RequestParam("priceCampingTickets") Double priceCampingTickets,
								  @RequestParam("priceOneDayTickets") Double priceOneDayTickets) {

		if (priceCampingTickets < 0 || priceOneDayTickets < 0)
			return financesPage(model);

		this.priceCampingTickets = Money.of(priceCampingTickets, EURO);
		this.priceOneDayTickets = Money.of(priceOneDayTickets, EURO);

		model.addAttribute("priceCampingTickets", String.format("%.2f", priceCampingTickets));
		model.addAttribute("priceOneDayTickets", String.format("%.2f", priceOneDayTickets));
		return financesPage(model);
	}

}