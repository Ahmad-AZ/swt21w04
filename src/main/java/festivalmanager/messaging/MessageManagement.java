package festivalmanager.messaging;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class MessageManagement {
	private final MessageRepository repository;

	public MessageManagement(MessageRepository repository) {
		Assert.notNull(repository, "MessageRepository must not be null!");
		this.repository = repository;
	}

	Streamable<Message> findAll() {
		return repository.findAll();
	}

	Streamable<Message> findByReceiverId(long receiverId) {
		return repository.findByReceiverId(receiverId);
	}
}
