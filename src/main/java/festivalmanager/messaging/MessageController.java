package festivalmanager.messaging;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MessageController {
	private final MessageManagement messageManagement;

	public MessageController(MessageManagement messageManagement) {
		Assert.notNull(messageManagement, "MessageManagement must not be null!");
		this.messageManagement = messageManagement;
	}

	@GetMapping("/messages/{receiverId}")
	public String getMessageView(@PathVariable("receiverId") long receiverId) {
		return "index.html";
	}
}
