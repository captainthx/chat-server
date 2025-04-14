package com.yutsuki.chatserver.service;

import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
@Log4j2
public class MailService {

    @Value("${api.email.template.verification}")
    private String emailTemplateVerification;

    @Value("${api.email.template.forgot-password}")
    private String emailTemplateForgot;

    @Resource
    private JavaMailSender mailSender;
    @Resource
    private MailProperties mailProperties;

    public void send(String to, String subject, String html) {
        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
        };
        mailSender.send(message);
    }

    @Async
    public void sendEmailVerificationCode(String email, String code) {
        String html;
        try {
            html = readHtml(emailTemplateVerification);
        } catch (IOException e) {
            throw new RuntimeException("SendEmailVerificationCode-[error].(template verification not found). error: " + e.getMessage());
        }

        String subject = "Email Verification";
        html = html.replace("{{CODE}}", code);
        try {
            send(email, subject, html);
        } catch (Exception e) {
            throw new RuntimeException("SendEmailVerificationCode-[error].(can't send email). error: " + e.getMessage());
        }
    }

    @Async
    public void sendForgotPasswordCode(String email, String code) {
        String html;
        try {
            html = readHtml(emailTemplateForgot);
        } catch (IOException e) {
            throw new RuntimeException("SendForgotPasswordCode-[error].(template forgot password not found). error: " + e.getMessage());
        }

        String subject = "Forgot Password";
        html = html.replace("{{CODE}}", code);
        try {
            send(email, subject, html);
        } catch (Exception e) {
            throw new RuntimeException("SendForgotPasswordCode-[error].(can't send email). error: " + e.getMessage());
        }
    }

    public String readHtml(String templatePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = resource.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
    }
}

