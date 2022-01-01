package festivalmanager.ticketShop;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.utils.UtilsManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TicketControllerUnitTest extends AbstractIntegrationTests {




	@Autowired TicketController testController;
	@Autowired TicketManagement ticketManagement;
	@Autowired UtilsManagement utilsManagement;
	@MockBean TicketRepository repository;
	Model model;


	@BeforeEach
	void set(){

		this.model = new ExtendedModelMap();
	}


	@Test
	void contextLoads() throws Exception {

		assertThat(testController).isNotNull();
	}

	@Test
	void rejectsUnauthenticatedAccessToController() {

		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> testController.ticketOverview(new ExtendedModelMap()));

		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> testController.update(new Ticket(),new ExtendedModelMap()));


		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> testController.showTicketInfo(new ExtendedModelMap()));



		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> testController.buyTicket(new Ticket(),new ExtendedModelMap()));




	}

	@Test
	@WithMockUser(roles = {"PLANNER", "ADMIN"})
	void testUpdateTicket(){


		Ticket ticket = new Ticket( 10,10,TicketType.CAMPING, 10,10);
		ticket.setFestivalId(0);
		ticket.setFestivalName("Festival");

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

		utilsManagement.setCurrentFestival(1);
		Model model = new ExtendedModelMap();
		String result =  testController.ticketOverview(model);
		assertThat(result).isEqualTo("ticketShopUnavailable");
	}


	@Test
	@WithMockUser(roles = {"TICKET_SELLER", "ADMIN"})
	void testReturnedViewInTicketOverviewWithCreatedTickets(){

		utilsManagement.setCurrentFestival(1);
		Ticket ticket = new Ticket( 1, "Festival",10,10,TicketType.CAMPING, 10,10);

		ticketManagement.setCurrentTicket(ticket);
		Model model = new ExtendedModelMap();
		String result =  testController.ticketOverview(model);
		assertThat(result).isEqualTo("ticketShop");
	}





/*
	@Test
	@WithMockUser(roles = {"PLANNER", "ADMIN"})
	void testTicketInfoForNonCreatedTickets(){
		utilsManagement.setCurrentFestival(1);
		String result = testController.showTicketInfo(model);
		assertThat(result).isEqualTo("ticketForm");
	}
*/


	@Test
	@WithMockUser(roles = {"PLANNER", "ADMIN"})
	void testTicketInfoForCreatedTickets(){
		Ticket ticket = new Ticket(10,10,TicketType.CAMPING, 10,10);

		ticket.setFestivalId(1);
		ticket.setFestivalName("Beispielfestival");

		utilsManagement.setCurrentFestival(1);
		ticketManagement.setCurrentTicket(ticket);

		String result = testController.showTicketInfo(model);
		assertThat(result).isEqualTo("ticketResult");
	}



	@Test
	@WithMockUser(roles = {"PLANNER", "ADMIN"})
	void testCreatingTicket(){
		Ticket ticket = new Ticket( 10,10,TicketType.CAMPING, 10,10);

		utilsManagement.setCurrentFestival(1);


		assertThat(testController.create(ticket,model)).isEqualTo("ticketResult");
		assertThat(ticket.getFestivalName()).isEqualTo("Beispielfestival");
		assertThat(ticket.getFestivalId()).isEqualTo(1);
		assertThat(ticketManagement.getCurrentTicket()).isEqualTo(ticket);

	}


	@Test
	@WithMockUser(roles = {"TICKET_SELLER", "ADMIN"})
	void sellingTicketWithDifferentValues(){

		Ticket currentTicket = new Ticket(1,"Beispielfestival",2,
				2,TicketType.DAY_TICKET,10,10);

		//-----------------------------------------------------
		when(repository.save(currentTicket)).thenReturn(currentTicket);
		when(repository.findAllByFestivalId(currentTicket.getFestivalId())).thenReturn(currentTicket);
		//--------------------------------------------------------

		ticketManagement.setCurrentTicket(currentTicket);
		Ticket twoTicketsSold = new Ticket(1,"Beispielfestival",0,
				2,TicketType.CAMPING,0,0);

		testController.buyTicket(twoTicketsSold, model);

		assertThat(twoTicketsSold.getCampingTicketsCount()).isEqualTo(currentTicket.getSoldCampingTicket());

		Ticket fiveTicketsSold = new Ticket(1,"Beispielfestival",0,
				5,TicketType.CAMPING,0,0);

		assertThat(testController.buyTicket(fiveTicketsSold, model)).isEqualTo("ticketShopUnavailable");



	}












}
