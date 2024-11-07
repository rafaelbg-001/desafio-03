package com.webservices.desafio.Exceptions;

public class PasswordsNotMatchingException extends RuntimeException {

    public PasswordsNotMatchingException(String message) {
        super(message);
    }
}
