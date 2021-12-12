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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

		for (Stage stage : currentFestival.getStages()) {
			equipmentCost = equipmentCost.add(stage.getRentalPerDay().multiply(durationDays));
		}

		totalCost.add(equipmentCost);
		return equipmentCost;
	}


	public Money getStaffCost() {

		Money staffCost = Money.of(0, EURO);
		List<Person> staffList = new ArrayList<>();
		// Roles for which the salary is paid on a per-festival basis
		List<String> toBePaid = Arrays.asList("SECURITY", "CATERING", "FESTIVAL_LEADER", "ADMISSION");

		Streamable<Person> staffMembers = staffManagement.findByFestivalId(currentFestival.getId());
		staffMembers.forEach(staffList::add);

		for (Person staffMember: staffMembers) {

			if (toBePaid.contains(staffMember.getRole())) {
				Money salary = staffMember.getSalary();
				// Staff members work 8 hours a day
				staffCost = staffCost.add(salary.multiply(8).multiply(durationDays));
			}
		}

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
