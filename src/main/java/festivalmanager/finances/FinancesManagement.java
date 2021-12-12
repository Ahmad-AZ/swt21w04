package festivalmanager.finances;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Artist;
import festivalmanager.utils.UtilsManagement;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.salespointframework.core.Currencies.EURO;


@Service
@Transactional
public class FinancesManagement {


	Festival currentFestival;
	Money totalCost;
	long durationDays;

	EquipmentManagement equipmentManagement;
	FestivalManagement festivalManagement;
	UtilsManagement utilsManagement;


	FinancesManagement(FestivalManagement festivalManagement,
					   UtilsManagement utilsManagement,
					   EquipmentManagement equipmentManagement) {

		this.equipmentManagement = equipmentManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;

		currentFestival = null;
		durationDays = 0;
		totalCost = Money.of(0, EURO);
	}


	public void updateFestival() {
		currentFestival = festivalManagement.findById(utilsManagement.getCurrentFestivalId()).get();
		durationDays = currentFestival.getEndDate().toEpochDay() - currentFestival.getStartDate().toEpochDay() + 1;
		totalCost = Money.of(0, EURO);
	}


	public Money getLocationCost() {

		Money locationCost = Money.of(0, EURO);

		if (currentFestival.getLocation() != null) {
			Money locationPricePerDay = currentFestival.getLocation().getPricePerDay();
			locationCost = locationPricePerDay.multiply(durationDays);
		}

		totalCost.add(locationCost);
		return locationCost;
	}


	public Money getArtistsCost() {

		Money artistsCost = Money.of(0, EURO);

		if (!currentFestival.artistsIsEmpty()) {
			for (Artist artist: currentFestival.getArtist()) {
				artistsCost = artistsCost.add(artist.getPrice());
			}
		}

		totalCost.add(artistsCost);
		return artistsCost;
	}


	public Money getEquipmentCost() {

		Money equipmentCost = Money.of(0, EURO);

		for (long equipmentId: currentFestival.getEquipments().keySet()) {

			Equipment equipment = equipmentManagement.findById(equipmentId).get();
			long amount = currentFestival.getEquipments().get(equipmentId);
			Money equipmentCostSingle = equipment.getRentalPerDay().multiply(durationDays);
			equipmentCost = equipmentCost.add(equipmentCostSingle.multiply(amount));
		}

		totalCost.add(equipmentCost);
		return equipmentCost;
	}


	public Money getStaffCost() {

		Money staffCost = Money.of(0, EURO);

		totalCost.add(staffCost);
		return staffCost;
	}


	public Money getRevenue(Money priceCampingTickets, Money priceOneDayTickets,
							long nCampingTickets, long nOneDayTickets) {

		Money revenue = Money.of(0, EURO);
		revenue = revenue.add(priceCampingTickets.multiply(nCampingTickets));
		revenue = revenue.add(priceOneDayTickets.multiply(nOneDayTickets));
		return revenue;
	}


	public Money getProfit(Money revenue) {
		return revenue.subtract(totalCost);
	}


	public Money getTotalCost() {
		return totalCost;
	}

}
