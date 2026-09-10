package com.smartservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from:onboarding@resend.dev}")
    private String fromEmail;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void sendOtp(String email, String otp) {

        try {

            String json = """
                    {
                      "from": "%s",
                      "to": ["%s"],
                      "subject": "Smart Service Portal - Your OTP",
                      "html": "<h2>Smart Service Portal</h2><p>Your OTP is:</p><h1>%s</h1><p>This OTP is valid for 5 minutes.</p><p>Please do not share this OTP with anyone.</p>"
                    }
                    """.formatted(
                    escapeJson(fromEmail),
                    escapeJson(email),
                    escapeJson(otp)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            json,
                            StandardCharsets.UTF_8
                    ))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() >= 200 &&
                    response.statusCode() < 300) {

                System.out.println("OTP email sent successfully to " + email);

            } else {

                System.err.println(
                        "Resend email failed. HTTP " +
                        response.statusCode() +
                        ": " +
                        response.body()
                );

                throw new RuntimeException(
                        "Unable to send OTP email"
                );
            }

        } catch (Exception ex) {

            System.err.println(
                    "OTP email sending failed: " +
                    ex.getMessage()
            );

            throw new RuntimeException(
                    "Unable to send OTP email"
            );
        }
    }

    private String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}