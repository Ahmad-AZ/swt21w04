package festivalmanager.ticketShop;


import com.google.zxing.WriterException;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.ticketShop.qr_code.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Objects;


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

	public Ticket save(@NonNull Ticket ticket) {
		return ticketRepo.save(ticket);
	}

	public Ticket update(Ticket ticket){
		return  ticketRepo.save(ticket);
	}

	public Ticket getCurrentTicket(){
		return this.currentTicket;
	}

	public Ticket TicketsByFestival(long festivalId) {

		return ticketRepo.findAllByFestivalId(festivalId);
	}


	public  void setFestival(Festival festival){

		this.currentFestival= festival;
	}

	public void setCurrentTicket(@NotNull Ticket ticket){
		if (Objects.isNull(ticketRepo.findAllByFestivalId(ticket.getFestivalId())))
		{
			ticketRepo.save(ticket);
			this.currentTicket = ticketRepo.findAllByFestivalId(ticket.getFestivalId());
		}
		else this.currentTicket=ticket;

	}

	public boolean checkTickets(Ticket ticket) {

		Ticket nTicket = ticketRepo.findAllByFestivalId(ticket.getFestivalId());

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
				this.currentTicket= nTicket;
				return true;
			}
		}
	 return false;
	}


	public Ticket buyTickets()  {

		return this.currentTicket;
	}

}
