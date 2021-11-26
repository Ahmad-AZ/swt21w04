package festivalmanager.ticketShop;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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



	// TODO: 11/26/2021 return a ticket implementation
	@PostMapping("/tickets/{ticketType}/{count}/{festivalName}")
	public String buyTicket(@RequestParam TicketType ticketType, @RequestParam int count, @RequestParam String festivalName, Model model){



		return "ticketPrint";
	}


	@GetMapping("/ticket")
	public String ticketOverview(Model model){

		model.addAttribute("tickets",new Ticket() );

		return "ticketShop";
	}







}
