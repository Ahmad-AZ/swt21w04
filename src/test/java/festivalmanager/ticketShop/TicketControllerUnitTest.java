package festivalmanager.ticketShop;

import festivalmanager.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import static org.assertj.core.api.Assertions.*;

public class TicketControllerUnitTest extends AbstractIntegrationTests {


	@Autowired TicketController TestController;
	@Autowired TicketManagement ticketManagement;



	@Test
	void contextLoads() throws Exception {

		assertThat(TestController).isNotNull();
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

		assertThat(TestController.getTitle()).isEqualTo("Ticketshop");

	}


	@Test
	void rejectsUnauthenticatedAccessToController() {

		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> TestController.ticketOverview(new ExtendedModelMap()));

		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> TestController.update(new Ticket(),new ExtendedModelMap()));


		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> TestController.showTicketInfo(new ExtendedModelMap()));



		assertThatExceptionOfType(AuthenticationException.class).isThrownBy(()
				-> TestController.buyTicket(new Ticket(),new ExtendedModelMap()));




	}

	@Test
	@WithMockUser(roles = {"PLANNER", "ADMIN"})
	void testUpdateTicket(){


		Ticket ticket = new Ticket(0, "Festival", 10,10,TicketType.CAMPING, 10,10);

		Model model = new ExtendedModelMap();

		TestController.update(ticket, model);
		assertThat(model.getAttribute("tickets")).isNotNull();
		assertThat(model.getAttribute("tickets")).isEqualTo(ticket);
		assertThat(ticketManagement.getCurrentTicket()).isEqualTo(ticket);
		assertThat(ticketManagement.getCurrentTicket().getFestivalName()).isEqualTo(ticket.getFestivalName());

		//-------------------------------------------------

		ticket.setFestivalName("anotherFestival");
		TestController.update(ticket, model);
		assertThat(model.getAttribute("tickets")).isEqualTo(ticket);
		assertThat(ticketManagement.getCurrentTicket().getFestivalName()).isEqualTo(ticket.getFestivalName());

	}















}
