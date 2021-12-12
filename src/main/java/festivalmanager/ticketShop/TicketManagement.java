package festivalmanager.ticketShop;


import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.FestivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.classpath.ClassPathRestartStrategy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TicketManagement {

	@Autowired
	private TicketRepository ticketRepo;

	private FestivalManagement festival;

	private Festival currentFestival;


	public TicketManagement(FestivalManagement festival) {
		this.festival = festival;
		this.currentFestival = null;
	}

	public Ticket createTickets(@NonNull Ticket ticket) {


		return ticketRepo.save(ticket);
	}


	public Ticket allTicketsByFestival(long festivalId) {

		return ticketRepo.findAllByFestivalId(festivalId);
	}




	public Festival findFestivalById(long id) {

		return festival.findById(id).get();
	}


}
