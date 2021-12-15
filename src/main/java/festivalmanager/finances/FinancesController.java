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


	private Festival currentFestival;
	private FinancesManagement financesManagement;
	private FestivalManagement festivalManagement;
	private UtilsManagement utilsManagement;

	private long nCampingTicketsExpected;
	private long nOneDayTicketsExpected;

	private Money priceCampingTicketsExpected;
	private Money priceOneDayTicketsExpected;


	FinancesController(FinancesManagement financesManagement,
					   FestivalManagement festivalManagement,
					   UtilsManagement utilsManagement) {
		this.financesManagement = financesManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;

		currentFestival = null;
		resetAttributes();
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Finanzübersicht";
	}


	private void resetAttributes() {
		nCampingTicketsExpected = 0;
		nOneDayTicketsExpected = 0;
		priceCampingTicketsExpected = Money.of(0, EURO);
		priceOneDayTicketsExpected = Money.of(0, EURO);
	}


	@GetMapping("/finances")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
	String financesPage(Model model) {

		if(this.currentFestival != null &&
				this.currentFestival.getId() != utilsManagement.getCurrentFestivalId()) {

			resetAttributes();
		}

		this.financesManagement.updateFestival();
		this.currentFestival = festivalManagement.findById(utilsManagement.getCurrentFestivalId()).get();

		addAttribute(model, "artistsCost", financesManagement.getArtistsCost());
		addAttribute(model, "locationCost", financesManagement.getLocationCost());
		addAttribute(model, "equipmentCost", financesManagement.getEquipmentCost());
		addAttribute(model, "staffCost", financesManagement.getStaffCost());
		addAttribute(model, "cateringCost", financesManagement.getCateringCost());

		addAttribute(model, "priceCampingTickets", financesManagement.getPriceCampingTickets());
		addAttribute(model, "priceOneDayTickets", financesManagement.getPriceOneDayTickets());
		addAttribute(model, "nCampingTickets", financesManagement.getNCampingTickets());
		addAttribute(model, "nOneDayTickets", financesManagement.getNOneDayTickets());

		addAttribute(model, "ticketsRevenue", financesManagement.getTicketsRevenue());
		addAttribute(model, "cateringRevenue", financesManagement.getCateringRevenue());

		Money totalCost = financesManagement.getTotalCost();
		addAttribute(model, "totalCost", totalCost);
		Money totalRevenue = financesManagement.getTotalRevenue();
		addAttribute(model, "totalRevenue", totalRevenue);
		addAttribute(model,"profit", financesManagement.getProfit(totalRevenue, totalCost));

		addAttribute(model,"priceCampingTicketsExpected", priceCampingTicketsExpected);
		addAttribute(model, "priceOneDayTicketsExpected", priceOneDayTicketsExpected);
		addAttribute(model, "nCampingTicketsExpected", nCampingTicketsExpected);
		addAttribute(model, "nOneDayTicketsExpected", nOneDayTicketsExpected);

		Money revenueExpected = financesManagement.getRevenueExpected(
				priceCampingTicketsExpected,
				priceOneDayTicketsExpected,
				nCampingTicketsExpected,
				nOneDayTicketsExpected);
		Money profitExpected = financesManagement.getProfit(revenueExpected, totalCost);
		addAttribute(model, "revenueExpected", revenueExpected);
		addAttribute(model,"profitExpected", profitExpected);

		utilsManagement.setCurrentPageLowerHeader("finances");
		utilsManagement.prepareModel(model);
		return "finances";
	}


	private void addAttribute(Model model, String attributeName, Object attributeValue) {

		if (attributeValue.getClass().getSimpleName().equals("Money")) {
			String attributeStr = String.format("%.2f", ((Money) attributeValue).getNumber().doubleValue());
			model.addAttribute(attributeName, attributeStr);
		} else {
			model.addAttribute(attributeName, attributeValue);
		}
	}


	@GetMapping("/setTicketNumber")
	public String ticketNumberForm(Model model,
								  @RequestParam("nCampingTicketsExpected") long nCampingTicketsExpected,
								  @RequestParam("nOneDayTicketsExpected") long nOneDayTicketsExpected) {

		if (nCampingTicketsExpected < 0 || nOneDayTicketsExpected < 0) {
			//return financesPage(model);
			return "redirect:/finances";
		}

		if (currentFestival != null && currentFestival.getLocation() != null &&
				nCampingTicketsExpected + nOneDayTicketsExpected
						> currentFestival.getLocation().getVisitorCapacity()) {
			//return financesPage(model);
			return "redirect:/finances";
		}

		this.nCampingTicketsExpected = nCampingTicketsExpected;
		this.nOneDayTicketsExpected = nOneDayTicketsExpected;
		//return financesPage(model);
		return "redirect:/finances";
	}


	@GetMapping("/setTicketPrice")
	public String ticketPriceForm(Model model,
								  @RequestParam("priceCampingTicketsExpected") Double priceCampingTicketsExpected,
								  @RequestParam("priceOneDayTicketsExpected") Double priceOneDayTicketsExpected) {

		if (priceCampingTicketsExpected < 0 || priceOneDayTicketsExpected < 0) {
			return "redirect:/finances";
		}

		this.priceCampingTicketsExpected = Money.of(priceCampingTicketsExpected, EURO);
		this.priceOneDayTicketsExpected = Money.of(priceOneDayTicketsExpected, EURO);
		return "redirect:/finances";
	}

}