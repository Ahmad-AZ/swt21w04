package festivalmanager.messaging;

import festivalmanager.messaging.forms.SendGlobalMessageForm;
import festivalmanager.messaging.forms.SendGroupMessageForm;
import festivalmanager.messaging.forms.SendPersonalMessageForm;
import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * initializer class to generate example data for the message package
 * @author Georg Kunze
 */
@Component
@Order(12)
public class MessageDataInitializer implements DataInitializer {
	private final MessageManagement messageManagement;
	private final StaffManagement staffManagement;

	/**
	 * constructor for the {@link MessageDataInitializer} class
	 * @param messageManagement				the {@link MessageManagement}, must not be {@literal null}
	 * @param staffManagement				the {@link StaffManagement}, must not be {@literal null}
	 */
	MessageDataInitializer(MessageManagement messageManagement, StaffManagement staffManagement) {
		Assert.notNull(messageManagement, "MessageManagement must not be null!");
		Assert.notNull(staffManagement, "StaffManagement must not be null!");

		this.messageManagement = messageManagement;
		this.staffManagement = staffManagement;
	}

	/**
	 * helper function to creat a new personal message
	 * @param senderId						the id of the sender of the message
	 * @param receiverId					the id of the receiver of the message
	 * @param title							the title of the message
	 * @param content						the content of the message
	 */
	public void newMessage(long senderId, long receiverId, String title, String content) {
		messageManagement.sendMessage(new SendPersonalMessageForm(senderId, receiverId, title, content));
	}

	/**
	 * helper function to create a new group message
	 * @param senderId						the id of the sender of the message
	 * @param festivalId					the id of the festival of the receiver of the message
	 * @param group							the group of the receiver of the message
	 * @param title							the title of the message
	 * @param content						the content of the message
	 */
	public void newGroupMessage(long senderId, long festivalId, String group, String title, String content) {
		messageManagement.sendMessage(new SendGroupMessageForm(senderId, festivalId, group, title, content));
	}

	/**
	 * helper function to create a new global message
	 * @param senderId						the id of the sender of the message
	 * @param title							the title of the message
	 * @param content						the content of the message
	 */
	public void newGlobalMessage(long senderId, String title, String content) {
		messageManagement.sendMessage(new SendGlobalMessageForm(senderId, title, content));
	}

	/**
	 * function to initialize the system, called automatically by spring
	 */
	@Override
	public void initialize() {
		if (messageManagement.findAll().isEmpty()) {
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
}
