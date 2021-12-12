package festivalmanager.ticketShop;


import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.utils.CurrentPageManagement;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class TicketController {


	private final TicketManagement ticketManagement;
	private Festival currentFestival;
	private CurrentPageManagement currentPageManagement;


	public TicketController(TicketManagement ticketManagement,
							FestivalManagement festivalManagement,
							CurrentPageManagement currentPageManagement) {
		this.ticketManagement = ticketManagement;
		this.currentPageManagement = currentPageManagement;
		this.currentFestival = null;
	}


	// TODO: @PreAuthorize("hasRole('PLANNER')")
	@GetMapping("/tickets")
	public String showTicketInfo(Festival festival,Model model) {

		this.currentFestival =festival;


		model.addAttribute("ticket", new Ticket());
		model.addAttribute("festival", this.currentFestival);

		currentPageManagement.updateCurrentPage(model,"tickets");
		return "ticketFrom";
	}


	// TODO: @PreAuthorize("hasRole('PLANNER')")
	@PostMapping("/tickets")
	public String newTickets(@ModelAttribute Ticket ticket, RedirectAttributes rd) {


		ticketManagement.setFestival(currentFestival);



		ticket.setFestivalName(currentFestival.getName());
		ticket.setFestivalId(currentFestival.getId());



		ticketManagement.createTickets(ticket);

		rd.addFlashAttribute("ticket", ticket);
		return "redirect:/ticketShop";
	}





	// TODO: @PreAuthorize("hasRole('TicketSeller')")
	@PostMapping("/tickets/buy")
	public String buyTicket( @ModelAttribute Ticket ticket, Model model) {

		Ticket nTicket;
		if (ticketManagement.checkTickets(ticket)){

			nTicket = ticketManagement.buyTickets();


			int soldTicket;
			double ticketPrice;
			if (ticket.getDayTicketsCount() == 0) {
				soldTicket = ticket.getCampingTicketsCount();
				ticketPrice = nTicket.getCampingTicketPrice();
			} else {
				soldTicket = ticket.getDayTicketsCount();
				ticketPrice = nTicket.getDayTicketPrice();
			}

			model.addAttribute("ticketCount", soldTicket);
			model.addAttribute("ticketPrice", ticketPrice*soldTicket);
			model.addAttribute("festival",currentFestival.getName());
			model.addAttribute("tickets", ticket);


		};

		return "ticketPrint";
	}


	// TODO: @PreAuthorize("hasRole('TicketSeller')")
	@GetMapping("/ticketShop")
	public String ticketOverview(@ModelAttribute Ticket ticket, Model model) {


		model.addAttribute("tickets", ticket);

		currentPageManagement.updateCurrentPage(model,"ticketShop");
		return "ticketShop";
	}


}
