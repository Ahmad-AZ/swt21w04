package festivalmanager.staff;


import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

/**
 * the repository to store and manage {@link Person} instances
 * @author Georg Kunze
 */
public interface StaffRepository extends CrudRepository<Person, Long> {
	/**
	 * function to get all {@link Person} instances in the repository
	 * @return					a list of all instances
	 */
	@Override
	Streamable<Person> findAll();

	/**
	 * function to get all {@link Person} instances with the specified festivalId
	 * @param festivalId		the festivalId
	 * @return					a list of all matching instances
	 */
	Streamable<Person> findByFestivalId(long festivalId);

	/**
	 * function to get all {@link Person} instances with the specified festivalId and role
	 * @param festivalId		the festivalId
	 * @param role				the role
	 * @return					a list of all matching instances
	 */
	Streamable<Person> findByFestivalIdAndRole(long festivalId, String role);

	/**
	 * function to get all {@link Person} instances with the specified {@link UserAccount}
	 * @param account			the user-account
	 * @return					a matching instance
	 */
	Optional<Person> findByUserAccount(UserAccount account);
}
