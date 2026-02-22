package com.cal.yughistore.services;

import com.cal.yughistore.model.applicaitonuser.ApplicationUser;
import com.cal.yughistore.model.applicaitonuser.UserSettings;
import com.cal.yughistore.repository.ApplicationUserRepository;
import com.cal.yughistore.repository.UserSettingsRepository;
import com.cal.yughistore.security.JwtTokenProvider;
import com.cal.yughistore.security.exception.UserNotFoundException;
import com.cal.yughistore.services.DTOs.applicationuser.ApplicationUserDTO;
import com.cal.yughistore.services.DTOs.applicationuser.LoginDTO;
import com.cal.yughistore.services.DTOs.applicationuser.UserSettingsDTO;
import com.cal.yughistore.services.exception.UserSettingsNotFoundException;
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
//    private final EmployerRepository employerRepository;
//    private final EtudiantRepository studentRepository;
//    private final GestionnaireRepository gestionnaireRepository;
//    private final TeacherRepository teacherRepository;
    private final UserSettingsRepository userSettingsRepository;

    public ApplicationUserService(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            ApplicationUserRepository applicationUserRepository,
            UserSettingsRepository userSettingsRepository)
    {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.applicationUserRepository = applicationUserRepository;
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
        token = token.startsWith("Bearer") ? token.substring(7) : token;
        String email = jwtTokenProvider.getEmailFromJWT(token);
        ApplicationUser user = applicationUserRepository
                .findApplicationUserByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Étudiant introuvable avec email " + email
                        )
                );
        return ApplicationUserDTO.from(user);
//        return switch (user.getRole()) {
//            case ADMIN -> getEmployerDTO(user.getId());
//            case CLIENT -> getStudentDTO(user.getId());
//        };
    }

//    private EmployerDto getEmployerDTO(Long id) {
//        final Optional<Employer> employerOptional = employerRepository.findById(
//                id
//        );
//        return employerOptional.isPresent()
//                ? EmployerDto.create(employerOptional.get())
//                : EmployerDto.empty();
//    }
//
//    private EtudiantDTO getStudentDTO(Long id) {
//        final Optional<Etudiant> studentOptional = studentRepository.findById(
//                id
//        );
//        return studentOptional.isPresent()
//                ? EtudiantDTO.fromEntity(studentOptional.get())
//                : EtudiantDTO.empty();
//    }
//
//    private GestionnaireDTO getGestionnaireDTO(Long id) {
//        final Optional<Gestionnaire> gestionnaireOptional =
//                gestionnaireRepository.findById(id);
//        return gestionnaireOptional.isPresent()
//                ? GestionnaireDTO.fromEntity(gestionnaireOptional.get())
//                : GestionnaireDTO.empty();
//    }
//
//    private TeacherDTO getTeacherDTO(Long id) {
//        final Optional<Teacher> teacherOptional = teacherRepository.findById(
//                id
//        );
//        return teacherOptional.isPresent()
//                ? TeacherDTO.fromEntity(teacherOptional.get())
//                : TeacherDTO.empty();
//    }

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
