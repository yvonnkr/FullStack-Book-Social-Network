package com.yvolabs.book.auth;

import com.yvolabs.book.email.EmailService;
import com.yvolabs.book.email.EmailTemplateName;
import com.yvolabs.book.exception.ActivationTokenException;
import com.yvolabs.book.role.Role;
import com.yvolabs.book.role.RoleRepository;
import com.yvolabs.book.security.JwtService;
import com.yvolabs.book.user.Token;
import com.yvolabs.book.user.TokenRepository;
import com.yvolabs.book.user.User;
import com.yvolabs.book.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.yvolabs.book.enums.RoleName.USER;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 27/07/2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final String ACCOUNT_ACTIVATED_MESSAGE = "Account activated";

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {

        Role userRole = roleRepository.findByName(USER.getRoleName())
                .orElseThrow(() -> new EntityNotFoundException("Role not found with name: " + USER.getRoleName()));

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new EntityExistsException("User already exists with email: " + user.getEmail());
                });
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = (User) auth.getPrincipal();
        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public String activateAccount(String token) {

        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ActivationTokenException("Invalid activation token"));

        checkIfActivationTokenHasExpired(savedToken);

        User user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + savedToken.getUser().getId()));

        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
        return ACCOUNT_ACTIVATED_MESSAGE;

    }

    private void checkIfActivationTokenHasExpired(Token savedToken) {
        // Because sendValidationEmail() is Async, if we throw, the data is not being saved to db,Hence needed to use CompletableFuture.runAsync()
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    sendValidationEmail(savedToken.getUser());
                    throw new ActivationTokenException("Activation token has expired. A new token has been sent to the same email address");
                } catch (RuntimeException | MessagingException e) {
                    throw new ActivationTokenException(e.getMessage());
                }
            });

            try {
                future.get();
                log.info("CompletableFuture CAllED");
            } catch (RuntimeException | InterruptedException | ExecutionException e) {
                log.warn("CompletableFuture Exception: {}", e.getMessage());
                log.warn("Failed to activate account with token: {} ", savedToken.getToken());
                throw new ActivationTokenException(e.getCause().getMessage());
            }
        }
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                ACCOUNT_ACTIVATED_MESSAGE
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
