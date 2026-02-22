package com.cal.yughistore.services.user;

import com.cal.yughistore.model.applicaitonuser.ClientUser;
import com.cal.yughistore.repository.user.ClientUserRepository;
import com.cal.yughistore.services.dto.applicationuser.ClientUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientUserService {
    private static final Logger logger = LoggerFactory.getLogger(
            ClientUserService.class
    );

    private final ClientUserRepository clientUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClientUserService(ClientUserRepository clientUserRepository) {
        this.clientUserRepository = clientUserRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public ClientUserDTO registerUser(ClientUserDTO clienUsertDTO) {
        if (
                clientUserRepository.existsByCredentialsEmail((clienUsertDTO.getEmail())
                )) {
            throw new RuntimeException("Email already in use");
        }
        System.out.println("service");
        ClientUser savedClientUser = clientUserRepository.save(clienUsertDTO.toClientUser());
        logger.info("Student created = {}", savedClientUser.getEmail());

        return ClientUserDTO.of(savedClientUser);
    }

}
