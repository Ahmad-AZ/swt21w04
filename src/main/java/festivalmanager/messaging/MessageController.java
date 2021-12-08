package festivalmanager.messaging;

import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;
import java.util.function.Predicate;

@Controller
public class MessageController {
	private final MessageManagement messageManagement;
	private final StaffManagement staffManagement;

	public MessageController(MessageManagement messageManagement, StaffManagement staffManagement) {
		Assert.notNull(messageManagement, "MessageManagement must not be null!");
		Assert.notNull(messageManagement, "StaffManagement must not be null!");

		this.messageManagement = messageManagement;
		this.staffManagement = staffManagement;
	}

	@GetMapping("/messages/{userId}")
	public String getMessageView(@PathVariable("userId") long userId, Model model) {
		model.addAttribute("messages", messageManagement.findByReceiverId(userId));

		return "messages.html";
	}

	@GetMapping("/messages/{userId}/send")
	public String getSendMessageDialog(@PathVariable("userId") long userId, Model model) {
		model.addAttribute("messages", messageManagement.findByReceiverId(userId));
		model.addAttribute("dialog", "send_message");

		Optional<Person> user = staffManagement.findById(userId);

		if (user.isPresent()) {
			long festivalId = user.get().getFestivalId();
			Streamable<Person> possibleReceivers = staffManagement.findByFestivalId(festivalId);
			possibleReceivers = possibleReceivers.filter(Predicate.not(Predicate.isEqual(user.get())));
			model.addAttribute("possible_receivers", possibleReceivers);
		}

		return "messages.html";
	}

	@PostMapping("/messages/{senderId}/send")
	public String sendMessage(@PathVariable("senderId") long senderId, SendMessageForm form) {
		messageManagement.sendMessage(form);
		return "redirect:/messages/" + senderId;
	}
}
