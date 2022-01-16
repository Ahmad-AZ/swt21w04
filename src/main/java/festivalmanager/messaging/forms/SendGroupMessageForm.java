package festivalmanager.messaging.forms;

import javax.validation.constraints.NotEmpty;

public class SendGroupMessageForm extends SendMessageForm {
	@NotEmpty(message = "{SendGroupMessageForm.receiverFestivalId.NotEmpty}")
	private final long receiverFestivalId;

	@NotEmpty(message = "{SendGroupMessageForm.receiverGroup.NotEmpty}")
	private final String receiverGroup;

	public SendGroupMessageForm(long senderId, long receiverFestivalId, String receiverGroup, String title, String content) {
		super(senderId, title, content);
		this.receiverFestivalId = receiverFestivalId;
		this.receiverGroup = receiverGroup;
	}

	public long getReceiverFestivalId() {
		return receiverFestivalId;
	}

	public String getReceiverGroup() {
		return receiverGroup;
	}
}
