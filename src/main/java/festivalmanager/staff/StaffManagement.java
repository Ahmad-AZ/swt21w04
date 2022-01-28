package festivalmanager.staff;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.staff.forms.*;
import festivalmanager.utils.UtilsManagement;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * staff management logic
 * @author Georg Kunze
 */
@Service
@Transactional
public class StaffManagement {
	private final StaffRepository staff;
	private final UserAccountManagement userAccountManagement;
	private final FestivalManagement festivalManagement;

	public static final String[] roles = {
		"ADMIN",
		"MANAGER",
		"PLANNER",
		"FESTIVAL_LEADER",
		"TICKET_SELLER",
		"SECURITY",
		"CATERING"
	};

	/**
	 * constructor for the {@link StaffManagement} class
	 * @param staff					the {@link StaffRepository}, must not be {@literal null}
	 * @param userAccountManagement	the {@link UserAccountManagement}, must not be {@literal null}
	 * @param festivalManagement	the {@link FestivalManagement}, must not be {@literal null}
	 */
	StaffManagement(StaffRepository staff,
					UserAccountManagement userAccountManagement,
					FestivalManagement festivalManagement) {
		Assert.notNull(staff, "StaffRepository must not be null!");
		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");
		
		this.staff = staff;
		this.userAccountManagement = userAccountManagement;
		this.festivalManagement = festivalManagement;
	}

	/**
	 * factory method for a person, also stores the created instance in the repository
	 * @param festivalId			the id of the festival the person is associated with
	 * @param form					the form containing all the data required to create the person
	 * @return						the created instance
	 */
	public Person createPerson(long festivalId, CreateStaffForm form) {
		Assert.notNull(form, "Registration form must not be null!");

		if (List.of(roles).contains(form.getRole())) {
			var password = Password.UnencryptedPassword.of(form.getPassword());
			var userAccount = userAccountManagement.create(form.getName(), password, Role.of(form.getRole()));

			return staff.save(new Person(festivalId, form, userAccount));
		} else {
			return null;
		}
	}

	/**
	 * function to remove a person from the repository
	 * @param form					the form containing the id of the person
	 * @param festival				the current festival
	 */
	public void removePerson(RemoveStaffForm form, Festival festival) {
		Optional<Person> person = staff.findById(form.getId());
		if (person.isPresent()) {
			userAccountManagement.delete(person.get().getUserAccount());
			
			// requiered to avoid schedule errors
			if(person.get().getRole().equals("SECURITY") && festival != null) {
				festival.removeSecurity(form.getId());
				festivalManagement.saveFestival(festival);
			}
			
			staff.deleteById(person.get().getId());
		}
	}

	/**
	 * function to change the role of a person
	 * @param form					the form containing the id and new role of the person
	 */
	public void changeRole(ChangeRoleForm form) {
		Optional<Person> person = findById(form.getId());
		if (person.isPresent()) {
			if (person.get().getRole().equals("ADMIN") || person.get().getRole().equals("MANAGER")) {
				return;
			}

			if (List.of(roles).contains(form.getRole())) {
				person.get().getUserAccount().remove(Role.of(person.get().getRole()));
				person.get().getUserAccount().add(Role.of(form.getRole()));
				person.get().setRole(form.getRole());
			}
		}
	}

	/**
	 * function to change the password of a person
	 * @param form					the form containing the id and new password of the person
	 */
	public void changePassword(ChangePasswordForm form) {
		Optional<Person> person = findById(form.getId());
		if (person.isPresent()) {
			Password.UnencryptedPassword newPassword = Password.UnencryptedPassword.of(form.getPassword());
			userAccountManagement.changePassword(person.get().getUserAccount(), newPassword);
		}
	}

	/**
	 * wrapper for {@link StaffRepository}::findAll
	 * @return						a list of all persons in the repository
	 */
	public Streamable<Person> findAll() {
		return staff.findAll();
	}

	/**
	 * function to get all persons associated with a certain festival
	 * @param festivalId			the id of the festival
	 * @return						a list of all persons with matching criteria
	 */
	public Streamable<Person> findByFestivalId(long festivalId) {
		if (festivalId == -1) {
			// admins can see everyone
			return staff.findAll();
		} else {
			// everyone can see people from festival and admins
			return staff.findByFestivalId(festivalId).and(staff.findByFestivalId(-1));
		}
	}

	/**
	 * function to get all persons associated with a certain festival that have a certain role
	 * @param festivalId			the id of the festival
	 * @param role					the role
	 * @return						a list of all persons with matching criteria
	 */
	public Streamable<Person> findByFestivalIdAndRole(long festivalId, String role) {
		return staff.findByFestivalIdAndRole(festivalId, role);
	}

	/**
	 * function to get the person with the specified id
	 * @param id					the id of the person
	 * @return						the person if it is in the repository
	 */
	public Optional<Person> findById(long id) {
		return staff.findById(id);
	}

	/**
	 * function to get the person with the specified userAccount
	 * @param account				the account of the person
	 * @return						the person if it is in the repository
	 */
	public Optional<Person> findByUserAccount(UserAccount account) {
		return staff.findByUserAccount(account);
	}

	/**
	 * function to get the number of persons with the SECURITY role associated with the festival
	 * @param festivalId			the id of the festival
	 * @return						the number of persons in the festival with the SECURITY role
	 */
	public long getAvailableSecurityCount(long festivalId) {
		long availableSecurity = 0;
		for (Person person : findByFestivalId(festivalId)) {
			if (person.getRole().equals("SECURITY")) {
				availableSecurity++;
			}
		}
		return availableSecurity;
	}
}
