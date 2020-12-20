package com.findthebusiness.backend.utils;

import com.findthebusiness.backend.entity.Users;
import com.findthebusiness.backend.security.security_config.MyUserDetails;
import com.findthebusiness.backend.security.utils.AccessTokenUtil;
import com.findthebusiness.backend.security.utils.RefreshTokenUtil;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;

@Component
public class EmailUtil {
    private final AccessTokenUtil jwtUtil;
    private final RefreshTokenUtil refreshTokenUtil;

    public EmailUtil(AccessTokenUtil jwtUtil, RefreshTokenUtil refreshTokenUtil) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenUtil = refreshTokenUtil;
    }

    public void sendConfirmationForRegistrationEmail(String email, String url, String action) throws AddressException, MessagingException, IOException, NoSuchAlgorithmException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.fallback", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("produsesiservicii.ro@gmail.com", "dgtuutltczwdjthz"); //System.getenv("EMAIL_APP_PASS")
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("produsesiservicii.ro@gmail.com", false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject("Produse&Servicii.ro: Email de confirmare");

        msg.setText("<a href=\""+ url +"\"><button>Apasati aici</button></a> pentru a confirma " + action);
        //msg.setContent("Press on: localhost:8080/api/authentication/confirmation_email_register?code=" + token + " to confirm your account", "text/html");
        msg.setSentDate(new Date());

        //TODO style the email -> e.g. button with href for confirmation
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        //messageBodyPart.setContent("Press on: <a href="+endpoint+"><button>click here</button></a> to confirm your account", "text/html");
        messageBodyPart.setText("<a href=\""+ url +"\"><button>Apasati aici</button></a> pentru a confirma " + action, "UTF-8", "html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        //MimeBodyPart attachPart = new MimeBodyPart();

        //attachPart.attachFile("/var/tmp/image19.png");
        //multipart.addBodyPart(attachPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }
}
