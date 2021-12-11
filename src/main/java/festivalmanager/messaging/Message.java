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

	private String senderName;
	private String title;
	private String content;

	private LocalDateTime sentTimestamp;

	public Message(SendMessageForm form, String senderName) {
		this.senderId = form.getSenderId();
		this.receiverId = form.getReceiverId();
		this.title = form.getTitle();
		this.content = form.getContent();
		this.sentTimestamp = LocalDateTime.now();
		this.senderName = senderName;
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

	public String getSenderName() {
		return senderName;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
}
