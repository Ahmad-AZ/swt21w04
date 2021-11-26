package festivalmanager.ticketShop;


import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TicketManagement {


	@Autowired
	final TicketRepository ticketRepository;
	private TicketStock ticketStock;

	public TicketManagement(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}


	public Ticket createTickets(@NonNull Ticket ticket) {


		return ticketRepository.save(ticket);
	}

	public List<Ticket> allTickets(){

		return ticketRepository.findAll();
	}




}
