package com.moresby.ed.stockportfolio.email;

import com.moresby.ed.stockportfolio.user.User;
import com.moresby.ed.stockportfolio.user.registration.token.ConfirmEmailToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;

@Service
@Slf4j
@AllArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final String AUTH_TOKEN = "qtemkiotvvfdgkqd";
    private final static String HOST_URL = "http://localhost:8080/api/v1/confirmToken";
    private CustomEmail customEmail;

    @Async
    @Override
    public void sendConfirmEmail(User user){
        String from = "foxlovecat1989@gmail.com";
        String to = user.getEmail();
        String title = "MyStock Website confirm email";
       ConfirmEmailToken confirmEmailToken =
                user.getConfirmEmailTokens().stream()
                        .filter(
                                next ->
                                        next.getConfirmedAt() == null &&
                                                next.getExpiresAt().isAfter(LocalDateTime.now())
                        )
                        .findFirst()
                        .orElseThrow(
                                () -> {
                                    var msg = String.format("No valid token in User's Email: %s are founded", user.getEmail());
                                    IllegalStateException exception = new IllegalStateException(msg);
                                    log.error(msg);
                                    return exception;
                                }
                        );

        var url = HOST_URL + String.format("?token=%s", confirmEmailToken.getToken());
        System.out.println(url);
        String contentHtml =
                String.format("Dear %s, <p /><a href=' %s'>Confirm Email</a>", user.getUsername(), url);

        customEmail = CustomEmail.builder()
                .from(from)
                .to(to)
                .title(title)
                .contentHtml(contentHtml)
                .build();

        Runnable runnable = () -> {
            try {
                submit();
            } catch (Exception e) {
                log.error(e.getMessage());
                System.out.println("Email send error: " + e);
                e.printStackTrace();
            }

        };
        new Thread(runnable).start();
    }

    @Override
    public void submit() throws Exception {
        final String googleGmail = customEmail.getFrom();

        // get smpt
        Properties prop = getProperties();

        // create session to communicate with smpt
        Session session;
        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(googleGmail, AUTH_TOKEN);
                    }
                });

        Message message = new MimeMessage(session);
        InternetAddress internetAddress = new InternetAddress(googleGmail);
        internetAddress.setPersonal(googleGmail);
        message.setFrom(internetAddress);
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(customEmail.getTo())
        );
        message.setSubject(customEmail.getTitle());
        message.setContent(customEmail.getContentHtml(), "text/html;charset=utf-8");

        Transport.send(message);

        log.info(String.format("Email to: %s success", customEmail.getTo()));

    }

    private Properties getProperties() {
        // smpt config info
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        return prop;
    }
}
