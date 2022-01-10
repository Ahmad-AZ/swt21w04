package festivalmanager.messaging;

import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional
public class MessageManagement {
	private final MessageRepository repository;
	private final StaffManagement staffManagement;

	public MessageManagement(MessageRepository repository, StaffManagement staffManagement) {
		Assert.notNull(repository, "MessageRepository must not be null!");
		Assert.notNull(staffManagement, "StaffManagement must not be null!");

		this.repository = repository;
		this.staffManagement = staffManagement;
	}

	public void sendMessage(SendMessageForm form) {
		Optional<Person> sender = staffManagement.findById(form.getSenderId());
		String senderName = "[unknown]";
		if (sender.isPresent()) {
			senderName = sender.get().getName();
		}

		repository.save(new Message(form, senderName));
	}

	public Streamable<Message> findAll() {
		return repository.findAll();
	}

	public Optional<Message> findById(long id) {
		return repository.findById(id);
	}

	public Streamable<Message> findPersonalMessages(long receiverId) {
		return repository.findByReceiverId(receiverId);
	}

	public Streamable<Message> findGroupMessages(String group, long festivalId) {
		return repository.findByReceiverGroupAndReceiverFestivalId(group, festivalId);
	}

	public Streamable<Message> findGlobalMessages() {
		return repository.findByType(MessageType.GlobalMessage);
	}

	public Streamable<Message> findMessagesForUser(Person receiver) {
		Streamable<Message> personalMessages = findPersonalMessages(receiver.getId());
		Streamable<Message> groupMessages = findGroupMessages(receiver.getRole(), receiver.getFestivalId());
		return personalMessages.and(groupMessages);
	}
}
