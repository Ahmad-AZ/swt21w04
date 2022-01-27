package festivalmanager.staff;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

/**
 * helper class to set the model attribute "userId" globally when a user is logged in
 * @author Georg Kunze
 */
@ControllerAdvice
public class UserIdControllerAdvice {
	private final StaffManagement staffManagement;

	public UserIdControllerAdvice(StaffManagement staffManagement) {
		Assert.notNull(staffManagement, "StaffManagement must not be null!");
		this.staffManagement = staffManagement;
	}

	@ModelAttribute("userId")
	public Long getUserId(@LoggedIn Optional<UserAccount> userAccount) {
		if (userAccount.isPresent()) {
			Optional<Person> person = staffManagement.findByUserAccount(userAccount.get());
			if (person.isPresent()) {
				return person.get().getId();
			}
		}
		return -1L;
	}
}
