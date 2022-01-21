package festivalmanager.finances;


import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.EquipmentRepository;
import festivalmanager.Equipment.StageRepository;
import festivalmanager.catering.*;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.FestivalRepository;
import festivalmanager.hiring.Artist;
import festivalmanager.hiring.HiringManagement;
import festivalmanager.location.LocationManagement;
import festivalmanager.location.Location;
import festivalmanager.messaging.MessageManagement;
import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import festivalmanager.ticketShop.Ticket;
import festivalmanager.ticketShop.TicketManagement;
import festivalmanager.utils.UtilsManagement;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Streamable;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.salespointframework.core.Currencies.EURO;


class FinancesTests {


	FestivalManagement festivalManagement;
	FinancesManagement financesManagement;
	UtilsManagement utilsManagement;
	FestivalRepository festivalRepository;


	@BeforeEach
	void setUp() {

		festivalRepository = mock(FestivalRepository.class);
		festivalManagement = new FestivalManagement(
				festivalRepository,
				mock(LocationManagement.class),
				mock(HiringManagement.class));

		utilsManagement = new UtilsManagement(festivalManagement);

		CateringStock cateringStock = mock(CateringStock.class);
		CateringSales cateringSales = mock(CateringSales.class);
		Streamable<CateringSalesItem> cateringSalesStream = Streamable.empty();
		when(cateringSales.findAll()).thenReturn(cateringSalesStream);
		CateringProductCatalog cateringProductCatalog = mock(CateringProductCatalog.class);
		MessageManagement messageManagement = mock(MessageManagement.class);
		CateringController cateringController = new CateringController(
				cateringProductCatalog,
				cateringStock,
				cateringSales,
				utilsManagement,
				festivalManagement,
				messageManagement);

		Ticket ticketInformation = new Ticket();
		ticketInformation.setCampingTicketPrice(300);
		ticketInformation.setDayTicketPrice(100);
		TicketManagement ticketManagement = mock(TicketManagement.class);
		when(ticketManagement.TicketsByFestival(anyLong())).thenReturn(ticketInformation);

		EquipmentRepository equipmentRepository = mock(EquipmentRepository.class);
		when(equipmentRepository.findById(any())).thenReturn(Optional.empty());
		EquipmentManagement equipmentManagement = new EquipmentManagement(equipmentRepository, mock(StageRepository.class));

		StaffManagement staffManagement = mock(StaffManagement.class);
		Streamable<Person> staffMembers = Streamable.empty();
		when(staffManagement.findByFestivalId(anyLong())).thenReturn(staffMembers);

		financesManagement = new FinancesManagement(
				festivalManagement,
				equipmentManagement,
				staffManagement,
				ticketManagement,
				cateringController);
	}


	@Test
	void testFinancesManagement() {

		Festival testFestival = new Festival("Test Festival",
				LocalDate.of(2021, 12, 3),
				LocalDate.of(2021, 12, 6));

		when(festivalRepository.findById(any())).thenReturn(Optional.of(testFestival));
		festivalManagement.saveFestival(testFestival);

		Location testLocation = new Location();
		testLocation.setPricePerDay(Money.of(500, EURO));
		Artist testArtist = new Artist("Test Artist", Money.of(500, EURO), 0);
		testFestival.setLocation(testLocation);
		testFestival.addArtist(testArtist);

		FinancesController financesController = new FinancesController(
				financesManagement,
				festivalManagement,
				utilsManagement);

		Model testModel = new ExtendedModelMap();
		financesController.financesPage(testModel, testFestival.getId());

		assertThat(testModel.getAttribute("artistsCost")).isEqualTo("2000.00");
		assertThat(testModel.getAttribute("locationCost")).isEqualTo("2000.00");
		assertThat(testModel.getAttribute("staffCost")).isEqualTo("0.00");
		assertThat(testModel.getAttribute("equipmentCost")).isEqualTo("0.00");
		assertThat(testModel.getAttribute("totalCost")).isEqualTo("4000.00");
		assertThat(testModel.getAttribute("totalRevenue")).isEqualTo("0.00");
		assertThat(testModel.getAttribute("profit")).isEqualTo("-4000.00");
	}


	@Test
	void testFinancesCompanyManagement() {

		Festival testFestival1 = new Festival("Test Festival 1",
				LocalDate.of(2022, 9, 10),
				LocalDate.of(2022, 9, 12));

		Festival testFestival2 = new Festival("Test Festival 2",
				LocalDate.of(2022, 9, 15),
				LocalDate.of(2022, 9, 17));

		long testFestival1Id = testFestival1.getId();
		long testFestival2Id = testFestival2.getId();
		when(festivalRepository.findById(testFestival1Id)).thenReturn(Optional.of(testFestival1));
		when(festivalRepository.findById(testFestival2Id)).thenReturn(Optional.of(testFestival2));
		festivalManagement.saveFestival(testFestival1);
		festivalManagement.saveFestival(testFestival2);

		Collection<Festival> festivalList = new ArrayList<>();
		festivalList.add(testFestival1);
		festivalList.add(testFestival2);
		when(festivalRepository.findAll()).thenReturn(Streamable.of(festivalList));

		FinancesCompanyManagement financesCompanyManagement =
				new FinancesCompanyManagement(financesManagement, festivalManagement);
		FinancesCompanyController financesCompanyController =
				new FinancesCompanyController(financesCompanyManagement);

		Location testLocation = new Location();
		testLocation.setPricePerDay(Money.of(500, EURO));
		testFestival1.setLocation(testLocation);
		testFestival2.setLocation(testLocation);

		Model testModel = new ExtendedModelMap();
		financesCompanyController.financesPage(testModel);

		assertThat(testModel.getAttribute("nFestivals")).isEqualTo(2L);
		assertThat(testModel.getAttribute("averageCost")).isEqualTo("1500.00");
		assertThat(testModel.getAttribute("totalCost")).isEqualTo("3000.00");
		assertThat(testModel.getAttribute("averageRevenue")).isEqualTo("0.00");
		assertThat(testModel.getAttribute("totalRevenue")).isEqualTo("0.00");
		assertThat(testModel.getAttribute("averageProfit")).isEqualTo("-1500.00");
		assertThat(testModel.getAttribute("totalProfit")).isEqualTo("-3000.00");
	}


}
