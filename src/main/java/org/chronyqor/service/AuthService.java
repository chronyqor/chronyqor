package org.chronyqor.service;

import lombok.RequiredArgsConstructor;
import org.chronyqor.domain.Role;
import org.chronyqor.domain.User;
import org.chronyqor.dto.auth.*;
import org.chronyqor.repository.UserRepository;
import org.chronyqor.security.jwt.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }
        if (repository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        var user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        repository.save(user);

        emailService.sendEmail(user.getEmail(), "Welcome to Chronyqor!", 
            "Hello " + user.getUsername() + ",\n\nWelcome to Chronyqor! Your account has been created successfully.");

        var jwtToken = jwtUtils.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return new AuthResponse(jwtToken, refreshToken.getToken(), user.getUsername());
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = repository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isAccountNonLocked()) {
            throw new LockedException("Account is locked due to too many failed attempts");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );
            
            // Reset failed attempts on success
            user.setFailedLoginAttempts(0);
            repository.save(user);

        } catch (BadCredentialsException e) {
            int newAttempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(newAttempts);
            if (newAttempts >= 5) {
                user.setAccountNonLocked(false);
            }
            repository.save(user);
            throw e;
        }

        var jwtToken = jwtUtils.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return new AuthResponse(jwtToken, refreshToken.getToken(), user.getUsername());
    }

    public AuthResponse refreshToken(TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.refreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(org.chronyqor.domain.RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateToken(user);
                    return new AuthResponse(token, request.refreshToken(), user.getUsername());
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}
