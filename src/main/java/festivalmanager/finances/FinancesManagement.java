package festivalmanager.finances;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Artist;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.salespointframework.core.Currencies.EURO;


@Service
@Transactional
public class FinancesManagement {

	Festival currentFestival;
	FestivalManagement festivalManagement;


	FinancesManagement(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
		this.currentFestival = null;
	}


	public void updateFestival(long currentFestivalId) {
		this.currentFestival = festivalManagement.findById(currentFestivalId).get();
	}


	public Money getCost() {

		Money cost = Money.of(0, EURO);
		Money artistCost = getArtistsCost();
		Money locationCost = getLocationCost();

		cost = cost.add(artistCost);
		cost = cost.add(locationCost);
		return cost;
	}


	public Money getLocationCost() {

		Money locationCost = Money.of(0, EURO);
		long durationDays = currentFestival.getEndDate().toEpochDay() - currentFestival.getStartDate().toEpochDay() + 1;

		try {
			Money locationPricePerDay = currentFestival.getLocation().getPricePerDay();
			locationCost = locationPricePerDay.multiply(durationDays);
		}
		catch (NullPointerException e) {}

		return locationCost;
	}


	public Money getArtistsCost() {

		Money artistCost = Money.of(0, EURO);

		if (!currentFestival.artistsIsEmpty()) {
			for (Artist artist: currentFestival.getArtist()) {

			}
		}

		return artistCost;
	}


	public Money getRevenue(Festival currentFestival,
							Money priceCampingTickets, Money priceOneDayTickets,
							long nCampingTickets, long nOneDayTickets) {

		Money revenue = Money.of(0, EURO);
		revenue = revenue.add(priceCampingTickets.multiply(nCampingTickets));
		revenue = revenue.add(priceOneDayTickets.multiply(nOneDayTickets));
		return revenue;
	}


	public Money getProfit(Money cost, Money revenue) {
		return revenue.subtract(cost);
	}


}
