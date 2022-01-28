package festivalmanager.messaging;

import festivalmanager.messaging.forms.SendGroupMessageForm;
import festivalmanager.messaging.forms.SendMessageForm;
import festivalmanager.messaging.forms.SendPersonalMessageForm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * class that represents a message
 * @author Georg Kunze
 */
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

	/**
	 * constructor to initialize a message instance from a SendMessageForm
	 * @param form					the form containing the required data to initialize the message instance
	 * @param senderName			the name of the sender
	 */
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

	/**
	 * default constructor form {@link Message}
	 */
	public Message() {}

	/**
	 * getter for the id field
	 * @return				the id of this message
	 */
	public long getId() {
		return id;
	}

	/**
	 * getter for the senderId field
	 * @return				the id of the sender of this message
	 */
	public long getSenderId() {
		return senderId;
	}

	/**
	 * getter for the receiverId field
	 * @return				the id of the receiver of this message
	 */
	public long getReceiverId() {
		return receiverId;
	}

	/**
	 * getter for the receiverFestivalId field
	 * @return				the id of the festival of the receiver of this message
	 */
	public long getReceiverFestivalId() {
		return receiverFestivalId;
	}

	/**
	 * getter for the receiverGroup field
	 * @return				the group of the receiver of this message
	 */
	public String getReceiverGroup() {
		return receiverGroup;
	}

	/**
	 * getter for the senderName field
	 * @return				the name of the sender of this message
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * getter for the title field
	 * @return				the title of this message
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * getter for the content field
	 * @return				the content of this message
	 */
	public String getContent() {
		return content;
	}

	/**
	 * getter for the type field
	 * @return				the {@link MessageType} of this message
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * getter for the sentTimestamp field
	 * @return				the timestamp at which this message was sent
	 */
	public LocalDateTime getSentTimestamp() {
		return sentTimestamp;
	}
}
