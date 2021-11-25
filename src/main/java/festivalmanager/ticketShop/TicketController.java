package festivalmanager.ticketShop;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tickets")
public class TicketController {



	final TicketManagement ticketManagement;


	public TicketController(TicketManagement ticketManagement) {
		this.ticketManagement = ticketManagement;

	}



	@PostMapping("/tickets/create")
	public String newTickets( Ticket ticket){

		ticketManagement.createTickets(ticket);

		return "";
	}







}
