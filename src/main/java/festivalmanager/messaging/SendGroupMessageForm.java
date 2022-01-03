package festivalmanager.messaging;

import javax.validation.constraints.NotEmpty;

public class SendGroupMessageForm {
	@NotEmpty(message = "{SendGroupMessageForm.senderId.NotEmpty}")
	private final long senderId;

	@NotEmpty(message = "{SendGroupMessageForm.receiverFestivalId.NotEmpty}")
	private final long receiverFestivalId;

	@NotEmpty(message = "{SendGroupMessageForm.receiverGroup.NotEmpty}")
	private final String receiverGroup;

	@NotEmpty(message = "{SendGroupMessageForm.title.NotEmpty}")
	private final String title;

	@NotEmpty(message = "{SendGroupMessageForm.content.NotEmpty}")
	private final String content;

	public SendGroupMessageForm(long senderId, long receiverFestivalId, String receiverGroup, String title, String content) {
		this.senderId = senderId;
		this.receiverFestivalId = receiverFestivalId;
		this.receiverGroup = receiverGroup;
		this.title = title;
		this.content = content;
	}

	public long getSenderId() {
		return senderId;
	}

	public long getReceiverFestivalId() {
		return receiverFestivalId;
	}

	public String getReceiverGroup() {
		return receiverGroup;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
}
