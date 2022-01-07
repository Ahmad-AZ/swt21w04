package festivalmanager.ticketShop;


import com.google.zxing.WriterException;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.ticketShop.qr_code.QRCodeGenerator;
import festivalmanager.utils.UtilsManagement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.io.IOException;
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
	@GetMapping("/tickets/{festivalId}")

	public String showTicketInfo(@PathVariable("festivalId") long festivalId,Model model) {

		model.addAttribute("title", "Tickets");
		utilsManagement.prepareModel(model);

		if (Objects.isNull(ticketManagement.getCurrentTicket())) {

			this.currentFestival = festivalManagement.findById(festivalId).get();
			model.addAttribute("ticket", new Ticket());
			model.addAttribute("festival", this.currentFestival);


			utilsManagement.prepareModel(model);
			return "ticketForm";
		}

		model.addAttribute("tickets", ticketManagement.getCurrentTicket());
		return "ticketResult";
	}






	@PreAuthorize("hasRole('PLANNER')||hasRole('ADMIN')")
	@PostMapping("/tickets/{festivalId}")
	public String create(@ModelAttribute Ticket ticket,@PathVariable("festivalId") long festivalId, Model model) {

		model.addAttribute("title", "Tickets");
		utilsManagement.prepareModel(model);

		currentFestival= festivalManagement.findById(festivalId).get();

		ticket.setFestivalName(currentFestival.getName());
		ticket.setFestivalId(currentFestival.getId());

		ticketManagement.setCurrentTicket(ticket);
		ticketManagement.setFestival(currentFestival);


		model.addAttribute("tickets", ticketManagement.save(ticket));
		return "ticketResult";
	}


	@PreAuthorize("hasRole('TICKET_SELLER')||hasRole('ADMIN')")
	@PostMapping("/tickets/buy")
	@ExceptionHandler({IOException.class, WriterException.class})
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

			try {
				if (nTicket.getId() != null) {

					String uuid = nTicket.getId().toString();
					QRCodeGenerator.generateQRCodeImage(uuid);
				}

			} catch (WriterException |IOException e) {
				e.printStackTrace();
			}

			model.addAttribute("ticketCount", soldTicket);
			model.addAttribute("ticketPrice", ticketPrice * soldTicket);
			model.addAttribute("festival", currentFestival);
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
	@GetMapping("/ticketShop/{festivalId}")
	public String ticketOverview(@PathVariable("festivalId") long festivalId,  Model model) {


		Ticket ticket = ticketManagement.TicketsByFestival(festivalId);
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
