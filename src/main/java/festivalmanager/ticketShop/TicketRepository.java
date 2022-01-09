package festivalmanager.ticketShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

	@Override
	List<Ticket> findAll();
	@Query("select t from Ticket t where t.festivalId = ?1")
	Ticket findAllByFestivalId(long id);
}
