package festivalmanager.finances;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.salespointframework.core.Currencies.EURO;


@Service
@Transactional
public class FinancesCompanyManagement {


	FinancesManagement financesManagement;
	FestivalManagement festivalManagement;

	long nFestivals;
	long totalCampingTickets;
	long totalOneDayTickets;
	Money totalCostCampingTickets;
	Money totalCostOneDayTickets;
	Money totalCost;
	Money totalRevenue;


	FinancesCompanyManagement(FinancesManagement financesManagement,
							  FestivalManagement festivalManagement) {
		this.financesManagement = financesManagement;
		this.festivalManagement = festivalManagement;
	}


	public void updateAttributes() {

		List<Festival> festivalList = festivalManagement.findAll().toList();

		nFestivals = festivalList.size();
		totalCost = Money.of(0, EURO);
		totalRevenue = Money.of(0, EURO);
		totalCostCampingTickets = Money.of(0, EURO);
		totalCostOneDayTickets = Money.of(0, EURO);

		for (Festival festival : festivalList) {

			financesManagement.setFestival(festival.getId());
			financesManagement.getLocationCost();
			financesManagement.getArtistsCost();
			financesManagement.getEquipmentCost();
			financesManagement.getStaffCost();
			financesManagement.getCateringCost();
			financesManagement.getTicketsRevenue();
			financesManagement.getCateringRevenue();

			totalCampingTickets += financesManagement.getNCampingTickets();
			totalOneDayTickets += financesManagement.getNOneDayTickets();
			totalCostCampingTickets = totalCostCampingTickets.add(financesManagement.getPriceCampingTickets());
			totalCostOneDayTickets = totalCostOneDayTickets.add(financesManagement.getPriceOneDayTickets());
			totalCost = totalCost.add(financesManagement.getTotalCost());
			totalRevenue = totalRevenue.add(financesManagement.getTotalRevenue());
		}
	}


	public long getNFestivals() {
		return nFestivals;
	}


	public Money getTotalCost() {
		return totalCost;
	}


	public Money getTotalRevenue() {
		return totalRevenue;
	}


	public Money getAverageCostCampingTickets() {
		return totalCostCampingTickets.divide(nFestivals);
	}


	public long getTotalCampingTickets() {
		return totalCampingTickets;
	}


	public Money getAverageCostOneDayTickets() {
		return totalCostOneDayTickets.divide(nFestivals);
	}


	public long getTotalOneDayTickets() {
		return totalOneDayTickets;
	}


}
