package festivalmanager.finances;


import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.salespointframework.core.Currencies.EURO;


/**
 * A class used to gather and compute the data that is to be displayed on the company finances page
 * @author Jan Biedermann
 */
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


	/**
	 * Creates a new instance of FinancesCompanyManagement
	 * @param financesManagement an instance of {@link FinancesManagement}
	 * @param festivalManagement an instance of {@link FestivalManagement}
	 */
	FinancesCompanyManagement(FinancesManagement financesManagement,
							  FestivalManagement festivalManagement) {
		this.financesManagement = financesManagement;
		this.festivalManagement = festivalManagement;
	}


	/**
	 * Computes the attributes which can be accessed through the various getter-methods of this class.
	 * Hence, it has to be called before making use of said getter-methods.
	 */
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


	/**
	 * Returns the number of festivals currently being planned in the software
	 * @return the number of festivals currently being planned in the software
	 */
	public long getNFestivals() {
		return nFestivals;
	}


	/**
	 * Returns the total cost caused by all festivals that are currently being planned
	 * @return the total cost caused by all festivals that are currently being planned
	 */
	public Money getTotalCost() {
		return totalCost;
	}


	/**
	 * Returns the total revenue due to festivals that are currently being planned
	 * @return the total revenue due to festivals that are currently being planned
	 */
	public Money getTotalRevenue() {
		return totalRevenue;
	}


	/**
	 * Returns the average cost of one camping ticket across all festivals
	 * @return the average cost of one camping ticket across all festivals
	 */
	public Money getAverageCostCampingTickets() {
		return totalCostCampingTickets.divide(nFestivals);
	}


	/**
	 * Returns the total number of camping tickets that have been generated for festivals
	 * @return the total number of camping tickets that have been generated for festivals
	 */
	public long getTotalCampingTickets() {
		return totalCampingTickets;
	}


	/**
	 * Returns the average cost of a one-day ticket across all festivals
	 * @return the average cost of a one-day ticket across all festivals
	 */
	public Money getAverageCostOneDayTickets() {
		return totalCostOneDayTickets.divide(nFestivals);
	}


	/**
	 * Returns the total number of one-day tickets that have been generated for festivals
	 * @return the total number of one-day tickets that have been generated for festivals
	 */
	public long getTotalOneDayTickets() {
		return totalOneDayTickets;
	}

}
