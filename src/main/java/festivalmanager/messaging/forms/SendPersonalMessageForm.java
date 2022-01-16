package festivalmanager.messaging.forms;

import javax.validation.constraints.NotEmpty;

public class SendPersonalMessageForm extends SendMessageForm {
	@NotEmpty(message = "{SendPersonalMessageForm.receiverId.NotEmpty}")
	private final long receiverId;

	public SendPersonalMessageForm(long senderId, long receiverId, String title, String content) {
		super(senderId, title, content);
		this.receiverId = receiverId;
	}

	public long getReceiverId() {
		return receiverId;
	}
}
