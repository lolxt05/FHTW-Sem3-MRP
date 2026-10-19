package org.mrp.model.exceptions;

public class SessionTokenInvalid extends RuntimeException {
    public SessionTokenInvalid(String message) {
        super(message);
    }
}
