package festivalmanager.ticketShop;


import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class TicketStock {





	private List<Ticket> ticketList;


	public String getCountOfTickets( ){

		return "";
	}


	public void  addTickets(@NonNull Ticket ticket ){

		ticketList.add(ticket);
	}


	// TODO: 11/13/2021 update ticket quantity for the festival argument
	public void updateQuantity(){

	}


	public Ticket getTicketByFestival(long id ){

		for (Ticket ticket : ticketList) {
			if (ticket.getFestivalId()== id ) {
				return ticket;
			}
		}
		return null;
	}



	public float getSales(){

		return 0;
	}


	public void setMaxNumberOfTickets(){


	}

	public boolean checkValidation(Ticket ticket){

		return true;
	}


	public Ticket buy(Ticket ticket) {

		Ticket nTicket = getTicketByFestival(ticket.getFestivalId());

		if (nTicket != null) {
			return null;
		}
		return null;

	}
}
