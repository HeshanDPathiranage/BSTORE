package com.example.BSTORE.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static final String ADMIN_SESSION_KEY = "ADMIN_USER";

    @Value("${admin.username:admin}")
    private String configuredAdminUsername;

    @Value("${admin.password:admin123}")
    private String configuredAdminPassword;

    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest() {}

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * Authenticate admin using constant-time comparison against configured credentials.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) LoginRequest request, HttpSession session) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Username and password are required"));
        }

        byte[] inputUser = request.getUsername().trim().getBytes(StandardCharsets.UTF_8);
        byte[] expectedUser = configuredAdminUsername.trim().getBytes(StandardCharsets.UTF_8);
        boolean usernameMatches = MessageDigest.isEqual(inputUser, expectedUser);

        byte[] inputPass = request.getPassword().getBytes(StandardCharsets.UTF_8);
        byte[] expectedPass = configuredAdminPassword.getBytes(StandardCharsets.UTF_8);
        boolean passwordMatches = MessageDigest.isEqual(inputPass, expectedPass);

        if (!usernameMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }

        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Wrong password"));
        }

        // Store authenticated admin in session
        session.setAttribute(ADMIN_SESSION_KEY, configuredAdminUsername);
        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "username", configuredAdminUsername
        ));
    }

    /**
     * Check if current session is authenticated as admin.
     */
    @GetMapping("/status")
    public ResponseEntity<?> status(HttpSession session) {
        Object adminUser = session.getAttribute(ADMIN_SESSION_KEY);
        if (adminUser != null) {
            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "username", adminUser.toString()
            ));
        }
        return ResponseEntity.ok(Map.of("authenticated", false));
    }

    /**
     * Logout and invalidate session.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.removeAttribute(ADMIN_SESSION_KEY);
        try {
            session.invalidate();
        } catch (IllegalStateException ignored) {
            // Session may already be invalidated
        }
        return ResponseEntity.ok(Map.of(
                "authenticated", false,
                "message", "Logged out successfully"
        ));
    }
}
