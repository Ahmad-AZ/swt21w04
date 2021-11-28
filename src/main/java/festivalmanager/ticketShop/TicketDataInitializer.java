package festivalmanager.ticketShop;

import org.salespointframework.core.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//import java.time.LocalDate;
//import java.util.Date;
import java.util.Objects;

@Component
@Order(5)
public class TicketDataInitializer implements DataInitializer {

	@Autowired
	final TicketRepository ticketRepository;

	public TicketDataInitializer(TicketRepository ticketRepository) {
		if (Objects.isNull(ticketRepository)) {

			System.out.println("object can't be null");
		}
		this.ticketRepository = ticketRepository;
	}

	@Override
	public void initialize() {

		// ticketRepository.save(new Ticket("",
		// "16/05/2021",5,TicketType.DAY_TICKET,100));
		//
		//
		//
		// ticketRepository.save(new Ticket("", "16/05/2021",5,TicketType.CAMPING,111));
		//
		//
		//
		// ticketRepository.save(new Ticket("", "16/05/2021",5,TicketType.CAMPING,
		// 125.5F));
		//
		//
		//
		// ticketRepository.save(new Ticket("",
		// "16/05/2021",5,TicketType.DAY_TICKET,52));
		//
		//
		//
		// ticketRepository.save(new Ticket("",
		// "16/05/2021",5,TicketType.DAY_TICKET,156));
		//
		//

	}
}
