package com.cal.yughistore.services.applicaitonuser;

import com.cal.yughistore.model.ShoppingCart;
import com.cal.yughistore.model.applicaitonuser.ClientUser;
import com.cal.yughistore.services.dto.applicationuser.ApplicationUserDTO;
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

    public ApplicationUserDTO userSignup(ApplicationUserDTO applicationUserDTO) {
        try {
            if (
                    clientUserRepository.existsByCredentialsEmail((applicationUserDTO.getEmail())
                    )) {
                throw new RuntimeException("Email already in use");
            }
            System.out.println("service");
            applicationUserDTO.setPassword(passwordEncoder.encode(applicationUserDTO.getPassword()));
            applicationUserDTO.setShoppingCart(new ShoppingCart());
            ClientUser savedClientUser = clientUserRepository.save(applicationUserDTO.toClientUser());
            logger.info("Client created = {}", savedClientUser.getEmail());

            return ApplicationUserDTO.of(savedClientUser);
        } catch (Exception e) {
            logger.error("Client signup failed for "+applicationUserDTO.getEmail()+" : {}", e.getMessage());
        }
        return null;
    }

}
