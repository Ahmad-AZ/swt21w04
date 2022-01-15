package festivalmanager.ticketShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;


/**
 * A repository interface to manage {@link Ticket} instances.
 *
 * @author Ahmad Abu Zahra
 */

@Repository
public interface TicketRepository extends CrudRepository<Ticket, UUID> {

	/**
	 * Re-declared {@link JpaRepository#findAll()} to return a {@link List} instead of {@link Iterable}.
	 */
	@Override
	List<Ticket> findAll();

	/**
	 * Created Query to select {@link Ticket} by festivalId
	 * @param id
	 */
	@Query("select t from Ticket t where t.festivalId = ?1")
	Ticket findAllByFestivalId(long id);
}
