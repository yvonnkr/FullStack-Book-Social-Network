package com.yvolabs.book.config;

import com.yvolabs.book.BookNetworkApiApplication;
import com.yvolabs.book.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 29/08/2024
 *
 * @apiNote This class implements AuditAware interface in Spring Data that allows you to provide the current auditor (usually the current user)
 * to automatically populate auditing fields like createdBy and lastModifiedBy in your entities.
 * @see BookNetworkApiApplication  Application Entry Point
 * @see BeansConfig Bean Declation
 */

public class ApplicationAuditAware implements AuditorAware<Integer> {

    @SuppressWarnings("NullableProblems")
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }

}
