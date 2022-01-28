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

/**
 * message management logic
 * @author Georg Kunze
 */
@Service
@Transactional
public class MessageManagement {
	private final MessageRepository repository;
	private final StaffManagement staffManagement;
	private final FestivalManagement festivalManagement;

	/**
	 * constructor for the {@link MessageManagement} class
	 * @param repository				the {@link MessageRepository}, must not be {@literal null}
	 * @param staffManagement			the {@link StaffManagement}, must not be {@literal null}
	 * @param festivalManagement		the {@link FestivalManagement}, must not be {@literal null}
	 */
	public MessageManagement(MessageRepository repository, StaffManagement staffManagement, FestivalManagement festivalManagement) {
		Assert.notNull(repository, "MessageRepository must not be null!");
		Assert.notNull(staffManagement, "StaffManagement must not be null!");
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");

		this.repository = repository;
		this.staffManagement = staffManagement;
		this.festivalManagement = festivalManagement;
	}

	/**
	 * helper function to create a message and store it in the repository
	 * @param form						the SendMessageForm containing all relevant data to create a message
	 */
	public void sendMessage(SendMessageForm form) {
		Optional<Person> sender = staffManagement.findById(form.getSenderId());
		String senderName = form.getSenderId() == -1 ? "[system]" : "[unknown]";
		if (sender.isPresent()) {
			senderName = sender.get().getName();
		}

		repository.save(new Message(form, senderName));
	}

	/**
	 * wrapper function for {@link MessageRepository}::findAll
	 * @return							a list of all messages in the repository
	 */
	public Streamable<Message> findAll() {
		return repository.findAll();
	}

	/**
	 * wrapper function for {@link MessageRepository}::findById
	 * @param id						the id of the message
	 * @return							the matching message
	 */
	public Optional<Message> findById(long id) {
		return repository.findById(id);
	}

	/**
	 * wrapper function for {@link MessageRepository}::findByReceiverId
	 * @param receiverId				the id of the receiver of the message
	 * @return							a list of all matching messages in the repository
	 */
	public Streamable<Message> findPersonalMessages(long receiverId) {
		return repository.findByReceiverId(receiverId);
	}

	/**
	 * wrapper function for {@link MessageRepository}::findByReceiverGroupAndReceiverFestivalId
	 * @param group						the group of the receiver of the message
	 * @param festivalId				the id of the festival of the receiver of the message
	 * @return							a list of all matching messages in the repository
	 */
	public Streamable<Message> findGroupMessages(String group, long festivalId) {
		return repository.findByReceiverGroupAndReceiverFestivalId(group, festivalId);
	}

	/**
	 * function to get all messages with the type set to {@link MessageType}.GlobalMessage
	 * @return							a list of all matching messages in the repository
	 */
	public Streamable<Message> findGlobalMessages() {
		return repository.findByType(MessageType.GlobalMessage);
	}

	/**
	 * function to get all messages for a certain user
	 * @param receiver					the receiver of the messages
	 * @return							a list of personal messages for the receiver and group messages for the receiver's group
	 */
	public Streamable<Message> findMessagesForUser(Person receiver) {
		Streamable<Message> personalMessages = findPersonalMessages(receiver.getId());
		Streamable<Message> groupMessages = findGroupMessages(receiver.getRole(), receiver.getFestivalId());
		return personalMessages.and(groupMessages);
	}

	/**
	 * function to scan for festivals that started and automatically send a message
	 * gets executed automatically once every 60 seconds
	 */
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
