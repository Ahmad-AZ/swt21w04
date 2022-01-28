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

/**
 * initializer class to generate example data for the staff package
 * @author Georg Kunze
 */
@Component
@Order(11)
class StaffDataInitializer implements DataInitializer {
	private final UserAccountManagement userAccountManagement;
	private final StaffManagement staffManagement;
	private final FestivalManagement festivalManagement;

	/**
	 * constructor for the {@link StaffDataInitializer} class
	 * @param userAccountManagement			the {@link UserAccountManagement}, must not be {@literal null}
	 * @param staffManagement				the {@link StaffManagement}, must not be {@literal null}
	 * @param festivalManagement			the {@link FestivalManagement}, must not be {@literal null}
	 */
	StaffDataInitializer(UserAccountManagement userAccountManagement,
						 StaffManagement staffManagement,
						 FestivalManagement festivalManagement) {
		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(staffManagement, "StaffManagement must not be null!");
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");

		this.userAccountManagement = userAccountManagement;
		this.staffManagement = staffManagement;
		this.festivalManagement = festivalManagement;
	}

	/**
	 * helper function to create a user if he isn't already in the system
	 * @param festivalId				the id of the festival the user should be associated with
	 * @param name						the name of the user
	 * @param password					the password of the user
	 * @param role						the role of the user
	 * @param salary					the salary of the user
	 */
	public void createUser(long festivalId, String name, String password, String role, double salary) {
		if (userAccountManagement.findByUsername(name).isEmpty()) {
			staffManagement.createPerson(festivalId, new CreateStaffForm(name, password, role, salary));
		}
	}

	/**
	 * function to initialize the system, called automatically by spring
	 */
	@Override
	public void initialize() {
		createUser(-1, "admin", "adminpw", "ADMIN", 0.0);
		createUser(-1, "manager", "managerpw", "MANAGER", 25.0);
		createUser(-1, "planner_1", "plannerpw", "PLANNER", 20.0);
		createUser(-1, "planner_2", "plannerpw", "PLANNER", 20.0);

		long festivalId = 0;
		Streamable<Festival> festivals = festivalManagement.findAll();
		if (!festivals.isEmpty()) {
			festivalId = festivals.toList().get(0).getId();
		}

		System.out.println("preparing users for festival " + festivalId);

		createUser(festivalId, "festival_leader", "staffpw", "FESTIVAL_LEADER", 0.0);
		createUser(festivalId, "ticket_seller_1", "staffpw", "TICKET_SELLER", 10.0);
		createUser(festivalId, "security_1", "staffpw", "SECURITY", 10.0);
		createUser(festivalId, "security_2", "staffpw", "SECURITY", 10.0);
		createUser(festivalId, "security_3", "staffpw", "SECURITY", 10.0);
		createUser(festivalId, "catering_1", "staffpw", "CATERING", 10.0);
		createUser(festivalId, "catering_2", "staffpw", "CATERING", 10.0);
	}
}
