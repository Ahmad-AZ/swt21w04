package festivalmanager.finances;


import static org.salespointframework.core.Currencies.EURO;

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


/**
 * A class used to gather and compute the data that is to be displayed on the finances page for a festival
 * @author Jan Biedermann
 */
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
	StaffManagement staffManagement;
	TicketManagement ticketManagement;
	CateringController cateringController;


	/**
	 * Creates a new instance of FinancesManagement
	 * @param festivalManagement an instance of {@link FestivalManagement}
	 * @param equipmentManagement an instance of {@link EquipmentManagement}
	 * @param staffManagement an instance of {@link StaffManagement}
	 * @param ticketManagement an instance of {@link TicketManagement}
	 * @param cateringController an instance of {@link CateringController}
	 */
	FinancesManagement(FestivalManagement festivalManagement,
					   EquipmentManagement equipmentManagement,
					   StaffManagement staffManagement,
					   TicketManagement ticketManagement,
					   CateringController cateringController) {

		this.equipmentManagement = equipmentManagement;
		this.festivalManagement = festivalManagement;
		this.staffManagement = staffManagement;
		this.ticketManagement = ticketManagement;
		this.cateringController = cateringController;

		currentFestival = null;
		ticketInformation = null;

		durationDays = 0;
		totalRevenue = Money.of(0, EURO);
		totalCost = Money.of(0, EURO);
	}


	/**
	 * FinancesManagement computes financial data for one specific festival.
	 * This function is used to determine which festival that is.
	 * @param festivalId the ID of the festival for which the finances page is to be generated
	 */
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


	/**
	 * Returns the cost due to the location
	 * @return the cost due to the location
	 */
	public Money getLocationCost() {

		Money locationCost = Money.of(0, EURO);

		if (currentFestival.getLocation() != null) {
			Money locationPricePerDay = currentFestival.getLocation().getPricePerDay();
			locationCost = locationPricePerDay.multiply(durationDays);
		}

		totalCost = totalCost.add(locationCost);
		return locationCost;
	}


	/**
	 * Returns the cost due to artists
	 * @return the cost due to artists
	 */
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


	/**
	 * Returns the cost due to equipment
	 * @return the cost due to equipment
	 */
	public Money getEquipmentCost() {

		Money equipmentCost = Money.of(0, EURO);

		for (SalespointIdentifier equipmentId: currentFestival.getEquipments().keySet()) {

			Equipment equipment = equipmentManagement.findEquipmentById(equipmentId).get();
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


	/**
	 * Returns the cost due to employees
	 * @return the cost due to employees
	 */
	public Money getStaffCost() {

		Money staffCost = Money.of(0, EURO);
		Streamable<Person> staffMembers = staffManagement.findByFestivalId(currentFestival.getId());

		for (Person staffMember : staffMembers) {

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


	/**
	 * Returns the cost due to catering
	 * @return the cost due to catering
	 */
	public Money getCateringCost() {

		Money cateringCost = Money.of(0, EURO);
		CateringStock cateringStock = cateringController.getStock();

		for (CateringStockItem cateringStockItem :
				cateringStock.findByFestivalId(currentFestival.getId())) {
			long amount = cateringStockItem.getOrderQuantity();
			Money price = cateringStockItem.getBuyingPrice();
			cateringCost = cateringCost.add(price.multiply(amount));
		}

		totalCost = totalCost.add(cateringCost);
		return cateringCost;
	}


	/**
	 * Returns the revenue due to ticket sales
	 * @return the revenue due to ticket sales
	 */
	public Money getTicketsRevenue() {

		Money ticketsRevenue = Money.of(0, EURO);

		ticketsRevenue = ticketsRevenue.add(getPriceCampingTickets().multiply(getNCampingTickets()));
		ticketsRevenue = ticketsRevenue.add(getPriceOneDayTickets().multiply(getNOneDayTickets()));
		totalRevenue = totalRevenue.add(ticketsRevenue);
		return ticketsRevenue;
	}


	/**
	 * Returns the revenue due to catering
	 * @return the revenue due to catering
	 */
	public Money getCateringRevenue() {

		CateringSales cateringSales = cateringController.getCateringSales();
		Money cateringRevenue = Money.of(0, EURO);

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


	/**
	 * Returns the price of camping tickets for this festival
	 * @return the price of camping tickets for this festival
	 */
	public Money getPriceCampingTickets() {
		return Money.of(ticketInformation.getCampingTicketPrice(), EURO);
	}


	/**
	 * Returns the price of one-day tickets for this festival
	 * @return the price of one-day tickets for this festival
	 */
	public Money getPriceOneDayTickets() {
		return Money.of(ticketInformation.getDayTicketPrice(), EURO);
	}


	/**
	 * Returns the number of camping tickets available for this festival
	 * @return the number of camping tickets available for this festival
	 */
	public long getNCampingTickets() {
		return ticketInformation.getSoldCampingTicket();
	}


	/**
	 * Returns the number of one-day tickets available for this festival
	 * @return the number of one-day tickets available for this festival
	 */
	public long getNOneDayTickets() {
		return ticketInformation.getSoldDayTicket();
	}


	/**
	 * A function which computes the revenue the festival would cause if it
	 * had the sales numbers and ticket prices passed to this function
	 * @param priceCampingTickets the expected price of camping tickets
	 * @param priceOneDayTickets the expected price of one-day tickets
	 * @param nCampingTickets the expected number of camping ticket sales
	 * @param nOneDayTickets the expected number of one-day ticket sales
	 * @return the revenue the festival would cause if it
	 * had the sales numbers and ticket prices passed to the function
	 */
	public Money getRevenueExpected(Money priceCampingTickets,
									Money priceOneDayTickets,
									long nCampingTickets,
									long nOneDayTickets) {

		Money revenue = Money.of(0, EURO);
		revenue = revenue.add(priceCampingTickets.multiply(nCampingTickets));
		revenue = revenue.add(priceOneDayTickets.multiply(nOneDayTickets));
		return revenue;
	}


	/**
	 * Returns the total revenue due to the festival
	 * @return the total revenue due to the festival
	 */
	public Money getTotalRevenue() {
		return totalRevenue;
	}


	/**
	 * Returns the total cost caused by the festival
	 * @return the total cost caused by the festival
	 */
	public Money getTotalCost() {
		return totalCost;
	}


	/**
	 * A function which computes the profit due to the given cost and revenue
	 * @param revenue the revenue that contributes to the profit
	 * @param cost the cost that contributes to the profit
	 * @return the profit resulting from cost and revenue
	 */
	public static Money getProfit(Money revenue, Money cost) {
		return revenue.subtract(cost);
	}

}
