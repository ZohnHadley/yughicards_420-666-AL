package com.cal.yughistore.services.user;

import com.cal.yughistore.model.applicaitonuser.ClientUser;
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

    private final com.cal.yughistore.repository.user.ClientUserRepository clientUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClientUserService(com.cal.yughistore.repository.user.ClientUserRepository clientUserRepository) {
        this.clientUserRepository = clientUserRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public ClientUserDTO userSignup(ClientUserDTO clientUserDTO) {
        if (
                clientUserRepository.existsByCredentialsEmail((clientUserDTO.getEmail())
                )) {
            throw new RuntimeException("Email already in use");
        }
        System.out.println("service");
        clientUserDTO.setPassword(passwordEncoder.encode(clientUserDTO.getPassword()));
        ClientUser savedClientUser = clientUserRepository.save(clientUserDTO.toClientUser());
        logger.info("Client created = {}", savedClientUser.getEmail());

        return ClientUserDTO.of(savedClientUser);
    }

}
