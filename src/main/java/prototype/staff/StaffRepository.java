package prototype.staff;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface StaffRepository extends CrudRepository<Person, Long> {
	@Override
	Streamable<Person> findAll();
}
