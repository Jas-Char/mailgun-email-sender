# E-mail Sending with Mailgun

This small application provides the ability to send HTML e-mails (optionally with attachments) via the Mailgun API.

## Usage with Download

1. Extract ZIP into folder
2. Download the [configuration file](https://raw.githubusercontent.com/Jas-Char/mailgun-email-sender/master/src/main/resources/sender.properties) into the same folder and update with your Mailgun and e-mail configuration
3. Run `java -DSENDER_CONFIG=file:sender.properties -jar mailgun-email-sender-1.0.0.jar`

Note that the provided version only allows a single ICS attachment to be included in the e-mail. However, you can modify the application and use `MailgunApiInterface` to send any attachments you like.

If you download the source, run `mvn clean package` to generate the JAR file.
