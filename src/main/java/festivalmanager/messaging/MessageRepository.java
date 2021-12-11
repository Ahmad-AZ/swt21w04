package festivalmanager.messaging;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface MessageRepository extends CrudRepository<Message, Long> {
	@Override
	Streamable<Message> findAll();

	Streamable<Message> findByReceiverId(long receiverId);
}
