package festivalmanager.staff;

import festivalmanager.staff.forms.*;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional
public class StaffManagement {
	private final StaffRepository staff;
	private final UserAccountManagement userAccountManagement;

	StaffManagement(StaffRepository staff, UserAccountManagement userAccountManagement) {

		Assert.notNull(staff, "StaffRepository must not be null!");
		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");

		this.staff = staff;
		this.userAccountManagement = userAccountManagement;
	}

	public Person createPerson(long festivalId, CreateStaffForm form) {
		Assert.notNull(form, "Registration form must not be null!");

		var password = Password.UnencryptedPassword.of(form.getPassword());
		var userAccount = userAccountManagement.create(form.getName(), password, Role.of(form.getRole()));

		return staff.save(new Person(festivalId, form.getName(), form.getRole(), userAccount));
	}

	public void removePerson(RemoveStaffForm form) {
		Optional<Person> person = staff.findById(form.getId());
		if (person.isPresent()) {
			userAccountManagement.delete(person.get().getAccount());
			staff.deleteById(person.get().getId());
		}
	}

	public void changeRole(ChangeRoleForm form) {
		Optional<Person> person = findById(form.getId());
		if (person.isPresent()) {
			person.get().getAccount().remove(Role.of(person.get().getRole()));
			person.get().getAccount().add(Role.of(form.getRole()));
			person.get().setRole(form.getRole());
		}
	}

	public void changePassword(ChangePasswordForm form) {
		Optional<Person> person = findById(form.getId());
		if (person.isPresent()) {
			userAccountManagement.changePassword(person.get().getAccount(), Password.UnencryptedPassword.of(form.getPassword()));
		}
	}

	public Streamable<Person> findAll() {
		return staff.findAll();
	}
	public Streamable<Person> findByFestivalId(long festivalId) {
		return staff.findByFestivalId(festivalId);
	}

	public Optional<Person> findById(long id) {
		return staff.findById(id);
	}
}
