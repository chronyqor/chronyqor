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

public class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() {
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "password123");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("/api/v1/auth/register", request, AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().username()).isEqualTo("testuser");
        assertThat(response.getBody().token()).isNotEmpty();
        assertThat(response.getBody().refreshToken()).isNotEmpty();
    }

    @Test
    void shouldLoginUser() {
        // Register first
        RegisterRequest regRequest = new RegisterRequest("loginuser", "login@example.com", "password123");
        restTemplate.postForEntity("/api/v1/auth/register", regRequest, AuthResponse.class);

        // Then login
        AuthRequest loginRequest = new AuthRequest("loginuser", "password123");
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("/api/v1/auth/login", loginRequest, AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().token()).isNotEmpty();
    }

    @Test
    void shouldFailLoginWithWrongPassword() {
        RegisterRequest regRequest = new RegisterRequest("wrongpass", "wrong@example.com", "password123");
        restTemplate.postForEntity("/api/v1/auth/register", regRequest, AuthResponse.class);

        AuthRequest loginRequest = new AuthRequest("wrongpass", "WRONGPASSWORD");
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/v1/auth/login", loginRequest, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
