package festivalmanager.messaging;

import javax.validation.constraints.NotEmpty;

public class SendMessageForm {
	@NotEmpty(message = "{SendMessageForm.senderId.NotEmpty}")
	private final long senderId;

	@NotEmpty(message = "{SendMessageForm.title.NotEmpty}")
	private final String title;

	@NotEmpty(message = "{SendMessageForm.content.NotEmpty}")
	private final String content;

	public SendMessageForm(long senderId, String title, String content) {
		this.senderId = senderId;
		this.title = title;
		this.content = content;
	}

	public long getSenderId() {
		return senderId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
}
