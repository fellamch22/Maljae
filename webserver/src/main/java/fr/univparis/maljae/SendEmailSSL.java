package fr.univparis.maljae;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmailSSL {

    public static void sendEmailSSL(String mailTo,String subject , String messagefull) {

        final String username = "lifang@vanmiltenburg.fr";
        final String password = "Louloutte!24";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "ssl0.ovh.net");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("lifang@prepro.fr"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mailTo)
            );
            message.setSubject(subject);
            message.setText(messagefull);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}