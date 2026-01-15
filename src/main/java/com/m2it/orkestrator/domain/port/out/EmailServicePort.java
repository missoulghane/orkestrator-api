package com.m2it.orkestrator.domain.port.out;

public interface EmailServicePort {

    void sendVerificationEmail(String to, String token);

    void sendPasswordResetEmail(String to, String token);

}
