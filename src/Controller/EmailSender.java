package Controller;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String toEmail, String subject, String content) {
        final String fromEmail = "aktu046@gmail.com"; // Thay bằng email phòng khám
        final String password = "vdzr rnxz lsmx edyp";     // Thay bằng app password từ Gmail

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("✔ Email sent successfully to " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send email to " + toEmail);
        }
    }
}
