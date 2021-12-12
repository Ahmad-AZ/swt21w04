package festivalmanager.ticketShop;


import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.utils.UtilsManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class TicketController {


	private final TicketManagement ticketManagement;
	private Festival currentFestival;

	private final FestivalManagement festivalManagement;
	private UtilsManagement utilsManagement;


	public TicketController(TicketManagement ticketManagement,
							UtilsManagement utilsManagement,
							FestivalManagement festivalManagement) {
		this.ticketManagement = ticketManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement= utilsManagement;
		this.currentFestival = null;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Ticketshop";
	}

	// TODO: @PreAuthorize("hasRole('PLANNER')")
	@GetMapping("/tickets")
	public String showTicketInfo(Model model) {

		this.currentFestival =festivalManagement.findById(utilsManagement.getCurrentFestivalId()).get();


		model.addAttribute("ticket", new Ticket());
		model.addAttribute("festival", this.currentFestival);

		utilsManagement.setCurrentPageLowerHeader("tickets");

		utilsManagement.prepareModel(model);
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

		utilsManagement.prepareModel(model);
		return "ticketShop";
	}


}
