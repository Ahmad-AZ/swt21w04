package festivalmanager.messaging;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.messaging.forms.SendGlobalMessageForm;
import festivalmanager.messaging.forms.SendMessageForm;
import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class MessageManagement {
	private final MessageRepository repository;
	private final StaffManagement staffManagement;
	private final FestivalManagement festivalManagement;

	public MessageManagement(MessageRepository repository, StaffManagement staffManagement, FestivalManagement festivalManagement) {
		Assert.notNull(repository, "MessageRepository must not be null!");
		Assert.notNull(staffManagement, "StaffManagement must not be null!");
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");

		this.repository = repository;
		this.staffManagement = staffManagement;
		this.festivalManagement = festivalManagement;
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

	@Scheduled(fixedDelay = 60000)
	public void scanForStartedFestivals() {
		LocalDate now = LocalDate.now();
		for (Festival festival : festivalManagement.findAll()) {
			LocalDate start = festival.getStartDate();
			if ((start.isAfter(now) || start.isEqual(now)) && !festival.isAnnounced()) {
				sendMessage(new SendGlobalMessageForm(-1, festival.getName() + " hat gerade begonnen", ""));
				festival.setAnnounced(true);
			}
		}
	}
}
