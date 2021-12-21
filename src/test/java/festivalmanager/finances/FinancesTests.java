package festivalmanager.finances;

import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.EquipmentRepository;
import festivalmanager.catering.*;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.FestivalRepository;
import festivalmanager.hiring.HiringManagement;
import festivalmanager.location.LocationManagement;
import festivalmanager.location.Location;
import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import festivalmanager.ticketShop.Ticket;
import festivalmanager.ticketShop.TicketManagement;
import festivalmanager.utils.UtilsManagement;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Streamable;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.salespointframework.core.Currencies.EURO;


class FinancesTests {


	@Test
	void testFinancesManagement() {

		FestivalRepository festivalRepository = mock(FestivalRepository.class);
		Festival testFestival = new Festival("TestFestival",
				LocalDate.of(2021, 12, 3),
				LocalDate.of(2021, 12, 6));
		when(festivalRepository.findById(any())).thenReturn(Optional.of(testFestival));

		FestivalManagement festivalManagement = new FestivalManagement(
				festivalRepository,
				mock(LocationManagement.class),
				mock(HiringManagement.class));
		festivalManagement.saveFestival(testFestival);

		Location testLocation = new Location();
		testLocation.setPricePerDay(Money.of(500, EURO));
		testFestival.setLocation(testLocation);
		//Artist testArtist = new Artist();
		//testFestival.addArtist(testArtist);

		UtilsManagement utilsManagement = new UtilsManagement(festivalManagement);
		utilsManagement.setCurrentFestival(testFestival.getId());
		EquipmentRepository equipmentRepository = mock(EquipmentRepository.class);
		when(equipmentRepository.findById(any())).thenReturn(Optional.empty());
		EquipmentManagement equipmentManagement = new EquipmentManagement(equipmentRepository);

		StaffManagement staffManagement = mock(StaffManagement.class);
		Streamable<Person> staffMembers = Streamable.empty();
		when(staffManagement.findByFestivalId(anyLong())).thenReturn(staffMembers);

		Ticket ticketInformation = new Ticket();
		ticketInformation.setCampingTicketPrice(300);
		ticketInformation.setDayTicketPrice(100);
		TicketManagement ticketManagement = mock(TicketManagement.class);
		when(ticketManagement.TicketsByFestival(anyLong())).thenReturn(ticketInformation);

		CateringStock cateringStock = mock(CateringStock.class);
		CateringSales cateringSales = mock(CateringSales.class);
		Streamable<CateringSalesItem> cateringSalesStream = Streamable.empty();
		when(cateringSales.findAll()).thenReturn(cateringSalesStream);
		CateringProductCatalog cateringProductCatalog = mock(CateringProductCatalog.class);
		CateringController cateringController = new CateringController(
				cateringProductCatalog,
				cateringStock,
				cateringSales,
				utilsManagement,
				festivalManagement);


		FinancesManagement financesManagement = new FinancesManagement(
				festivalManagement,
				utilsManagement,
				equipmentManagement,
				staffManagement,
				ticketManagement,
				cateringController);
		FinancesController financesController = new FinancesController(
				financesManagement,
				festivalManagement,
				utilsManagement);
		Model testModel = new ExtendedModelMap();
		financesController.financesPage(testModel);

		assertThat(testModel.getAttribute("artistsCost")).isEqualTo("0.00");
		assertThat(testModel.getAttribute("locationCost")).isEqualTo("2000.00");
		assertThat(testModel.getAttribute("staffCost")).isEqualTo("0.00");
		assertThat(testModel.getAttribute("equipmentCost")).isEqualTo("0.00");
		assertThat(testModel.getAttribute("totalCost")).isEqualTo("2000.00");
		assertThat(testModel.getAttribute("totalRevenue")).isEqualTo("0.00");
		assertThat(testModel.getAttribute("profit")).isEqualTo("-2000.00");

	}


}
