package festivalmanager.ticketShop;

import festivalmanager.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Streamable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;



public class TicketManagementUnitTest extends AbstractIntegrationTests {


	@Autowired
	TicketManagement ticketManagement;
	@MockBean
	TicketRepository repository;


	@Test
	void returnedTicketTest() {

		Ticket ticket = new Ticket(10, 10, TicketType.DAY_TICKET, 10, 10);

		ticket.setFestivalId(0);
		ticket.setFestivalName("TestFestival");

		when(repository.save(ticket)).thenReturn(ticket);
		//when(repository.findAllByFestivalId(ticket.getFestivalId())).thenReturn(ticket);
		when(repository.findByFestivalId(ticket.getFestivalId())).thenReturn(Streamable.of(ticket));

		ticketManagement.setCurrentTicket(ticket);

		assertThat(ticketManagement.getCurrentTicket()).isEqualTo(ticket);
		assertThat(ticketManagement.getCurrentTicket().getFestivalName()).isEqualTo(ticket.getFestivalName());
		assertThat(ticketManagement.getCurrentTicket().getFestivalId()).isEqualTo(ticket.getFestivalId());

	}


}