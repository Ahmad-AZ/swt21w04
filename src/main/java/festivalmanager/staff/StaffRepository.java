package festivalmanager.staff;


import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

public interface StaffRepository extends CrudRepository<Person, Long> {
	@Override
	Streamable<Person> findAll();

	Streamable<Person> findByFestivalId(long festivalId);

	Optional<Person> findByUserAccount(UserAccount account);
}
