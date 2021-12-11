package festivalmanager.messaging;

import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(12)
public class MessageDataInitializer implements DataInitializer {
	private final MessageManagement messageManagement;
	private final StaffManagement staffManagement;

	MessageDataInitializer(MessageManagement messageManagement, StaffManagement staffManagement) {
		Assert.notNull(messageManagement, "MessageManagement must not be null!");
		Assert.notNull(staffManagement, "StaffManagement must not be null!");

		this.messageManagement = messageManagement;
		this.staffManagement = staffManagement;
	}

	@Override
	public void initialize() {
		long adminId = 0, managerId = 0;
		for (Person p : staffManagement.findByFestivalId(-1).toList()) {
			if (p.getName().equals("admin")) {
				adminId = p.getId();
			} else if (p.getName().equals("manager")) {
				managerId = p.getId();
			}
		}

		messageManagement.sendMessage(new SendMessageForm(managerId, adminId, "test manager -> admin", "Hello World!"));
		messageManagement.sendMessage(new SendMessageForm(managerId, adminId, "test manager -> admin - 2", "Hello World! - 2"));
		messageManagement.sendMessage(new SendMessageForm(adminId, managerId, "test admin -> manager", "Hello World!"));
		messageManagement.sendMessage(new SendMessageForm(adminId, managerId, "test admin -> manager - 2", "Hello World! - 2"));
	}
}
