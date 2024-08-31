package com.yvolabs.book.exception;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 31/08/2024
 */

public class OperationNotPermittedException extends RuntimeException {

    public OperationNotPermittedException() {
    }

    public OperationNotPermittedException(String message) {
        super(message);
    }
}