package festivalmanager.ticketShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

	// TODO: 11/13/2021 find all ticket by festival object  
	@Override
	List<Ticket> findAll();
}
