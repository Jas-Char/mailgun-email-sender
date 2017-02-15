package uk.co.jaschar.email.mailgun;

import java.io.File;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.common.io.Files;
import com.google.common.net.MediaType;

import uk.co.jaschar.email.mailgun.api.MailgunApiInterface;
import uk.co.jaschar.email.mailgun.helpers.EmailAttachment;

/**
 * <h3>Mailgun E-mail Sender (via Mailgun API)</h3>
 * 
 * @author Jas Rajasansir
 * @version 1.0.0
 * @since 13 Feb 2017
 */
@SpringBootApplication
public class MailgunEmailSender implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(MailgunEmailSender.class);

	
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(MailgunEmailSender.class);
		
		try {
			application.run(args);
		} catch (Exception e) {
			log.error("Application failed to initialise. Application will now EXIT!");
			System.exit(1);
		}
	}
	
	
	@Value("${mailgun.email.from}")
	private String fromAddress;
	
	@Value("${mailgun.email.to}")
	private String toAddress;
	
	@Value("${mailgun.email.subject}")
	private String subject;
	
	
	@Autowired
	@Qualifier("htmlBodyFile")
	private File htmlBodyFile;
	
	@Autowired
	@Qualifier("icsFile")
	private File icsFile;
	
	@Autowired
	private MailgunApiInterface mailgun;
	

	public void run(String... arg0) throws Exception {
		String htmlBody = Files.toString(htmlBodyFile, Charset.defaultCharset());
		
		if(icsFile == null) {
			mailgun.sendHtmlEmail(fromAddress, toAddress, subject, htmlBody, null);
			return;
		}
		
		String icsBody = Files.toString(icsFile, Charset.defaultCharset());
		EmailAttachment icsAttachment = new EmailAttachment(MediaType.I_CALENDAR_UTF_8, icsBody, "invite.ics");
		
		mailgun.sendHtmlEmail(fromAddress, toAddress, subject, htmlBody, icsAttachment);
	}

}
