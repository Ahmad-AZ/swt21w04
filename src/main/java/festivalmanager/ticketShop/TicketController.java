package festivalmanager.ticketShop;


import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.utils.UtilsManagement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.util.Objects;

@Controller
public class TicketController {


	private final TicketManagement ticketManagement;
	private Festival currentFestival;
	private final FestivalManagement festivalManagement;
	private UtilsManagement utilsManagement;


	public TicketController(TicketManagement ticketManagement, UtilsManagement utilsManagement, FestivalManagement festivalManagement) {
		this.ticketManagement = ticketManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
		this.currentFestival = null;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Ticketshop";
	}

	@PreAuthorize("hasRole('PLANNER')||hasRole('ADMIN')")
	@GetMapping("/tickets")
	public String showTicketInfo(Model model) {

		model.addAttribute("title", "Tickets");
		utilsManagement.setCurrentPageLowerHeader("tickets");
		utilsManagement.prepareModel(model);

		if (Objects.isNull(ticketManagement.getCurrentTicket())) {

			this.currentFestival = festivalManagement.findById(utilsManagement.getCurrentFestivalId()).get();
			model.addAttribute("ticket", new Ticket());
			model.addAttribute("festival", this.currentFestival);

			utilsManagement.setCurrentPageLowerHeader("tickets");

			utilsManagement.prepareModel(model);
			return "ticketForm";
		}

		model.addAttribute("tickets", ticketManagement.getCurrentTicket());
		return "ticketResult";
	}






	@PreAuthorize("hasRole('PLANNER')||hasRole('ADMIN')")
	@PostMapping("/tickets")
	public String create(@ModelAttribute Ticket ticket, Model model) {

		model.addAttribute("title", "Tickets");
		utilsManagement.prepareModel(model);

		currentFestival= festivalManagement.findById(utilsManagement.getCurrentFestivalId()).get();

		ticket.setFestivalName(currentFestival.getName());
		ticket.setFestivalId(currentFestival.getId());

		ticketManagement.setCurrentTicket(ticket);
		ticketManagement.setFestival(currentFestival);


		model.addAttribute("tickets", ticketManagement.save(ticket));
		return "ticketResult";
	}


	@PreAuthorize("hasRole('TICKET_SELLER')||hasRole('ADMIN')")
	@PostMapping("/tickets/buy")
	public String buyTicket(@ModelAttribute Ticket ticket, Model model) {

		Ticket nTicket;
		ticket.setFestivalName(currentFestival.getName());
		ticket.setFestivalId(currentFestival.getId());
		if (ticketManagement.checkTickets(ticket)) {

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
			model.addAttribute("ticketPrice", ticketPrice * soldTicket);
			model.addAttribute("festival", currentFestival.getName());
			model.addAttribute("tickets", ticket);

		} else {
			utilsManagement.prepareModel(model);
			model.addAttribute("ticketsUnavailable", "true");
			return "ticketShopUnavailable";
		}

		utilsManagement.prepareModel(model);

		return "ticketPrint";
	}





	@PreAuthorize("hasRole('TICKET_SELLER')||hasRole('ADMIN')")
	@GetMapping("/ticketShop")
	public String ticketOverview(Model model) {


		Ticket ticket = ticketManagement.TicketsByFestival(utilsManagement.getCurrentFestivalId());
		utilsManagement.setCurrentPageLowerHeader("ticketShop");
		utilsManagement.prepareModel(model);

		if (ticket == null) {
			model.addAttribute("ticketsNotCreated", "true");
			return "ticketShopUnavailable";
		}

		model.addAttribute("tickets", ticket);
		return "ticketShop";
	}


	@PreAuthorize("hasRole('PLANNER')||hasRole('ADMIN')")
	@PostMapping("tickets/edit")
	public String update(@NotNull @ModelAttribute Ticket ticket, Model model) {

		model.addAttribute("title", "Tickets");
		utilsManagement.prepareModel(model);

		ticketManagement.setCurrentTicket(ticket);
		ticketManagement.save(ticket);

		model.addAttribute("tickets", ticket);
		return "ticketResult";

	}

	Festival getCurrentFestival(){

		return  currentFestival;
	}

	void setCurrentFestival(){

		this.currentFestival = festivalManagement.findById(utilsManagement.getCurrentFestivalId()).get();
	}



}
