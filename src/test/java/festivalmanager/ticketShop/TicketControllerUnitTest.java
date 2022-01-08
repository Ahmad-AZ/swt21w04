package festivalmanager.ticketShop;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.utils.UtilsManagement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;



public class TicketControllerUnitTest extends AbstractIntegrationTests {




	@Autowired TicketController testController;
	@Autowired TicketManagement ticketManagement;
	@Autowired UtilsManagement utilsManagement;
	@Autowired FestivalManagement festivalManagement;



	@Test
	void contextLoads() throws Exception {

		assertThat(testController).isNotNull();
	}


	@Test
	void rejectsUnauthenticatedAccessToController() {

		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> testController.ticketOverview(0, new ExtendedModelMap()));

		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> testController.update(new Ticket(),new ExtendedModelMap()));


		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> testController.showTicketInfo(0, new ExtendedModelMap()));


		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> testController.buyTicket(new Ticket(),new ExtendedModelMap()));

	}

	@Test
	@WithMockUser(roles = {"PLANNER", "ADMIN"})
	void testUpdateTicket(){


		Ticket ticket = new Ticket( 10,10,TicketType.CAMPING, 10,10);
		ticket.setFestivalId(0);
		ticket.setFestivalName("Festival");

		Model model = new ExtendedModelMap();

		testController.update(ticket, model);
		assertThat(model.getAttribute("tickets")).isNotNull();
		assertThat(model.getAttribute("tickets")).isEqualTo(ticket);
		assertThat(ticketManagement.getCurrentTicket()).isEqualTo(ticket);
		assertThat(ticketManagement.getCurrentTicket().getFestivalName()).isEqualTo(ticket.getFestivalName());

		//-------------------------------------------------

		ticket.setFestivalName("anotherFestival");
		testController.update(ticket, model);
		assertThat(model.getAttribute("tickets")).isEqualTo(ticket);
		assertThat(ticketManagement.getCurrentTicket().getFestivalName()).isEqualTo(ticket.getFestivalName());

	}



	@Test
	@WithMockUser(roles = {"TICKET_SELLER", "ADMIN"})
	void testReturnedViewInTicketOverviewWithNoTickets(){


		Model model = new ExtendedModelMap();
		String result =  testController.ticketOverview(0, model);
		assertThat(result).isEqualTo("ticketShopUnavailable");
	}


	@Test
	@WithMockUser(roles = {"TICKET_SELLER", "ADMIN"})
	void testReturnedViewInTicketOverviewWithCreatedTickets(){

		Ticket ticket = new Ticket( 10,10,TicketType.CAMPING, 10,10);
		ticket.setFestivalId(1);
		ticket.setFestivalName("Festival");


		ticketManagement.setCurrentTicket(ticket);
		Model model = new ExtendedModelMap();

		String result =  testController.ticketOverview(ticket.getFestivalId(), model);
		assertThat(result).isEqualTo("ticketShop");
	}


	@Test
	@WithMockUser(roles = {"PLANNER", "ADMIN"})
	void testTicketInfoForCreatedTickets(){

		Model model = new ExtendedModelMap();

		Festival festival = new Festival("NewFestival",
				LocalDate.of(2022, 1, 10),
				LocalDate.of(2022, 1, 20));
		festivalManagement.saveFestival(festival);


		Ticket ticket = new Ticket(10,10,TicketType.CAMPING, 10,10);
		ticket.setFestivalId(festival.getId());
		ticket.setFestivalName(festival.getName());

		ticketManagement.setCurrentTicket(ticket);
		String result = testController.showTicketInfo(festival.getId(), model);
		assertThat(result).isEqualTo("ticketResult");
	}



	@Test
	@WithMockUser(roles = {"PLANNER", "ADMIN"})
	void testCreatingTicket(){
		Model model = new ExtendedModelMap();
		Festival festival = new Festival("NewFestival",
				LocalDate.of(2022, 1, 10),
				LocalDate.of(2022, 1, 20));
		festivalManagement.saveFestival(festival);

		Ticket ticket = new Ticket( 10,10,TicketType.CAMPING, 10,10);

		assertThat(testController.create(ticket,festival.getId(), model)).isEqualTo("ticketResult");
		assertThat(ticket.getFestivalName()).isEqualTo("NewFestival");
		assertThat(ticket.getFestivalId()).isEqualTo(festival.getId());
		assertThat(ticketManagement.getCurrentTicket()).isEqualTo(ticket);

	}




	@Test
	@WithMockUser(roles = {"TICKET_SELLER", "ADMIN"})
	void sellingTicketWithDifferentValues() {

		Festival festival = new Festival("NewFestival",
				LocalDate.of(2022, 1, 10),
				LocalDate.of(2022, 1, 20));

		festivalManagement.saveFestival(festival);

		Model model = new ExtendedModelMap();
		Ticket currentTicket = new Ticket(festival.getId(),festival.getName(),2,
				2,TicketType.DAY_TICKET,10,10);

		ticketManagement.setCurrentTicket(currentTicket);

		testController.setCurrentFestival(festival.getId());

		Ticket twoTicketsSold = new Ticket(festival.getId(),festival.getName(),0,
				2,TicketType.CAMPING,0,0);

		testController.buyTicket(twoTicketsSold, model);
		assertThat(twoTicketsSold.getCampingTicketsCount()).isEqualTo(currentTicket.getSoldCampingTicket());


		Ticket fiveTicketsSold = new Ticket(festival.getId(),festival.getName(),0,
				5,TicketType.CAMPING,0,0);
		assertThat(testController.buyTicket(fiveTicketsSold, model)).isEqualTo("ticketShopUnavailable");



		currentTicket.setDayTicketsCount(300);
		currentTicket.setCampingTicketsCount(300);
		currentTicket.setSoldDayTicket(-currentTicket.getSoldDayTicket());
		currentTicket.setSoldCampingTicket(-currentTicket.getSoldCampingTicket());

		Ticket twoHundredTickets= new Ticket(festival.getId(),festival.getName(),0,
				200
				,TicketType.CAMPING,0,0);
		Ticket oneHundredTickets= new Ticket(festival.getId(),festival.getName(),100,
				0
				,TicketType.DAY_TICKET,0,0);

		testController.buyTicket(twoHundredTickets, model);
		testController.buyTicket(oneHundredTickets, model);
		assertThat(twoHundredTickets.getCampingTicketsCount()).isEqualTo(currentTicket.getSoldCampingTicket());
		assertThat(oneHundredTickets.getDayTicketsCount()).isEqualTo(currentTicket.getSoldDayTicket());




	}


















}
