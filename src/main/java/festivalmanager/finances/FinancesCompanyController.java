package festivalmanager.finances;


import org.javamoney.moneta.Money;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


/**
 * A class used to pass on values computed in {@link festivalmanager.finances.FinancesCompanyManagement}
 * to the financesCompany.html file
 * @author Jan Biedermann
 */
@Controller
public class FinancesCompanyController {


	FinancesCompanyManagement financesCompanyManagement;


	/**
	 * Creates a new instance of FinancesCompanyManagement
	 * @param financesCompanyManagement an instance of {@link festivalmanager.finances.FinancesCompanyManagement}
	 */
	FinancesCompanyController(FinancesCompanyManagement financesCompanyManagement) {
		this.financesCompanyManagement = financesCompanyManagement;
	}


	/**
	 * Used to pass on the title of the company finances page to the page header
	 * @return title attribute for the company finances tab
	 */
	@ModelAttribute("title")
	public String getTitle() {
		return "Finanzen FVIV GmbH";
	}


	/**
	 * Prepares the model for the company finances page and returns the company finances page
	 * @param model the spring model for the company finances page
	 * @return the company finances page
	 */
	@GetMapping("/financesCompany")
	@PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER')")
	public String financesPage(Model model) {

		financesCompanyManagement.updateAttributes();
		long nFestivals = financesCompanyManagement.getNFestivals();
		long totalCampingTickets = financesCompanyManagement.getTotalCampingTickets();
		long totalOneDayTickets = financesCompanyManagement.getTotalOneDayTickets();
		Money totalCost = financesCompanyManagement.getTotalCost();
		Money totalRevenue = financesCompanyManagement.getTotalRevenue();

		FinancesController.addAttribute(model, "nFestivals", nFestivals);
		FinancesController.addAttribute(model, "averageCost", totalCost.divide(nFestivals));
		FinancesController.addAttribute(model, "totalCost", totalCost);
		FinancesController.addAttribute(model, "averageRevenue", totalRevenue.divide(nFestivals));
		FinancesController.addAttribute(model, "totalRevenue", totalRevenue);
		FinancesController.addAttribute(model, "totalProfit", totalRevenue.subtract(totalCost));
		FinancesController.addAttribute(model, "averageProfit",
				totalRevenue.subtract(totalCost).divide(nFestivals));

		FinancesController.addAttribute(model, "totalCampingTickets", totalCampingTickets);
		FinancesController.addAttribute(model, "totalOneDayTickets", totalOneDayTickets);
		FinancesController.addAttribute(model, "averageCampingTickets",
				totalCampingTickets / nFestivals);
		FinancesController.addAttribute(model, "averageOneDayTickets",
				totalOneDayTickets / nFestivals);
		FinancesController.addAttribute(model, "averageCostCampingTickets",
				financesCompanyManagement.getAverageCostCampingTickets());
		FinancesController.addAttribute(model, "averageCostOneDayTickets",
				financesCompanyManagement.getAverageCostOneDayTickets());

		return "financesCompany";
	}

}
