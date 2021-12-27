package festivalmanager.ticketShop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TicketControllerUnitTest  {


	@Autowired TicketController ticket;


	@Test
	void contextLoads() throws Exception {
		assertThat(ticket).isNotNull();
	}

	@Test
	@WithMockUser(roles = {"ADMIN", "TICKET_SELLER"})
	void allowsAuthenticatedAccessToController(){

//
//		ExtendedModelMap modelMap= new ExtendedModelMap();
//
//		String response = ticket.ticketOverview(modelMap);
//
//		assertThat("ticketShopUnavailable").isEqualTo(response);


	}





}
