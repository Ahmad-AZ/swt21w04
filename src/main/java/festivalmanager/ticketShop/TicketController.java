package festivalmanager.ticketShop;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class TicketController {



	final TicketManagement ticketManagement;


	public TicketController(TicketManagement ticketManagement) {
		this.ticketManagement = ticketManagement;

	}



	@RequestMapping("/tickets")
	public String showTicketInfo(Model model){

	 	model.addAttribute("ticket", new Ticket());
	 	return "ticketFrom";
	}

	@PostMapping("/tickets/create")
	public String newTickets(@ModelAttribute Ticket ticket, Model model){

		ticketManagement.createTickets(ticket);
		model.addAttribute("tickets", ticket);

		return "ticketResult";
	}







}
