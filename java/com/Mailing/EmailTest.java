package com.Mailing;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailTest {
    public static void main(String[] args) {
        String to = "srikanthbusinessman555@gmail.com";
        String from = "srikanth.linga01@gmail.com";
        final String username = "srikanth.linga01@gmail.com";
        final String password = "skdvszvohgxakwal"; // Use Gmail App Password

        
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Test Subject");
            message.setText("Hello, this is a test email from Java!");

            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
