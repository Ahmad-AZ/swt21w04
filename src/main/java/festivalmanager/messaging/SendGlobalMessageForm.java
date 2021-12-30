package festivalmanager.messaging;

import javax.validation.constraints.NotEmpty;

public class SendGlobalMessageForm {
	@NotEmpty(message = "{SendGlobalMessageForm.senderId.NotEmpty}")
	private final long senderId;

	@NotEmpty(message = "{SendGlobalMessageForm.title.NotEmpty}")
	private final String title;

	@NotEmpty(message = "{SendGlobalMessageForm.content.NotEmpty}")
	private final String content;

	public SendGlobalMessageForm(long senderId, String title, String content) {
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
