package festivalmanager.ticketShop;



import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.constraints.NotNull;
import java.util.Objects;


/**
 * Implementation of business logic related to {@link Ticket}.
 *
 * @author Ahmad Abu Zahra
 */
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

	/**
	 * Used to save {@link Ticket} in the {@link TicketRepository}
	 * @param ticket
	 * the new instance of   {@link Ticket}
	 * @return the saved ticket
	 */
	public Ticket save(@NonNull Ticket ticket) {
		Ticket currentTicketForFestival = TicketsByFestival(ticket.getFestivalId());
		if (currentTicketForFestival != null && !ticket.equals(currentTicketForFestival)) {
			ticketRepo.delete(currentTicketForFestival);
			return ticketRepo.save(ticket);
		}
		else {
			return ticketRepo.save(ticket);
		}
	}

	/**
	 * Used to update the saved  {@link Ticket} and if it's not found will add a new  {@link Ticket}
	 * @param ticket
	 * the updated Ticket
	 * @return
	 * the saved ticket
	 */
	public Ticket update(Ticket ticket){
		return  ticketRepo.save(ticket);
	}

	/**
	 *
	 * @return the current  {@link Ticket} for the current  {@link Festival}
	 */
	public Ticket getCurrentTicket(){
		return this.currentTicket;
	}

	/**
	 * Used to find  {@link Ticket} by festival id using  {@link TicketRepository}
	 * @param festivalId
	 * id of  the current  {@link Festival}
	 * @return
	 * the found {@link Ticket} by festival id.
	 */
	public Ticket TicketsByFestival(long festivalId) {

		Streamable<Ticket> ticketStreamable = ticketRepo.findByFestivalId(festivalId);
		if (ticketStreamable.iterator().hasNext()) {
			return ticketStreamable.iterator().next();
		}
		return null;
	}


	/**
	 * Used to set the current  {@link Festival}.
	 * @param festival must not be {@literal null}.
	 *
	 */
	public  void setFestival(Festival festival){

		this.currentFestival= festival;
	}

	/**
	 * Used to set the current Ticket if it's already saved in {@link TicketRepository} otherwise will save it.
	 * @param ticket must not be {@literal null}.
	 *

	 */
	public void setCurrentTicket(@NotNull Ticket ticket){
		if (Objects.isNull(TicketsByFestival(ticket.getFestivalId()))) {
			save(ticket);
			this.currentTicket = TicketsByFestival(ticket.getFestivalId());
		} else {
			this.currentTicket=ticket;
		}

	}

	/**
	 * Used to check if the current {@link Ticket} have  enough Tickets for selling
	 * @param ticket
	 * ticket instance with ticket count to be sold
	 * @return true / false
	 */
	public boolean checkTickets(Ticket ticket) {

		Ticket nTicket = TicketsByFestival(ticket.getFestivalId());

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


			if ( difference >= 0 && (nTicket.getSoldCampingTicket() + soldTicket <= currCampingTickets)) {
				nTicket.setSoldCampingTicket(soldTicket);
				nTicket.setCampingTicketsCount(currCampingTickets - soldTicket);
				this.currentTicket= nTicket;
				return true;
			}


		} else {

			int currDayTickets;
			soldTicket = ticket.getDayTicketsCount();
			currDayTickets= nTicket.getDayTicketsCount();
			difference= currDayTickets - soldTicket;

			if ( difference >= 0 && (nTicket.getSoldDayTicket() + soldTicket <= currDayTickets)) {
				nTicket.setSoldDayTicket(soldTicket);
				nTicket.setDayTicketsCount(currDayTickets- soldTicket);
				this.currentTicket= nTicket;
				return true;
			}
		}
	 return false;
	}


	/**
	 * Used after calling checkTickets method for selling Ticket
	 * @return
	 * the sold {@link Ticket}
	 */
	public Ticket buyTickets()  {

		return this.currentTicket;
	}

}
