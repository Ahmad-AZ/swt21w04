package festivalmanager.messaging;

import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(10)
public class MessageDataInitializer implements DataInitializer {
	private final MessageRepository repository;

	MessageDataInitializer(MessageRepository repository) {
		Assert.notNull(repository, "MessageRepository must not be null!");
		this.repository = repository;
	}

	@Override
	public void initialize() {
	}
}
