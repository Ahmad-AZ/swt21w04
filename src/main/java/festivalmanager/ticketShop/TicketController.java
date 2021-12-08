package festivalmanager.ticketShop;


import com.sun.istack.NotNull;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class TicketController {


	private  TicketManagement ticketManagement;
	private Festival currentFestival;


	public TicketController(TicketManagement ticketManagement) {
		this.ticketManagement = ticketManagement;
		this.currentFestival = null;
	}

	// TODO: @PreAuthorize("hasRole('PLANNER')")
	@GetMapping("/tickets")
	public String showTicketInfo(@ModelAttribute Festival festival, Model model) {

		this.currentFestival = festival;

		System.out.println("current festival in ticket shop " + currentFestival.getName());


		model.addAttribute("ticket", new Ticket());
		model.addAttribute("festival", this.currentFestival);

		return "ticketFrom";
	}


	// TODO: @PreAuthorize("hasRole('PLANNER')")
	@PostMapping("/tickets/create")
	public String newTickets(@ModelAttribute Ticket ticket, RedirectAttributes rd) {

		ticket.setFestivalName(currentFestival.getName());
		ticket.setFestivalId(currentFestival.getId());
		ticketManagement.createTickets(ticket);


		rd.addFlashAttribute("ticket", ticket);

		return "redirect:/ticketShop";
	}


	// TODO: 11/26/2021 check  ticketStock for ticket

	// TODO: @PreAuthorize("hasRole('TicketSeller')")
	@PostMapping("/tickets/buy")
	public String buyTicket( @ModelAttribute Ticket ticket, Model model) {


		int soldTicket;
		double ticketPrice;
		Ticket currentTickets = ticketManagement.allTicketsByFestival(currentFestival.getId());
		if (currentTickets == null) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}


		int currCampingTickets = currentTickets.getDayTicketsCount();
		int currDayTickets = currentTickets.getDayTicketsCount();

		// TODO: 12/7/2021 check for count of tickets

		if (ticket.getDayTicketsCount() == 0) {

			soldTicket = ticket.getCampingTicketsCount();
			currentTickets.setCampingTicketsCount(currCampingTickets - soldTicket);
			ticketPrice = currentTickets.getCampingTicketPrice();


		} else {
			soldTicket = ticket.getDayTicketsCount();
			currentTickets.setCampingTicketsCount(currDayTickets - soldTicket);

			ticketPrice = currentTickets.getDayTicketPrice();

		}

		model.addAttribute("ticketCount", soldTicket);
		model.addAttribute("ticketPrice", ticketPrice);
		model.addAttribute("festival",currentFestival.getName());
		model.addAttribute("tickets", ticket);

		return "ticketPrint";
	}

	// TODO: @PreAuthorize("hasRole('TicketSeller')")
	@GetMapping("/ticketShop")
	public String ticketOverview(@ModelAttribute Ticket ticket, Model model) {


		model.addAttribute("tickets", ticket);

		return "ticketShop";
	}


}
