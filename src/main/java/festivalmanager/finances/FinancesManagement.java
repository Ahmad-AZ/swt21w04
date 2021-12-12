package festivalmanager.finances;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Artist;
import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import festivalmanager.utils.UtilsManagement;
import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.salespointframework.core.Currencies.EURO;


@Service
@Transactional
public class FinancesManagement {


	Festival currentFestival;
	Money totalRevenue;
	Money totalCost;
	long durationDays;

	EquipmentManagement equipmentManagement;
	FestivalManagement festivalManagement;
	UtilsManagement utilsManagement;
	StaffManagement staffManagement;


	FinancesManagement(FestivalManagement festivalManagement,
					   UtilsManagement utilsManagement,
					   EquipmentManagement equipmentManagement,
					   StaffManagement staffManagement) {

		this.equipmentManagement = equipmentManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
		this.staffManagement = staffManagement;

		currentFestival = null;
		durationDays = 0;
		totalRevenue = Money.of(0, EURO);
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

		totalCost = totalCost.add(locationCost);
		return locationCost;
	}


	public Money getArtistsCost() {

		Money artistsCost = Money.of(0, EURO);

		if (!currentFestival.artistsIsEmpty()) {
			for (Artist artist: currentFestival.getArtist()) {
				artistsCost = artistsCost.add(artist.getPrice());
			}
		}

		totalCost = totalCost.add(artistsCost);
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

		for (Stage stage : currentFestival.getStages()) {
			equipmentCost = equipmentCost.add(stage.getRentalPerDay().multiply(durationDays));
		}

		totalCost = totalCost.add(equipmentCost);
		return equipmentCost;
	}


	public Money getStaffCost() {

		Money staffCost = Money.of(0, EURO);
		Streamable<Person> staffMembers = staffManagement.findByFestivalId(currentFestival.getId());
		// Roles for which the salary is paid on a per-festival basis
		List<String> toBePaid = Arrays.asList("SECURITY", "CATERING", "FESTIVAL_LEADER", "ADMISSION");

		for (Person staffMember: staffMembers) {

			if (toBePaid.contains(staffMember.getRole())) {
				Money salary = staffMember.getSalary();
				// Staff members work 8 hours a day
				staffCost = staffCost.add(salary.multiply(8).multiply(durationDays));
			}
		}

		totalCost = totalCost.add(staffCost);
		return staffCost;
	}


	public Money getTicketsRevenue() {

		Money ticketsRevenue = Money.of(0, EURO);

		ticketsRevenue = ticketsRevenue.add(getPriceCampingTickets().multiply(getNCampingTickets()));
		ticketsRevenue = ticketsRevenue.add(getPriceOneDayTickets().multiply(getNOneDayTickets()));
		totalRevenue = totalRevenue.add(ticketsRevenue);
		return ticketsRevenue;
	}


	public Money getCateringRevenue() {

		Money cateringRevenue = Money.of(1000, EURO);

		totalRevenue = totalRevenue.add(cateringRevenue);
		return cateringRevenue;
	}


	public Money getPriceCampingTickets() {

		Money priceCampingTickets = Money.of(100, EURO);
		return priceCampingTickets;
	}


	public Money getPriceOneDayTickets() {

		Money priceOneDayTickets = Money.of(50, EURO);
		return priceOneDayTickets;
	}


	public long getNCampingTickets() {

		long nCampingTickets = 123;
		return nCampingTickets;
	}


	public long getNOneDayTickets() {

		long nOneDayTickets = 123;
		return nOneDayTickets;
	}


	public Money getRevenueExpected(Money priceCampingTickets,
									Money priceOneDayTickets,
									long nCampingTickets,
									long nOneDayTickets) {

		Money revenue = Money.of(0, EURO);
		revenue = revenue.add(priceCampingTickets.multiply(nCampingTickets));
		revenue = revenue.add(priceOneDayTickets.multiply(nOneDayTickets));
		return revenue;
	}


	public Money getTotalRevenue() {
		return totalRevenue;
	}


	public Money getTotalCost() {
		return totalCost;
	}


	public Money getProfit(Money revenue, Money cost) {
		return revenue.subtract(cost);
	}

}
