package com.dongpv.sns.notification.service;

import com.dongpv.sns.notification.dto.request.EmailRequest;
import com.dongpv.sns.notification.dto.request.SendEmailRequest;
import com.dongpv.sns.notification.dto.request.Sender;
import com.dongpv.sns.notification.dto.response.EmailResponse;
import com.dongpv.sns.notification.exception.AppException;
import com.dongpv.sns.notification.exception.ErrorCode;
import com.dongpv.sns.notification.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    @Value("${notification.email.brevo-apikey}")
    @NonFinal
    String apiKey;

    @Value("${notification.email.name}")
    @NonFinal
    String name;

    @Value("${notification.email.email}")
    @NonFinal
    String email;

    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name(name)
                        .email(email)
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
