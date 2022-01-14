package festivalmanager.finances;


import org.javamoney.moneta.Money;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.utils.UtilsManagement;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.salespointframework.core.Currencies.EURO;


/**
 * A class used to pass on values computed in {@link festivalmanager.finances.FinancesManagement} to the finances page
 * @author Jan Biedermann
 */
@Controller
class FinancesController {


	private FinancesManagement financesManagement;
	private FestivalManagement festivalManagement;
	private UtilsManagement utilsManagement;

	// Maps festival ID to expected sales numbers
	private Map<Long, Long> nCampingTicketsMap;
	private Map<Long, Long> nOneDayTicketsMap;
	private Map<Long, Money> priceCampingTicketsMap;
	private Map<Long, Money> priceOneDayTicketsMap;


	/**
	 * Creates a new instance of FinancesController
	 * @param financesManagement an instance of {@link FinancesManagement}
	 * @param festivalManagement an instance of {@link FestivalManagement}
	 * @param utilsManagement an instance of {@link UtilsManagement}
	 */
	FinancesController(FinancesManagement financesManagement,
					   FestivalManagement festivalManagement,
					   UtilsManagement utilsManagement) {
		this.financesManagement = financesManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;

		nCampingTicketsMap = new HashMap<>();
		nOneDayTicketsMap = new HashMap<>();
		priceCampingTicketsMap = new HashMap<>();
		priceOneDayTicketsMap = new HashMap<>();
	}


	/**
	 * Used to pass on the title of the finances page to the page header
	 * @return title attribute for the finances tab
	 */
	@ModelAttribute("title")
	public String getTitle() {
		return "Finanzübersicht";
	}


	/**
	 * Generates a page which summarizes financial information on a specific festival
	 * (as opposed to financial information that concerns all festivals,
	 * which is done in {@link FinancesCompanyManagement})
	 * @param model the spring model for the finances page
	 * @param festivalId the ID of the specific festival for which the finances page is to be generated
	 * @return the finances page for the festival belonging to festivalId
	 */
	@GetMapping("/finances/{festivalId}")
	@PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
	String financesPage(Model model, @PathVariable Long festivalId) {

		Optional<Festival> festivalOptional = festivalManagement.findById(festivalId);
		if (festivalOptional.isEmpty()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found");
		}

		Festival currentFestival = festivalOptional.get();
		financesManagement.setFestival(currentFestival.getId());

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

		Money priceCampingTicketsExpected =
				priceCampingTicketsMap.getOrDefault(festivalId, Money.of(0, EURO));
		Money priceOneDayTicketsExpected =
				priceOneDayTicketsMap.getOrDefault(festivalId, Money.of(0, EURO));
		long nCampingTicketsExpected =
				nCampingTicketsMap.getOrDefault(festivalId, 0L);
		long nOneDayTicketsExpected =
				nOneDayTicketsMap.getOrDefault(festivalId, 0L);

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

		utilsManagement.prepareModel(model, festivalId);
		return "finances";
	}


	/**
	 * If an instance of Money is to be used as a model attribute,
	 * some formatting is required to get the desired output.
	 * This function takes care of the formatting and adds the formatted attribute to the model.
	 * @param model the spring model to which an attribute is to be added
	 * @param attributeName a name for the model attribute
	 * @param attributeValue the value belonging to the attribute name
	 */
	public static void addAttribute(Model model, String attributeName, Object attributeValue) {

		if (attributeValue.getClass().getSimpleName().equals("Money")) {
			String attributeStr = String.format("%.2f", ((Money) attributeValue).getNumber().doubleValue());
			model.addAttribute(attributeName, attributeStr);
		} else {
			model.addAttribute(attributeName, attributeValue);
		}
	}


	/**
	 * The finances page can compute expected cost and revenue for a festival if given expected sales numbers.
	 * This function is used to pass on the expected sales numbers from the user to the finances controller.
	 * @param festivalId the festivalId for which the expected number of ticket sales is to be set
	 * @param nCampingTicketsExpected the expected number of camping ticket sales
	 * @param nOneDayTicketsExpected the expected number of one-day ticket sales
	 * @return a redirect to the finances page
	 */
	@GetMapping("/finances/setTicketNumber")
	public String ticketNumberForm(@RequestParam("festivalId") Long festivalId,
								  @RequestParam("nCampingTicketsExpected") long nCampingTicketsExpected,
								  @RequestParam("nOneDayTicketsExpected") long nOneDayTicketsExpected) {

		Optional<Festival> festivalOptional = festivalManagement.findById(festivalId);
		if (festivalOptional.isEmpty()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found");
		}

		Festival currentFestival = festivalOptional.get();

		if (nCampingTicketsExpected < 0 || nOneDayTicketsExpected < 0) {
			return "redirect:/finances/" + festivalId;
		}

		if (currentFestival.getLocation() != null &&
				nCampingTicketsExpected + nOneDayTicketsExpected
						> currentFestival.getLocation().getVisitorCapacity()) {
			return "redirect:/finances/" + festivalId;
		}

		nCampingTicketsMap.put(festivalId, nCampingTicketsExpected);
		nOneDayTicketsMap.put(festivalId, nOneDayTicketsExpected);
		return "redirect:/finances/" + festivalId;
	}


	/**
	 * The finances page can compute expected cost and revenue for a festival if given expected ticket prices.
	 * This function is used to pass on the expected ticket prices from the user to the finances controller.
	 * @param festivalId the festivalId for which the expected ticket price is to be set
	 * @param priceCampingTicketsExpected the expected price of camping tickets
	 * @param priceOneDayTicketsExpected the expected price of camping tickets
	 * @return a redirect to the finances page
	 */
	@GetMapping("/finances/setTicketPrice")
	public String ticketPriceForm(@RequestParam("festivalId") Long festivalId,
								  @RequestParam("priceCampingTicketsExpected") Double priceCampingTicketsExpected,
								  @RequestParam("priceOneDayTicketsExpected") Double priceOneDayTicketsExpected) {

		if (priceCampingTicketsExpected < 0 || priceOneDayTicketsExpected < 0) {
			return "redirect:/finances/" + festivalId;
		}

		priceCampingTicketsMap.put(festivalId, Money.of(priceCampingTicketsExpected, EURO));
		priceOneDayTicketsMap.put(festivalId, Money.of(priceOneDayTicketsExpected, EURO));
		return "redirect:/finances/" + festivalId;
	}

}