package festivalmanager.ticketShop;

import festivalmanager.AbstractIntegrationTests;
import festivalmanager.utils.UtilsManagement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import static org.assertj.core.api.Assertions.*;



public class TicketControllerUnitTest extends AbstractIntegrationTests {


	@Autowired TicketController testController;
	@Autowired TicketManagement ticketManagement;
	@Autowired UtilsManagement utilsManagement;





	@Test
	void contextLoads() throws Exception {

		assertThat(testController).isNotNull();
	}

//	@Test
//	@WithMockUser(roles = {"PLANNER", "ADMIN"})
//	void allowsAuthenticatedAccessToController(){
//		ticketManagement.setFestivalById(0);
//
//		assertThat(ticketManagement.getCurrentTicket()).isNull();
//
//	}

	@Test
	void attributesTesting(){

		assertThat(testController.getTitle()).isEqualTo("Ticketshop");

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


		Ticket ticket = new Ticket(0, "Festival", 10,10,TicketType.CAMPING, 10,10);

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
		Ticket ticket = new Ticket(1, "Festival", 10,10,TicketType.CAMPING, 10,10);
		ticketManagement.setCurrentTicket(ticket);
		Model model = new ExtendedModelMap();
		
		String result =  testController.ticketOverview(model);
		assertThat(result).isEqualTo("ticketShop");
	}

















}
