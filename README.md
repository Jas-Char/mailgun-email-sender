# E-mail Sending with Mailgun

This small application provides the ability to send HTML e-mails (optionally with attachments) via the Mailgun API.

## Usage

1. Clone the repository
2. Modify `src/main/resources/sender.properties` with your Mailgun and e-mail configuration
2. In the repository root, run `mvn clean package`
4. Run `java -jar target/mailgun-email-sender-1.0.0-SNAPSHOT.jar`

Note that the provided version only allows a single ICS attachment to be included in the e-mail. However, you modify the application and use `MailgunApiInterface` to send any attachments you like.
