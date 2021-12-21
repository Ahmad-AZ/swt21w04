package festivalmanager.messaging;

import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import festivalmanager.utils.UtilsManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;
import java.util.function.Predicate;

@Controller
public class MessageController {
	private final MessageManagement messageManagement;
	private final StaffManagement staffManagement;
	private final UtilsManagement utilsManagement;

	public MessageController(MessageManagement messageManagement, StaffManagement staffManagement, UtilsManagement utilsManagement) {
		Assert.notNull(messageManagement, "MessageManagement must not be null!");
		Assert.notNull(staffManagement, "StaffManagement must not be null!");

		this.messageManagement = messageManagement;
		this.staffManagement = staffManagement;
		this.utilsManagement = utilsManagement;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Nachrichten";
	}

	@ModelAttribute("messages")
	public Streamable<Message> getMessages(@PathVariable("userId") long userId) {
		return messageManagement.findByReceiverId(userId);
	}

	@GetMapping("/messages/{userId}")
	public String getMessageView(Model model) {
		utilsManagement.setCurrentPageUpperHeader("messages");
		utilsManagement.prepareModel(model);
		return "messages.html";
	}

	@GetMapping("/messages/{userId}/view/{messageId}")
	public String getMessageDetailView(@PathVariable("messageId") long messageId, Model model) {
		Optional<Message> message = messageManagement.findById(messageId);
		if (message.isPresent()) {
			model.addAttribute("currentMessage", message.get());
		}

		utilsManagement.prepareModel(model);
		return "messages.html";
	}

	@GetMapping("/messages/{userId}/send")
	public String getSendMessageDialog(@PathVariable("userId") long userId, Model model) {
		model.addAttribute("dialog", "send_message");

		Optional<Person> user = staffManagement.findById(userId);

		if (user.isPresent()) {
			long festivalId = user.get().getFestivalId();
			Streamable<Person> possibleReceivers = staffManagement.findByFestivalId(festivalId);
			possibleReceivers = possibleReceivers.filter(Predicate.not(Predicate.isEqual(user.get())));
			model.addAttribute("possible_receivers", possibleReceivers);
		}

		utilsManagement.prepareModel(model);
		return "messages.html";
	}

	@PostMapping("/messages/{userId}/send")
	public String sendMessage(@PathVariable("userId") long userId, SendMessageForm form) {
		messageManagement.sendMessage(form);
		return "redirect:/messages/" + userId;
	}
}
