package uk.co.jaschar.email.mailgun.api;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.buabook.http.common.HttpClient;
import com.buabook.http.common.exceptions.HttpClientRequestFailedException;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.MultipartContent.Part;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import uk.co.jaschar.email.mailgun.helpers.EmailAttachment;

/**
 * <h3>Mailgun API Interface</h3>
 * (c) 2017 Sport Trades Ltd
 * 
 * @author Jas Rajasansir
 * @version 1.0.0
 * @since 13 Feb 2017
 */
@Component
public class MailgunApiInterface {
	private static final Logger log = LoggerFactory.getLogger(MailgunApiInterface.class);
	
	private static final String API_URL_TEMPLATE = "https://api.mailgun.net/v3/__DOMAIN__/messages";

	
	@Value("${mailgun.api.domain}")
	private String apiDomain;
	
	@Value("${mailgun.api.key}")
	private String apiKey;
	
	
	private HttpClient httpClient;
	
	private String apiUrl;
	
	
	@PostConstruct
	private void initialise() {
		if(Strings.isNullOrEmpty(apiDomain) || Strings.isNullOrEmpty(apiKey))
			throw new IllegalArgumentException("Missing API domain or API key");
		
		this.httpClient = new HttpClient("api", apiKey);
		this.apiUrl = API_URL_TEMPLATE.replace("__DOMAIN__", apiDomain);
		
		log.info("New Mailgun API Interface object created [ Domain: {} ]", apiDomain);
	}
	
	
	public void sendHtmlEmail(String fromAddress, String toAddress, String subject, String htmlBody, EmailAttachment attachment) throws HttpClientRequestFailedException {
		Map<String, Object> mailParameters = ImmutableMap.<String, Object>builder()
																				.put("from", fromAddress)
																				.put("to", toAddress)
																				.put("subject", subject)
																				.put("html", htmlBody)
																				.build();
		
		if(attachment == null)
			sendEmail(mailParameters);
		else
			sendEmailWithAttachments(mailParameters, ImmutableList.of(attachment));
	}
	
	protected void sendEmail(Map<String, Object> parameters) throws HttpClientRequestFailedException {
		if(parameters == null || parameters.isEmpty())
			throw new IllegalArgumentException("No parameters to send e-mail with");
		
		JSONObject response = null;
		
		log.info("Sending e-mail (no attachments) via Mailgun API [ Domain: {} ] ", apiDomain); 
		
		try {
			HttpResponse httpResponse = httpClient.doPost(apiUrl, parameters);
			response = HttpClient.getResponseAsJson(httpResponse);
		} catch (HttpClientRequestFailedException e) {
			log.error("Failed to send e-mail via Mailgun API. Error - {}", e.getMessage(), e);
			throw e;
		}

		log.info("Mail sent via Mailgun API! Response: {}", response.toString());
	}
	
	protected void sendEmailWithAttachments(Map<String, Object> parameters, List<EmailAttachment> attachments) throws HttpClientRequestFailedException {
		if(parameters == null || parameters.isEmpty())
			throw new IllegalArgumentException("No parameters to send e-mail with");
		
		if(attachments == null || attachments.isEmpty())
			throw new IllegalArgumentException("No attachments to send. Use 'sendEmail' method instead");
		
		MultipartContent content = MailgunApiBuilders.getNewMultipartFormDataContent();
		
		for(Entry<String, Object> param : parameters.entrySet()) {
			Part contentPart = new Part(ByteArrayContent.fromString("text/plain", param.getValue().toString()));
			contentPart.setHeaders(MailgunApiBuilders.getMultipartFormPartHeader(param.getKey()));
			
			content.addPart(contentPart);
		}

		for(EmailAttachment attachment : attachments) {
			if(attachment == null)
				continue;
			
			Part fileContentPart = new Part(ByteArrayContent.fromString(attachment.getAttachmentMediaType().toString(), attachment.getAttachmentBody()));
			fileContentPart.setHeaders(MailgunApiBuilders.getMultipartFormPartHeaderForFile("attachment", attachment.getAttachmentFileName()));
			content.addPart(fileContentPart);
		}
		
		
		JSONObject response = null;
		
		log.info("Sending e-mail (with attachments) via Mailgun API [ Domain: {} ] [ Attachment Count: {} ]", apiDomain, attachments.size()); 
		
		try {
			HttpResponse httpResponse = httpClient.doPost(apiUrl, content, null);
			response = HttpClient.getResponseAsJson(httpResponse);
		} catch (HttpClientRequestFailedException e) {
			log.error("Failed to send e-mail via Mailgun API. Error - {}", e.getMessage(), e);
			throw e;
		}

		log.info("Mail (with attachments) sent via Mailgun API! Response: {}", response.toString());
	}
}
