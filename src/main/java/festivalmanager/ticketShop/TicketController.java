package festivalmanager.ticketShop;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;

@Controller

public class TicketController {


	final TicketManagement ticketManagement;


	public TicketController(TicketManagement ticketManagement) {
		this.ticketManagement = ticketManagement;

	}



	// TODO: @PreAuthorize("hasRole('PLANNER')")
	@GetMapping("/tickets")
	public String showTicketInfo(Model model) {



		model.addAttribute("ticket", new Ticket());
		//model.addAttribute("festival", id);
		//System.out.println("festival id ==" + id);
		return "ticketFrom";
	}

	// TODO: @PreAuthorize("hasRole('PLANNER')")
	@PostMapping("/tickets/create")
	public String newTickets(@ModelAttribute Ticket ticket, Model model) {



		ticketManagement.createTickets(ticket);
		model.addAttribute("tickets", ticket);


		return "ticketResult";
	}


	// TODO: 11/26/2021 check  ticketStock for ticket

	// TODO: @PreAuthorize("hasRole('TicketSeller')")
	@PostMapping("/tickets/buy")
	public String buyTicket(@ModelAttribute Ticket ticket, Model model) {

		model.addAttribute("tickets", ticket);

		int ticketCount;
		double ticketPrice;
		if (ticket.getDayTicketsCount() == 0) {
			ticketCount = ticket.getCampingTicketsCount();
			ticketPrice = ticket.getCampingTicketPrice();
		} else {
			ticketCount = ticket.getDayTicketsCount();
			ticketPrice = ticket.getDayTicketPrice();
		}

		model.addAttribute("ticketCount", ticketCount);
		model.addAttribute("ticketPrice", ticketPrice);


		return "ticketPrint";
	}

	// TODO: @PreAuthorize("hasRole('TicketSeller')")
	@GetMapping("/ticketShop")
	public String ticketOverview(Model model) {


		model.addAttribute("tickets", new Ticket());

		return "ticketShop";
	}


}
