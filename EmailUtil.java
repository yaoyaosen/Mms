package xyz.mryyaos.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {
	public static final Logger logger = LogManager.getLogger(SendEmail.class);

	private static final String FROM_EMAIL = "cool0snail@163.com";
	private static final String FROM_EMAIL_PASSWD = "1231238";
	private static final String SMTP_HOST = "smtp.163.com";


	public static void send(String to, String msg) {

		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", SMTP_HOST);
		prop.put("mail.smtp.port", "25");
		prop.put("mail.smtp.ssl.trust", SMTP_HOST);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(prop);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM_EMAIL));
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Error, 服务异常通知!");
			message.setContent("<p>"+msg+"</p>", "text/html");

			// Send message
			Transport t = session.getTransport("smtp");
			t.connect(SMTP_HOST, FROM_EMAIL, FROM_EMAIL_PASSWD);
			t.sendMessage(message, message.getAllRecipients());

			logger.info("Sent message successfully....");
		} catch (MessagingException e) {
			logger.error("Error, Send email,", e);
		}
	}

	public static void main(String[] args) {
		send("nanjue_zhao@163.com", "ok");
	}
}
