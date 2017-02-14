package uk.co.jaschar.email.mailgun;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("${SENDER_CONFIG}")
public class MailgunEmailSenderConfig {

	@Value("${mailgun.email.htmlBodyFile}")
	private String htmlBodyFilePath;
	
	@Value("${mailgun.email.icsFile}")
	private String icsFilePath;
	
	
	@Bean
	public File htmlBodyFile() throws FileNotFoundException {
		File file = new File(htmlBodyFilePath);
		
		if(! file.exists())
			throw new FileNotFoundException(htmlBodyFilePath);
		
		return file;
	}
	
	@Bean
	public File icsFile() {
		File file = new File(icsFilePath);
		
		if(! file.exists())
			return null;
		
		return file;
	}
	
}
