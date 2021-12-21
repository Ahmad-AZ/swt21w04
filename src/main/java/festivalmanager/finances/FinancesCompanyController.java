package festivalmanager.finances;

import festivalmanager.utils.UtilsManagement;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class FinancesCompanyController {


	UtilsManagement utilsManagement;
	FinancesCompanyManagement financesCompanyManagement;


	FinancesCompanyController(FinancesCompanyManagement financesCompanyManagement,
							  UtilsManagement utilsManagement) {
		this.financesCompanyManagement = financesCompanyManagement;
		this.utilsManagement = utilsManagement;
	}


	@GetMapping("/financesCompany")
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

		utilsManagement.setCurrentPageUpperHeader("finances");
		utilsManagement.prepareModel(model);
		return "financesCompany";
	}

}
