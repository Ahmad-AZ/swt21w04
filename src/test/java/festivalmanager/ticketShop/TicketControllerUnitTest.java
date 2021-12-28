package festivalmanager.ticketShop;

import festivalmanager.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.AbstractBindingResult;


import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class TicketControllerUnitTest extends AbstractIntegrationTests {


	@Autowired TicketController controller;
	@Autowired TicketManagement ticketManagement;



	@Test
	void contextLoads() throws Exception {

		assertThat(controller).isNotNull();
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

		assertThat(controller.getTitle()).isEqualTo("Ticketshop");

	}



	@Test
	@WithMockUser(roles = {"PLANNER", "ADMIN"})
	void testUpdateTicket(){


		Ticket ticket = new Ticket(0, "Festival", 10,10,TicketType.CAMPING, 10,10);
		Ticket emptyTicket = new Ticket();

		Model model = new ExtendedModelMap();


		controller.update(ticket, model);
		assertThat(model.getAttribute("tickets")).isNotNull();
		assertThat(ticketManagement.getCurrentTicket()).isEqualTo(ticket);
		assertThat(ticketManagement.getCurrentTicket().getFestivalName()).isEqualTo(ticket.getFestivalName());


		controller.update(emptyTicket, model );
		assertThat(ticketManagement.getCurrentTicket()).isNotEqualTo(ticket);
		assertThat(ticketManagement.getCurrentTicket().getFestivalName()).isNull();


	}


}
