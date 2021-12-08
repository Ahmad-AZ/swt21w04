package festivalmanager.staff;

import festivalmanager.staff.forms.CreateStaffForm;
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
		if (userAccountManagement.findByUsername("admin").isEmpty()) {
			staffManagement.createPerson(-1, new CreateStaffForm("admin", "adminpw", "ADMIN", 0.0));
		}

		if (userAccountManagement.findByUsername("manager").isEmpty()) {
			staffManagement.createPerson(-1, new CreateStaffForm("manager", "managerpw", "MANAGER", 0.0));
		}

		if (userAccountManagement.findByUsername("catering").isEmpty()) {
			staffManagement.createPerson(0, new CreateStaffForm("catering", "cateringpw", "CATERING", 0.0));
		}
	}
}
