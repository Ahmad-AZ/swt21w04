package festivalmanager.messaging;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * the repository to store and manage {@link Message} instances
 * @author Georg Kunze
 */
public interface MessageRepository extends CrudRepository<Message, Long> {
	/**
	 * function to get all {@link Message} instances in the repository
	 * @return							a list of all messages
	 */
	@Override
	Streamable<Message> findAll();

	/**
	 * function to get all {@link Message} instances with a certain receiverId in the repository
	 * @param receiverId				the receiverId
	 * @return							a list of messages with matching criteria
	 */
	Streamable<Message> findByReceiverId(long receiverId);

	/**
	 * function to get all {@link Message} instances with a certain receiverGroup and receiverFestivalId in the repository
	 * @param receiverGroup				the receiverGroup
	 * @param receiverFestivalId		the receiverFestivalId
	 * @return							a list of messages with matching criteria
	 */
	Streamable<Message> findByReceiverGroupAndReceiverFestivalId(String receiverGroup, long receiverFestivalId);

	/**
	 * function to get all {@link Message} instances with a certain type
	 * @param type						the message type
	 * @return							a list of messages with matching criteria
	 */
	Streamable<Message> findByType(MessageType type);
}
