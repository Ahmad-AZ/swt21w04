package festivalmanager.messaging;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MessageController {
	private final MessageManagement messageManagement;

	public MessageController(MessageManagement messageManagement) {
		Assert.notNull(messageManagement, "MessageManagement must not be null!");
		this.messageManagement = messageManagement;
	}

	@GetMapping("/messages/{receiverId}")
	public String getMessageView(@PathVariable("receiverId") long receiverId, Model model) {
		model.addAttribute("messages", messageManagement.findByReceiverId(receiverId));

		return "messages.html";
	}

	@GetMapping("/messages/{receiverId}/send")
	public String getSendMessageDialog(@PathVariable("receiverId") long receiverId, Model model) {
		model.addAttribute("messages", messageManagement.findByReceiverId(receiverId));
		model.addAttribute("dialog", "send_message");

		return "messages.html";
	}

	@PostMapping("/messages/{senderId}/send")
	public String sendMessage(@PathVariable("senderId") long senderId, SendMessageForm form) {
		messageManagement.sendMessage(form);
		return "redirect:/messages/" + senderId;
	}
}
