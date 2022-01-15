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


/**
 *
 * @author Ahmad Abu Zahra
 */

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


	/**

	 * @return
	 * title attribute for the TicketShop tab
	 */
	@ModelAttribute("title")
	public String getTitle() {
		return "Ticketshop";
	}


	/**
	 * display the current {@link Ticket} info for a given if the Ticket not created will Return:
	 * TicketFrom  page and if it's not will Return: TicketResult page
	 * @param festivalId
	 * the current festival id
	 * @param model
	 * contains all the data-related logic .



	 */
	@PreAuthorize("hasRole('PLANNER')||hasRole('ADMIN')")
	@GetMapping("/tickets/{festivalId}")
	public String showTicketInfo(@PathVariable("festivalId") long festivalId,Model model) {

		model.addAttribute("title", "Tickets");
		utilsManagement.prepareModel(model, festivalId);

		if (Objects.isNull(ticketManagement.getCurrentTicket())) {

			this.currentFestival = festivalManagement.findById(festivalId).get();
			model.addAttribute("ticket", new Ticket());
			model.addAttribute("festival", this.currentFestival);
			utilsManagement.prepareModel(model, festivalId);
			return "ticketForm";
		}

		model.addAttribute("tickets", ticketManagement.getCurrentTicket());
		return "ticketResult";
	}





	/**
	 * Creates a new {@link Ticket} using the information given .
	 *
	 * @param ticket must not be {@literal null}.
	 * @return the new {@link Ticket} instance in the HTML.
	 */

	@PreAuthorize("hasRole('PLANNER')||hasRole('ADMIN')")
	@PostMapping("/tickets/{festivalId}")
	public String create(@ModelAttribute Ticket ticket,@PathVariable("festivalId") long festivalId, Model model) {

		model.addAttribute("title", "Tickets");
		utilsManagement.prepareModel(model, festivalId);

		currentFestival= festivalManagement.findById(festivalId).get();

		ticket.setFestivalName(currentFestival.getName());
		ticket.setFestivalId(currentFestival.getId());

		ticketManagement.setCurrentTicket(ticket);
		ticketManagement.setFestival(currentFestival);


		model.addAttribute("tickets", ticketManagement.save(ticket));
		return "ticketResult";
	}


	/**
	 * Buying  {@link Ticket} using the given information  for the current {@link Festival}.
	 *
	 * @param ticket
	 * contains the count of the ticket to be sold
	 * ,must not be {@literal null}.

	 */

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



			try {
				String location = currentFestival.getLocation().getName();
				model.addAttribute("locationsExists", "true");
			}
			catch (NullPointerException exception)
			{
				System.out.println(exception);
			}


		} else {
			utilsManagement.prepareModel(model, currentFestival.getId());
			model.addAttribute("ticketsUnavailable", "true");
			return "ticketShopUnavailable";
		}

		utilsManagement.prepareModel(model, currentFestival.getId());

		return "ticketPrint";
	}


	/**
	 * Used check if there's not {@link Ticket} created for the current {@link Festival} before selling Ticket
	 * @param festivalId
	 * for the current festival id.
	 * @param model
	 * contains all the data-related logic for the current instance.

	 * @return TicketShop page
	 */
	@PreAuthorize("hasRole('TICKET_SELLER')||hasRole('ADMIN')")
	@GetMapping("/ticketShop/{festivalId}")
	public String ticketOverview(@PathVariable("festivalId") long festivalId,  Model model) {

		utilsManagement.prepareModel(model, festivalId);
		if (ticketManagement.TicketsByFestival(festivalId) == null) {
			model.addAttribute("ticketsNotCreated", "true");
			return "ticketShopUnavailable";
		}

		model.addAttribute("tickets", new Ticket());
		return "ticketShop";
	}


	/**
	 *
	 * @param ticket
	 * the updated {@link Ticket} to be assign for the current {@link Ticket}.
	 * @param model
	 * spring boot model for the TicketResult page.
	 * @return
	 * TicketResult page.
	 */
	@PreAuthorize("hasRole('PLANNER')||hasRole('ADMIN')")
	@PostMapping("tickets/edit")
	public String update(@NotNull @ModelAttribute Ticket ticket, Model model) {

		model.addAttribute("title", "Tickets");
		utilsManagement.prepareModel(model, currentFestival.getId());

		ticketManagement.setCurrentTicket(ticket);
		ticketManagement.save(ticket);

		model.addAttribute("tickets", ticket);
		return "ticketResult";

	}


	/**
	 * Used to set the current  {@link Festival}.
	 *
	 * @param id
	 * to find the current Festival by id
	 *
	 */
	void setCurrentFestival(long id ){

		this.currentFestival = festivalManagement.findById(id).get();
	}



}
