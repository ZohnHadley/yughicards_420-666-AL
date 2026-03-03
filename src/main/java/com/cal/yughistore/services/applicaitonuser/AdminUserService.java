package com.cal.yughistore.services.applicaitonuser;

import com.cal.yughistore.model.applicaitonuser.AdminUser;
import com.cal.yughistore.services.dto.applicationuser.ApplicationUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {
    private static final Logger logger = LoggerFactory.getLogger(
            AdminUserService.class
    );

    private final com.cal.yughistore.repository.user.AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserService(com.cal.yughistore.repository.user.AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public ApplicationUserDTO userSignup(ApplicationUserDTO applicationUserDTO) {
        try {
            if (
                    adminUserRepository.existsByCredentialsEmail((applicationUserDTO.getEmail())
                    )) {
                throw new RuntimeException("Email already in use");
            }
            System.out.println("service");
            applicationUserDTO.setPassword(passwordEncoder.encode(applicationUserDTO.getPassword()));
            AdminUser savedClientUser = adminUserRepository.save(applicationUserDTO.toAdminUser());
            logger.info("Admin created = {}", savedClientUser.getEmail());

            return ApplicationUserDTO.of(savedClientUser);
        } catch (Exception e) {
            logger.error("Admin signup failed "+applicationUserDTO.getEmail()+" : {}",e.getMessage());
        }
        return null;
    }
}
