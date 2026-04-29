package org.chronyqor.integration;

import org.chronyqor.dto.auth.AuthRequest;
import org.chronyqor.dto.auth.AuthResponse;
import org.chronyqor.dto.auth.RegisterRequest;
import org.chronyqor.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldLockAccountAfterFiveFailedAttempts() {
        // Register user
        String username = "lockme";
        RegisterRequest regRequest = new RegisterRequest(username, "lock@example.com", "correctPassword");
        restTemplate.postForEntity("/api/v1/auth/register", regRequest, AuthResponse.class);

        // Fail login 5 times
        AuthRequest wrongRequest = new AuthRequest(username, "wrongPassword");
        for (int i = 0; i < 5; i++) {
            restTemplate.postForEntity("/api/v1/auth/login", wrongRequest, Void.class);
        }

        // Try correct password - should be locked
        AuthRequest correctRequest = new AuthRequest(username, "correctPassword");
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/v1/auth/login", correctRequest, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
    }
}
