package com.example.lexar.projecthbo_ictbv.error;

/**
 * And error for getting an error code from the server.
 */
public class BadCodeException extends Exception {

    private int code;
    private String message;

    public BadCodeException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Did not receive a 200 code. Received " + code +
                " instead. With the message: " + message;
    }
}
