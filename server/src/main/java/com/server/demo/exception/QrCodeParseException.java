package com.server.demo.exception;

public class QrCodeParseException extends RuntimeException {

    public QrCodeParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
