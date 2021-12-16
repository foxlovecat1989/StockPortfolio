package com.moresby.ed.stockportfolio.service.impl;

import com.moresby.ed.stockportfolio.domain.CustomEmail;
import com.moresby.ed.stockportfolio.service.EmailService;
import com.moresby.ed.stockportfolio.domain.User;
import com.moresby.ed.stockportfolio.domain.ConfirmEmailToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

import static com.moresby.ed.stockportfolio.constant.EmailConstant.*;
import static javax.mail.Message.RecipientType.TO;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String authToken = "qtemkiotvvfdgkqd";
    private final static PasswordAuthentication passwordAuthentication =
            new PasswordAuthentication(GMAIL_ACCOUNT, authToken);
    private CustomEmail customEmail;

    @Async
    @Override
    public void sendConfirmEmail(User user){
        generateConfirmTokenEmail(user);
        sendEmail();
    }
    @Async
    @Override
    public void sendNewPasswordEmail(User user, String password){
        generateNewPasswordEmail(user, password);
        sendEmail();
    }
    private void generateNewPasswordEmail(User user, String password) {
        String to = user.getEmail();
        String title = "MyStock Website - New Password";
        String contentHtml =
                String.format(
                        "Hello %s, \n \n Your new account password is: %s \n \n The Support Team",
                        user.getUsername(),
                        password
                );
        customEmail =
                CustomEmail.builder()
                        .from(GMAIL_ACCOUNT)
                        .to(to)
                        .title(title)
                        .contentHtml(contentHtml)
                        .build();
    }

    private void generateConfirmTokenEmail(User user) {
        String to = user.getEmail();
        String title = "MyStock Website confirm email";
        String contentHtml =
                String.format("Dear %s, <p /><a href=' %s'>Confirm Email</a>", user.getUsername(), getTokenUrl(user));
        customEmail =
                CustomEmail.builder()
                .from(GMAIL_ACCOUNT)
                .to(to)
                .title(title)
                .contentHtml(contentHtml)
                .build();
    }

    private void sendEmail() {
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
        Message message = new MimeMessage(getEmailSession());
        InternetAddress internetAddress = new InternetAddress(GMAIL_ACCOUNT);
        internetAddress.setPersonal(GMAIL_ACCOUNT);
        message.setFrom(internetAddress);
        message.setRecipients(
                TO,
                InternetAddress.parse(customEmail.getTo())
        );
        message.setSubject(customEmail.getTitle());
        message.setContent(customEmail.getContentHtml(), "text/html;charset=utf-8");
        message.setSentDate(new Date());
        Transport.send(message);

        log.info(String.format("Email - to: %s success", customEmail.getTo()));
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);

        return Session.getInstance(properties,  new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return passwordAuthentication;
            }
        });
    }

    private String getTokenUrl(User user) {
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

        return CONFIRM_TOKEN_API_PATH + String.format("?token=%s", confirmEmailToken.getToken());
    }
}
