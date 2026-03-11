package com.cal.yughistore.service.utils;


import com.cal.yughistore.event.PasswordResetRequestEvent;
import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.user.auth.Credentials;
import com.cal.yughistore.repository.user.ApplicationUserRepository;
import com.cal.yughistore.security.JwtTokenProvider;
import com.cal.yughistore.security.exceptions.InvalidJwtTokenException;
import com.cal.yughistore.security.exceptions.UserNotFoundException;
import com.cal.yughistore.service.user.ApplicationUserService;
import com.cal.yughistore.service.dto.user.ApplicationUserDTO;
import com.cal.yughistore.service.dto.user.LoginDTO;
import com.cal.yughistore.service.dto.user.PasswordRequestDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService userAppService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationEventPublisher eventPublisher;


    private final Map<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_TIME = Duration.ofMinutes(15);


    private record LoginAttempt(int attempts, LocalDateTime lastAttempt) {}

    public String userSigning(LoginDTO LoginDTO) {

        String email = LoginDTO.getEmail();
        String password = LoginDTO.getPassword();

        if (email == null || email.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Veuillez entrer un courriel.");
        }

        if (password == null || password.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Veuillez entrer un mot de passe.");
        }

        if (isAccountLocked(email)) {
            throw new LockedException(
                    "Compte verrouillé après " + MAX_ATTEMPTS + " tentatives. Réessayez dans 15 minutes."
            );
        }

        ApplicationUser user = applicationUserRepository.findApplicationUserByEmail(email)
                .orElseThrow();
        if (!passwordEncoder.matches(LoginDTO.getPassword(), user.getPassword())) {
            registerFailedAttempt(email);
            throw new BadCredentialsException("Votre courriel ou mot de passe est erroné.");
        }

        loginAttempts.remove(email);
        logger.info("Login successful for user {}", email);
        return userAppService.authenticateUser(LoginDTO);
    }

    private boolean isAccountLocked(String email) {
        LoginAttempt attempt = loginAttempts.get(email);
        if (attempt == null) return false;

        if (attempt.attempts() < MAX_ATTEMPTS) return false;

        if (Duration.between(attempt.lastAttempt(), LocalDateTime.now()).compareTo(LOCK_TIME) > 0) {
            loginAttempts.remove(email);
            return false;
        }
        return true;
    }

    private void registerFailedAttempt(String email) {
        loginAttempts.compute(email, (key, attempt) -> {
            if (attempt == null) {
                return new LoginAttempt(1, LocalDateTime.now());
            }
            return new LoginAttempt(attempt.attempts() + 1, LocalDateTime.now());
        });
    }

    @Transactional
    public void userPasswordReset(PasswordRequestDTO PasswordRequestDTO) {
        try {
            String email = jwtTokenProvider.getEmailFromJWT(PasswordRequestDTO.getToken());
            ApplicationUser user = applicationUserRepository.findApplicationUserByEmail(email)
                    .orElseThrow();

            Credentials newCredentials = Credentials.builder()
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(PasswordRequestDTO.getNewPassword()))
                    .role(user.getRole())
                    .build();

            user.setCredentials(newCredentials);
            applicationUserRepository.save(user);
        }
        catch (Exception e) {
            throw new InvalidJwtTokenException(HttpStatus.UNAUTHORIZED, "Token invalide ou expiré.");
        }
    }

    public void userPasswordResetRequest(String email) {
        ApplicationUser user = applicationUserRepository.findApplicationUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Étudiant introuvable avec email " + email));

        ApplicationUserDTO userDTO = new ApplicationUserDTO(user);

        String resetToken = jwtTokenProvider.generatePasswordResetToken(user.getEmail());

        eventPublisher.publishEvent(new PasswordResetRequestEvent(userDTO, resetToken));
    }
}
