package festivalmanager.staff;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.staff.forms.CreateStaffForm;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(11)
class StaffDataInitializer implements DataInitializer {
	private final UserAccountManagement userAccountManagement;
	private final StaffManagement staffManagement;
	private final FestivalManagement festivalManagement;

	StaffDataInitializer(UserAccountManagement userAccountManagement, StaffManagement staffManagement, FestivalManagement festivalManagement) {
		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(staffManagement, "StaffManagement must not be null!");
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");

		this.userAccountManagement = userAccountManagement;
		this.staffManagement = staffManagement;
		this.festivalManagement = festivalManagement;
	}

	@Override
	public void initialize() {
		if (userAccountManagement.findByUsername("admin").isEmpty()) {
			staffManagement.createPerson(-1, new CreateStaffForm("admin", "adminpw", "ADMIN", 0.0));
		}

		if (userAccountManagement.findByUsername("manager").isEmpty()) {
			staffManagement.createPerson(-1, new CreateStaffForm("manager", "managerpw", "MANAGER", 0.0));
		}

		if (userAccountManagement.findByUsername("planner_1").isEmpty()) {
			staffManagement.createPerson(-1, new CreateStaffForm("planner_1", "plannerpw", "PLANNER", 0.0));
		}

		if (userAccountManagement.findByUsername("planner_2").isEmpty()) {
			staffManagement.createPerson(-1, new CreateStaffForm("planner_2", "plannerpw", "PLANNER", 0.0));
		}

		long festivalId = 0;
		Streamable<Festival> festivals = festivalManagement.findAll();
		if (!festivals.isEmpty()) {
			festivalId = festivals.toList().get(0).getId();
		}

		System.out.println("preparing users for festival " + festivalId);

		if (userAccountManagement.findByUsername("festival_leader").isEmpty()) {
			staffManagement.createPerson(festivalId, new CreateStaffForm("festival_leader", "staffpw", "FESTIVAL_LEADER", 0.0));
		}

		if (userAccountManagement.findByUsername("ticket_seller_1").isEmpty()) {
			staffManagement.createPerson(festivalId, new CreateStaffForm("ticket_seller_1", "staffpw", "TICKET_SELLER", 0.0));
		}

		if (userAccountManagement.findByUsername("admission_1").isEmpty()) {
			staffManagement.createPerson(festivalId, new CreateStaffForm("admission_1", "staffpw", "ADMISSION", 0.0));
		}

		if (userAccountManagement.findByUsername("admission_2").isEmpty()) {
			staffManagement.createPerson(festivalId, new CreateStaffForm("admission_2", "staffpw", "ADMISSION", 0.0));
		}

		if (userAccountManagement.findByUsername("security_1").isEmpty()) {
			staffManagement.createPerson(festivalId, new CreateStaffForm("security_1", "staffpw", "SECURITY", 0.0));
		}

		if (userAccountManagement.findByUsername("security_2").isEmpty()) {
			staffManagement.createPerson(festivalId, new CreateStaffForm("security_2", "staffpw", "SECURITY", 0.0));
		}

		if (userAccountManagement.findByUsername("security_3").isEmpty()) {
			staffManagement.createPerson(festivalId, new CreateStaffForm("security_3", "staffpw", "SECURITY", 0.0));
		}

		if (userAccountManagement.findByUsername("catering_1").isEmpty()) {
			staffManagement.createPerson(festivalId, new CreateStaffForm("catering_1", "staffpw", "CATERING", 0.0));
		}

		if (userAccountManagement.findByUsername("catering_2").isEmpty()) {
			staffManagement.createPerson(festivalId, new CreateStaffForm("catering_2", "staffpw", "CATERING", 0.0));
		}
	}
}
