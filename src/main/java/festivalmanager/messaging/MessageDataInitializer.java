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

	public void newMessage(long senderId, long receiverId, String title, String content) {
		messageManagement.sendMessage(new SendPersonalMessageForm(senderId, receiverId, title, content));
	}

	public void newGroupMessage(long senderId, long festivalId, String group, String title, String content) {
		messageManagement.sendMessage(new SendGroupMessageForm(senderId, festivalId, group, title, content));
	}

	public void newGlobalMessage(long senderId, String title, String content) {
		messageManagement.sendMessage(new SendGlobalMessageForm(senderId, title, content));
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

		newMessage(managerId, adminId, "test manager -> admin", "Hello World!");
		newMessage(managerId, adminId, "test manager -> admin - 2", "Hello World! - 2");
		newMessage(adminId, managerId, "test admin -> manager", "Hello World!");
		newMessage(adminId, managerId, "test admin -> manager - 2", "Hello World! - 2");

		newGroupMessage(adminId, -1, "MANAGER", "admin -> MANAGER", "Hello World!");
		newGlobalMessage(adminId, "Hello World!", "global messages work");
	}
}
