package festivalmanager.messaging;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long senderId;
	private long receiverId;
	private long receiverFestivalId;
	private String receiverGroup;

	private String senderName;
	private String title;
	private String content;

	private LocalDateTime sentTimestamp;

	private MessageType type;

	public Message(SendMessageForm form, String senderName) {
		this.senderId = form.getSenderId();
		this.title = form.getTitle();
		this.content = form.getContent();
		this.sentTimestamp = LocalDateTime.now();
		this.senderName = senderName;

		if (form instanceof SendGroupMessageForm) {
			this.receiverFestivalId = ((SendGroupMessageForm)form).getReceiverFestivalId();
			this.receiverGroup = ((SendGroupMessageForm)form).getReceiverGroup();

			this.type = MessageType.GroupMessage;
		} else if (form instanceof SendPersonalMessageForm) {
			this.receiverId = ((SendPersonalMessageForm)form).getReceiverId();

			this.type = MessageType.PersonalMessage;
		} else {
			this.type = MessageType.GlobalMessage;
		}
	}

	public Message() {}

	public long getId() {
		return id;
	}

	public long getSenderId() {
		return senderId;
	}

	public long getReceiverId() {
		return receiverId;
	}

	public long getReceiverFestivalId() {
		return receiverFestivalId;
	}

	public String getReceiverGroup() {
		return receiverGroup;
	}

	public String getSenderName() {
		return senderName;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public MessageType getType() {
		return type;
	}

	public LocalDateTime getSentTimestamp() {
		return sentTimestamp;
	}
}
