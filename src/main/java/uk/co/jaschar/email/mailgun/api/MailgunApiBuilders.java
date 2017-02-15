package uk.co.jaschar.email.mailgun.api;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.MultipartContent;

/**
 * <h3>Google API Client Builders for Mailgun API</h3>
 * 
 * @author Jas Rajasansir
 * @version 1.0.0
 * @since 13 Feb 2017
 */
public final class MailgunApiBuilders {
	
	public static final HttpMediaType MULTIPART_FORM_DATA = new HttpMediaType("multipart/form-data")
																							.setParameter("boundary", "__END_OF_PART__");

	public static MultipartContent getNewMultipartFormDataContent() {
		return new MultipartContent()
								.setMediaType(MULTIPART_FORM_DATA);
	}
	
	public static HttpHeaders getMultipartFormPartHeader(String fieldName) {
		return new HttpHeaders()
							.set("Content-Disposition", "form-data; name=\"" + fieldName + "\"");
	}
	
	public static HttpHeaders getMultipartFormPartHeaderForFile(String fieldName, String fileName) {
		return new HttpHeaders()
							.set("Content-Disposition", "form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"");
	}

}
