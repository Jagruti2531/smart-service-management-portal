package com.smartservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Autowired public EmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }
    public void sendOtp(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email); message.setSubject("Smart Service Portal - Your OTP");
            message.setText("Your Smart Service Portal OTP is: " + otp + "\n\nThis OTP is valid for 5 minutes.");
            mailSender.send(message);
            System.out.println("OTP email sent to " + email);
        } catch (Exception ex) {
            System.out.println("Email delivery is not configured. Local OTP for " + email + ": " + otp);
        }
    }
}
