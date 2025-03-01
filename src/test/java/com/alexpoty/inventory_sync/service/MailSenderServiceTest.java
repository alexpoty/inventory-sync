package com.alexpoty.inventory_sync.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailSenderService mailSenderService;

    @Test
    void sendMail_ShouldSendEmailSuccessfully() {
        // Given
        Map<String, String> productsToOrder = Map.of(
                "Laptop", "Warehouse A",
                "Phone", "Warehouse B"
        );

        // When
        doNothing().when(mailSender).send(any(MimeMessagePreparator.class));

        mailSenderService.sendMail(productsToOrder);

        // Then
        verify(mailSender, Mockito.times(1)).send(any(MimeMessagePreparator.class));
    }

    @Test
    void sendMail_ShouldHandleMailException() {
        // Given
        Map<String, String> productsToOrder = Map.of("Laptop", "Warehouse A");

        doThrow(new MailException("Mail sending failed") {
        }).when(mailSender).send(any(MimeMessagePreparator.class));

        // Then
        assertThrows(
                RuntimeException.class,
                () -> mailSenderService.sendMail(productsToOrder),
                "Exception occurred then sending mail"
        );

        verify(mailSender, times(1)).send(any(MimeMessagePreparator.class));
    }
}