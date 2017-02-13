package uk.co.jaschar.email.mailgun.helpers;

import com.google.common.net.MediaType;

public class EmailAttachment {

	private final MediaType attachmentMediaType;
	
	private final String attachmentBody;
	
	private final String attachmentFileName;

	
	public EmailAttachment(MediaType attachmentMediaType, String attachmentBody, String attachmentFileName) {
		this.attachmentMediaType = attachmentMediaType;
		this.attachmentBody = attachmentBody;
		this.attachmentFileName = attachmentFileName;
	}


	public MediaType getAttachmentMediaType() {
		return attachmentMediaType;
	}

	public String getAttachmentBody() {
		return attachmentBody;
	}
	
	public String getAttachmentFileName() {
		return attachmentFileName;
	}
}
