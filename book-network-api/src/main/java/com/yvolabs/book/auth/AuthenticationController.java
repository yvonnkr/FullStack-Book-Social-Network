package com.yvolabs.book.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 27/07/2024
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> confirm(@RequestParam String token) throws MessagingException {
        String activationMessage = service.activateAccount(token);

        return ResponseEntity.ok(Map.of("message", activationMessage));

    }

// Was void but because of Async Issues decided to return a response
//    @GetMapping("/activate-account")
//    public void confirm(@RequestParam String token) throws MessagingException {
//        service.activateAccount(token);
//    }
}
