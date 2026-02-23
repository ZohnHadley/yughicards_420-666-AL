package com.cal.yughistore.services.user;

import com.cal.yughistore.model.applicaitonuser.AdminUser;
import com.cal.yughistore.services.dto.applicationuser.AdminUserDTO;
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

    public AdminUserDTO signup(AdminUserDTO adminUserDTO) {
        if (
                adminUserRepository.existsByCredentialsEmail((adminUserDTO.getEmail())
                )) {
            throw new RuntimeException("Email already in use");
        }
        System.out.println("service");
        AdminUser savedClientUser = adminUserRepository.save(adminUserDTO.toAdminUser());
        logger.info("Admin created = {}", savedClientUser.getEmail());

        return AdminUserDTO.of(savedClientUser);
    }
}
