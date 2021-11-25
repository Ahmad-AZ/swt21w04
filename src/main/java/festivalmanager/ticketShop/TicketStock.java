package festivalmanager.ticketShop;


import org.springframework.lang.NonNull;

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


	// TODO: 11/13/2021 add festvial argument
	public float getSales(){

		return 0;
	}

	// TODO: 11/13/2021 add festvial argument
	public void setMaxNumberOfTickets(){


	}

	public boolean checkValidation(Ticket ticket){

		return true;
	}




}
