package festivalmanager.ticketShop;


import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.FestivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.classpath.ClassPathRestartStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class TicketManagement {

	@Autowired
	private TicketRepository ticketRepo;

	private FestivalManagement festival;

	private Festival currentFestival;
	private Ticket currentTicket;

	public TicketManagement(FestivalManagement festival) {
		this.festival = festival;
		this.currentFestival = null;
		this.currentTicket=null;
	}

	public Ticket createTickets(@NonNull Ticket ticket) {

		return ticketRepo.save(ticket);
	}


	public Ticket TicketsByFestival(long festivalId) {

		return ticketRepo.findAllByFestivalId(festivalId);
	}


	public  void setFestival(Festival festival ){

		this.currentFestival= festival;
	}

	public void setCurrentTicket(@NotNull Ticket ticket){

		this.currentTicket= ticket;
	}


	public Festival findFestivalById(long id) {

		return festival.findById(id).get();
	}



	// TODO: 12/11/2021 create exceptions

	public boolean checkTickets(Ticket ticket) {


		Ticket nTicket = ticketRepo.findAllByFestivalId(currentFestival.getId());

		if (Objects.isNull(nTicket)) {
			 throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}


		int soldTicket;
		int difference;

		if (ticket.getDayTicketsCount() == 0) {
			int currCampingTickets;

			soldTicket = ticket.getCampingTicketsCount();
			currCampingTickets = nTicket.getCampingTicketsCount();
			difference = currCampingTickets - soldTicket;


			if ( difference > 0 && (nTicket.getSoldCampingTicket() + soldTicket <= currCampingTickets)) {
				nTicket.setSoldCampingTicket(soldTicket);
				this.currentTicket= nTicket;
				return true;
			}


		} else {

			int currDayTickets;
			soldTicket = ticket.getDayTicketsCount();
			currDayTickets= nTicket.getDayTicketsCount();
			difference= currDayTickets - soldTicket;

			if ( difference > 0 && (nTicket.getSoldDayTicket() + soldTicket <= currDayTickets)) {
				nTicket.setSoldDayTicket(soldTicket);
				this.currentTicket= nTicket;
				return true;
			}
		}
	 return false;
	}

	public Ticket buyTickets() {

		 return this.currentTicket;
	}
}
