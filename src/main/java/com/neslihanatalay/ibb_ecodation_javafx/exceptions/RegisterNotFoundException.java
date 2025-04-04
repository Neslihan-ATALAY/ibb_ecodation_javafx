package com.neslihanatalay.ibb_ecodation_javafx.exceptions;

public class RegisterNotFoundException extends RuntimeException {

    public RegisterNotFoundException() {
        super("Kayıt bulunamadı");
    }

    public RegisterNotFoundException(String message) {
        super(message);
    }
}
