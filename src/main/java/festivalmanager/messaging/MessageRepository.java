package festivalmanager.messaging;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface MessageRepository extends CrudRepository<Message, Long> {
	@Override
	Streamable<Message> findAll();

	Streamable<Message> findByReceiverId(long receiverId);
	Streamable<Message> findByReceiverGroupAndReceiverFestivalId(String receiverGroup, long receiverFestivalId);
	Streamable<Message> findByType(MessageType type);
}
