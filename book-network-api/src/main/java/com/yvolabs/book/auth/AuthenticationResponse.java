package com.yvolabs.book.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 28/07/2024
 */
@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
}
