package festivalmanager.finances;

import org.javamoney.moneta.Money;
import festivalmanager.festival.Festival;
import static org.salespointframework.core.Currencies.EURO;


public class Finances {


	public Money getCost(Festival currentFestival) {

		Money cost = Money.of(0, EURO);
		Money locationPrice = Money.of(0, EURO);

		long durationMs = currentFestival.getEndDate().getTime() - currentFestival.getStartDate().getTime();
		long durationDays = durationMs / 86400000 + 1;
		try {
			Money locationPricePerDay = currentFestival.getLocation().getPricePerDay();
			locationPrice = locationPricePerDay.multiply(durationDays);
		}
		catch (NullPointerException e) {}

		cost = cost.add(locationPrice);
		return cost;
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
