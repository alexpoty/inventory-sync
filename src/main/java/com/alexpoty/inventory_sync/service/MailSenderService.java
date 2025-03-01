package com.alexpoty.inventory_sync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderService {

    private final JavaMailSender mailSender;
    private final String ownerEmail = "test@gmail.com";

    public void sendMail(Map<String, String> productsToOrder) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("inventory-sync@gmail.com");
            messageHelper.setTo(ownerEmail);
            messageHelper.setSubject("Your products is low stock, you can see below what products should be ordered");

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<html><body>");
            emailContent.append("<h2 style='color: #d9534f;'>Low Stock Alert</h2>");
            emailContent.append("<p>Here is a list of products with stock levels below 10:</p>");

            // Table Header
            emailContent.append("<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>");
            emailContent.append("<tr style='background-color: #f2f2f2;'>");
            emailContent.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Product</th>");
            emailContent.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Warehouse</th>");
            emailContent.append("</tr>");

            // Table Rows
            for (Map.Entry<String, String> entry : productsToOrder.entrySet()) {
                emailContent.append("<tr>");
                emailContent.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(entry.getKey()).append("</td>");
                emailContent.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(entry.getValue()).append("</td>");
                emailContent.append("</tr>");
            }

            emailContent.append("</table>");
            emailContent.append("<p style='margin-top: 20px;'>Please restock these products as soon as possible.</p>");
            emailContent.append("</body></html>");

            messageHelper.setText(emailContent.toString(), true);
        };
        try {
            mailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            log.error("Failed to send mail", e);
            throw new RuntimeException("Exception occurred then sending mail");
        }
    }
}
