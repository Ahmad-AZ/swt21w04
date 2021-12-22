package festivalmanager.finances;

import static org.salespointframework.core.Currencies.EURO;

import java.util.Arrays;
import java.util.List;

import festivalmanager.hiring.Show;
import org.javamoney.moneta.Money;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Stage;
import festivalmanager.catering.CateringController;
import festivalmanager.catering.CateringProduct;
import festivalmanager.catering.CateringProductCatalog;
import festivalmanager.catering.CateringSales;
import festivalmanager.catering.CateringSalesItem;
import festivalmanager.catering.CateringStock;
import festivalmanager.catering.CateringStockItem;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.hiring.Artist;
import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import festivalmanager.ticketShop.Ticket;
import festivalmanager.ticketShop.TicketManagement;
import festivalmanager.utils.UtilsManagement;


@Service
@Transactional
public class FinancesManagement {


	Festival currentFestival;
	Ticket ticketInformation;
	Money totalRevenue;
	Money totalCost;
	long durationDays;

	EquipmentManagement equipmentManagement;
	FestivalManagement festivalManagement;
	UtilsManagement utilsManagement;
	StaffManagement staffManagement;
	TicketManagement ticketManagement;
	CateringController cateringController;


	FinancesManagement(FestivalManagement festivalManagement,
					   UtilsManagement utilsManagement,
					   EquipmentManagement equipmentManagement,
					   StaffManagement staffManagement,
					   TicketManagement ticketManagement,
					   CateringController cateringController) {

		this.equipmentManagement = equipmentManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
		this.staffManagement = staffManagement;
		this.ticketManagement = ticketManagement;
		this.cateringController = cateringController;

		currentFestival = null;
		ticketInformation = null;

		durationDays = 0;
		totalRevenue = Money.of(0, EURO);
		totalCost = Money.of(0, EURO);
	}


	public void setFestival(long festivalId) {
		currentFestival = festivalManagement.findById(festivalId).get();
		ticketInformation = ticketManagement.TicketsByFestival(currentFestival.getId());
		if (ticketInformation == null) {
			ticketInformation = new Ticket();
		}

		durationDays = currentFestival.getEndDate().toEpochDay() - currentFestival.getStartDate().toEpochDay() + 1;
		totalRevenue = Money.of(0, EURO);
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
				artistsCost = artistsCost.add(artist.getPrice().multiply(durationDays));
			}
		}

		totalCost = totalCost.add(artistsCost);
		return artistsCost;
	}


	public Money getEquipmentCost() {

		Money equipmentCost = Money.of(0, EURO);

		for (SalespointIdentifier equipmentId: currentFestival.getEquipments().keySet()) {

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
		// List<String> toBePaid = Arrays.asList("SECURITY", "CATERING", "FESTIVAL_LEADER", "ADMISSION");

		for (Person staffMember : staffMembers) {

			// if (toBePaid.contains(staffMember.getRole())) {}

			Money salary = staffMember.getSalary();
			// Staff members work 8 hours a day
			staffCost = staffCost.add(salary.multiply(8).multiply(durationDays));
		}

		if (!currentFestival.artistsIsEmpty()) {
			for (Artist artist: currentFestival.getArtist()) {
				// Stage technicians are paid 100 EURO each day
				Money stageTechnicianCost = Money.of(100, EURO).multiply(durationDays);
				staffCost = staffCost.add(stageTechnicianCost.multiply(artist.getStageTechnician()));
			}
		}

		totalCost = totalCost.add(staffCost);
		return staffCost;
	}


	public Money getCateringCost() {

		Money cateringCost = Money.of(0, EURO);
		CateringStock cateringStock = cateringController.getStock();

		for (CateringStockItem cateringStockItem :
				cateringStock.findByFestivalId(currentFestival.getId())) {
			Long amount = cateringStockItem.getQuantity().getAmount().longValue();
			Money price = cateringStockItem.getBuyingPrice();
			cateringCost = cateringCost.add(price.multiply(amount));
		}

		totalCost = totalCost.add(cateringCost);
		return cateringCost;
	}


	public Money getTicketsRevenue() {

		Money ticketsRevenue = Money.of(0, EURO);

		ticketsRevenue = ticketsRevenue.add(getPriceCampingTickets().multiply(getNCampingTickets()));
		ticketsRevenue = ticketsRevenue.add(getPriceOneDayTickets().multiply(getNOneDayTickets()));
		totalRevenue = totalRevenue.add(ticketsRevenue);
		return ticketsRevenue;
	}


	public Money getCateringRevenue() {

		Money cateringRevenue = Money.of(0, EURO);

		CateringSales cateringSales = cateringController.getCateringSales();
		CateringProductCatalog cateringProductCatalog = cateringController.getCateringProductCatalog();

		for (CateringSalesItem salesItem : cateringSales.findAll()) {

			if (salesItem.getFestivalId() == currentFestival.getId()) {

				CateringProduct cateringProduct = salesItem.getCateringProduct();
				Money productPrice = Money.of(cateringProduct.getPrice().getNumber().doubleValue(), EURO);
				long productAmount = salesItem.getQuantity().getAmount().longValue();
				cateringRevenue = cateringRevenue.add(productPrice.multiply(productAmount));
			}
		}

		totalRevenue = totalRevenue.add(cateringRevenue);
		return cateringRevenue;
	}


	public Money getPriceCampingTickets() {

		Money priceCampingTickets = Money.of(ticketInformation.getCampingTicketPrice(), EURO);
		return priceCampingTickets;
	}


	public Money getPriceOneDayTickets() {

		Money priceOneDayTickets = Money.of(ticketInformation.getDayTicketPrice(), EURO);
		return priceOneDayTickets;
	}


	public long getNCampingTickets() {

		long nCampingTickets = ticketInformation.getSoldCampingTicket();
		return nCampingTickets;
	}


	public long getNOneDayTickets() {

		long nOneDayTickets = ticketInformation.getSoldDayTicket();
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
