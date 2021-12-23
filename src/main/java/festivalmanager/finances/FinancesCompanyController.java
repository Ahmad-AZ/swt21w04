package festivalmanager.finances;

import festivalmanager.utils.UtilsManagement;
import org.javamoney.moneta.Money;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class FinancesCompanyController {


	UtilsManagement utilsManagement;
	FinancesCompanyManagement financesCompanyManagement;


	FinancesCompanyController(FinancesCompanyManagement financesCompanyManagement,
							  UtilsManagement utilsManagement) {
		this.financesCompanyManagement = financesCompanyManagement;
		this.utilsManagement = utilsManagement;
	}


	@ModelAttribute("title")
	public String getTitle() {
		return "Finanzen FVIV GmbH";
	}


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

		utilsManagement.setCurrentPageUpperHeader("financesCompany");
		utilsManagement.prepareModel(model, null);
		return "financesCompany";
	}

}
