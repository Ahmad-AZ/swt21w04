package festivalmanager.messaging;

import javax.validation.constraints.NotEmpty;

public class SendPersonalMessageForm {
	@NotEmpty(message = "{SendPersonalMessageForm.senderId.NotEmpty}")
	private final long senderId;

	@NotEmpty(message = "{SendPersonalMessageForm.receiverId.NotEmpty}")
	private final long receiverId;

	@NotEmpty(message = "{SendPersonalMessageForm.title.NotEmpty}")
	private final String title;

	@NotEmpty(message = "{SendPersonalMessageForm.content.NotEmpty}")
	private final String content;

	public SendPersonalMessageForm(long senderId, long receiverId, String title, String content) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.title = title;
		this.content = content;
	}

	public long getSenderId() {
		return senderId;
	}

	public long getReceiverId() {
		return receiverId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
}
