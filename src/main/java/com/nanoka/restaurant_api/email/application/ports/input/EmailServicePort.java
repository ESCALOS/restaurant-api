package com.nanoka.restaurant_api.email.application.ports.input;

import jakarta.mail.MessagingException;

public interface EmailServicePort {
    void sendPasswordResetEmail(String to, String token) throws MessagingException;
}
