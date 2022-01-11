package festivalmanager.messaging;

import festivalmanager.messaging.forms.SendGlobalMessageForm;
import festivalmanager.messaging.forms.SendGroupMessageForm;
import festivalmanager.messaging.forms.SendPersonalMessageForm;
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

	public MessageController(MessageManagement messageManagement,
							 StaffManagement staffManagement, UtilsManagement utilsManagement) {
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
		Optional<Person> user = staffManagement.findById(userId);

		return user.map(messageManagement::findMessagesForUser).orElse(null);
	}

	@GetMapping("/messages/{userId}")
	public String getMessageView() {
		return "messages.html";
	}

	@GetMapping("/messages/{userId}/view/{messageId}")
	public String getMessageDetailView(@PathVariable("messageId") long messageId, Model model) {
		Optional<Message> message = messageManagement.findById(messageId);
		model.addAttribute("currentMessage", message.orElse(null));

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

		return "messages.html";
	}

	@GetMapping("/messages/{userId}/send-group")
	public String getSendGroupMessageDialog(@PathVariable("userId") long userId, Model model) {
		model.addAttribute("dialog", "send_group_message");

		model.addAttribute("possible_receivers", StaffManagement.roles);

		Optional<Person> user = staffManagement.findById(userId);
		if (user.isPresent()) {
			model.addAttribute("festivalId", user.get().getFestivalId());
		}

		return "messages.html";
	}

	@GetMapping("/messages/{userId}/send-global")
	public String getSendGlobalMessageDialog(@PathVariable("userId") long userId, Model model) {
		model.addAttribute("dialog", "send_global_message");

		return "messages.html";
	}

	@PostMapping("/messages/{userId}/send")
	public String sendMessage(@PathVariable("userId") long userId, SendPersonalMessageForm form) {
		messageManagement.sendMessage(form);
		return "redirect:/messages/" + userId;
	}

	@PostMapping("/messages/{userId}/send-group")
	public String sendGroupMessage(@PathVariable("userId") long userId, SendGroupMessageForm form) {
		messageManagement.sendMessage(form);
		return "redirect:/messages/" + userId;
	}

	@PostMapping("/messages/{userId}/send-global")
	public String sendGlobalMessage(@PathVariable("userId") long userId, SendGlobalMessageForm form) {
		messageManagement.sendMessage(form);
		return "redirect:/messages/" + userId;
	}
}
