package festivalmanager.staff;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(10)
class StaffDataInitializer implements DataInitializer {
	private final UserAccountManagement userAccountManagement;
	private final StaffManagement staffManagement;

	StaffDataInitializer(UserAccountManagement userAccountManagement, StaffManagement staffManagement) {
		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(staffManagement, "StaffManagement must not be null!");

		this.userAccountManagement = userAccountManagement;
		this.staffManagement = staffManagement;
	}

	@Override
	public void initialize() {
		if (userAccountManagement.findByUsername("admin").isPresent()) {
			return;
		}

		userAccountManagement.create("admin", Password.UnencryptedPassword.of("adminpw"), Role.of("ADMIN"));
	}
}
