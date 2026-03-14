package com.cal.yughistore.service.user;

import com.cal.yughistore.model.user.AdminUser;
import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.user.ClientUser;
import com.cal.yughistore.model.user.UserSettings;
import com.cal.yughistore.repository.user.AdminUserRepository;
import com.cal.yughistore.repository.user.ApplicationUserRepository;
import com.cal.yughistore.repository.user.ClientUserRepository;
import com.cal.yughistore.repository.user.UserSettingsRepository;
import com.cal.yughistore.security.JwtTokenProvider;
import com.cal.yughistore.security.exceptions.UserNotFoundException;
import com.cal.yughistore.service.dto.user.*;
import com.cal.yughistore.service.exception.userExceptions.UserSettingsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ApplicationUserService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationUserRepository applicationUserRepository;
    private final AdminUserRepository adminUserRepository;
    private final ClientUserRepository clientUserRepository;
    private final UserSettingsRepository userSettingsRepository;

    public ApplicationUserService(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            ApplicationUserRepository applicationUserRepository,
            AdminUserRepository adminUserRepository,
            ClientUserRepository clientUserRepository,
            UserSettingsRepository userSettingsRepository)
    {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.applicationUserRepository = applicationUserRepository;

        this.adminUserRepository = adminUserRepository;
        this.clientUserRepository = clientUserRepository;

        this.userSettingsRepository = userSettingsRepository;
    }

    public String authenticateUser(LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        final String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    public ApplicationUserDTO getMe(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = jwtTokenProvider.getEmailFromJWT(token);
        ApplicationUser user = applicationUserRepository
                .findApplicationUserByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Étudiant introuvable avec email " + email
                        )
                );
        return switch (user.getRole()) {
            case GUEST -> getGuestDTO(user.getId());
            case ADMIN -> getAdminDTO(user.getId());
            case CLIENT -> getClientDTO(user.getId());
        };
    }

    private ApplicationUserDTO getGuestDTO(Long id) {
        return ApplicationUserDTO.builder().build();
//        final Optional<GuestUser> adminUserOptional = adminUserRepository.findById(
//                id
//        );
//        return adminUserOptional.isPresent()
//                ? ApplicationUserDTO.of(adminUserOptional.get())
//                : new ApplicationUserDTO();
    }

    private ApplicationUserDTO getAdminDTO(Long id) {
        final Optional<AdminUser> adminUserOptional = adminUserRepository.findById(
                id
        );
        return adminUserOptional.isPresent()
                ? ApplicationUserDTO.of(adminUserOptional.get())
                : new ApplicationUserDTO();
    }

    private ApplicationUserDTO getClientDTO(Long id) {
        final Optional<ClientUser> clientOptional = clientUserRepository.findById(
                id
        );
        return clientOptional.isPresent()
                ? ApplicationUserDTO.of(clientOptional.get())
                : new ApplicationUserDTO();
    }

    public UserSettingsDTO getMySettings(Long userId) {
        UserSettings settings = userSettingsRepository.findByUserId(userId);
        if (settings == null) {
            throw new UserSettingsNotFoundException(
                    "Aucun paramètre trouvé pour l'utilisateur avec l'ID " + userId
            );
        }
        return UserSettingsDTO.fromEntity(settings);
    }

    @Transactional
    public UserSettingsDTO updateMySettings(Long userId, UserSettingsDTO dto) {
        ApplicationUser user = applicationUserRepository
                .findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Utilisateur introuvable avec id " + userId
                        )
                );

        UserSettings settings = userSettingsRepository.findByUserId(userId);
        if (settings == null) {
            settings = new UserSettings();
            settings.setUser(user);
        }

        if (dto.getLanguage() != null) {
            settings.setLanguage(dto.getLanguage());
        }

        userSettingsRepository.save(settings);
        return UserSettingsDTO.fromEntity(settings);
    }
}
