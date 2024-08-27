package com.yvolabs.book.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 27/08/2024
 */

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or Password is incorrect"),
    INVALID_ACTIVATION_TOKEN(400, BAD_REQUEST, "Invalid activation token"),
    INTERNAL_SERVER_ERROR(400, BAD_REQUEST, "Something went wrong, Internal Server Error"),;

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    private BusinessErrorCodes(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
