package festivalmanager.ticketShop;

import com.google.zxing.WriterException;
import festivalmanager.AbstractIntegrationTests;
import festivalmanager.utils.UtilsManagement;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;



public class TicketControllerUnitTest extends AbstractIntegrationTests {




	@Autowired TicketController testController;
	@Autowired TicketManagement ticketManagement;
	@Autowired UtilsManagement utilsManagement;




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

		utilsManagement.setCurrentFestival(1);
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
		Ticket ticket = new Ticket(10,10,TicketType.CAMPING, 10,10);

		Model model = new ExtendedModelMap();
		ticket.setFestivalId(1);
		ticket.setFestivalName("Beispielfestival");

		utilsManagement.setCurrentFestival(1);
		ticketManagement.setCurrentTicket(ticket);

		String result = testController.showTicketInfo(ticket.getFestivalId(), model);

		assertThat(result).isEqualTo("ticketResult");
	}



	@Test
	@WithMockUser(roles = {"PLANNER", "ADMIN"})
	void testCreatingTicket(){
		Ticket ticket = new Ticket( 10,10,TicketType.CAMPING, 10,10);

		utilsManagement.setCurrentFestival(1);
		Model model = new ExtendedModelMap();


		assertThat(testController.create(ticket,ticket.getFestivalId(), model)).isEqualTo("ticketResult");
		assertThat(ticket.getFestivalName()).isEqualTo("Beispielfestival");
		assertThat(ticket.getFestivalId()).isEqualTo(1);
		assertThat(ticketManagement.getCurrentTicket()).isEqualTo(ticket);

	}




	@Test
	@WithMockUser(roles = {"TICKET_SELLER", "ADMIN"})

		// TODO: 1/4/2022  the ticket id is null check why is that and convert it to byte array and then to qr code in frontend
	void sellingTicketWithDifferentValues() {

		Model model = new ExtendedModelMap();
		Ticket currentTicket = new Ticket(1,"Beispielfestival",2,
				2,TicketType.DAY_TICKET,10,10);

		ticketManagement.setCurrentTicket(currentTicket);
		utilsManagement.setCurrentFestival(1);
		testController.setCurrentFestival();

		Ticket twoTicketsSold = new Ticket(1,"Beispielfestival",0,
				2,TicketType.CAMPING,0,0);

		testController.buyTicket(twoTicketsSold, model);
		assertThat(twoTicketsSold.getCampingTicketsCount()).isEqualTo(currentTicket.getSoldCampingTicket());


		Ticket fiveTicketsSold = new Ticket(1,"Beispielfestival",0,
				5,TicketType.CAMPING,0,0);

		assertThat(testController.buyTicket(fiveTicketsSold, model)).isEqualTo("ticketShopUnavailable");


	}











}
