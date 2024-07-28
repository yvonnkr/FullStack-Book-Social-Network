package com.yvolabs.book.enums;

import lombok.Getter;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 28/07/2024
 */

@Getter
public enum RoleName {
    USER("USER"),
    ADMIN("ADMIN");


    private final String roleName;

    RoleName(String role) {
        this.roleName = role;
    }
}
