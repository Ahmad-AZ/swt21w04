package prototype.finances;

import org.javamoney.moneta.Money;
import prototype.festival.Festival;
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
		catch (Exception e) {}

		cost = cost.add(locationPrice);
		return cost;
	}


	public Money getRevenue(Festival currentFestival) {
		Money revenue = Money.of(0, EURO);
		return revenue;
	}


	public Money getProfit(Money cost, Money revenue) {
		return revenue.subtract(cost);
	}
}
