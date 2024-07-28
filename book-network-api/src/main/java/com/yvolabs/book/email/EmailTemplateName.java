package com.yvolabs.book.email;

import lombok.Getter;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 28/07/2024
 */

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account.html");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
