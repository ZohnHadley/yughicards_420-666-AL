package com.cal.yughistore.services.applicaitonuser;

import com.cal.yughistore.model.ShoppingCart;
import com.cal.yughistore.model.applicaitonuser.ClientUser;
import com.cal.yughistore.repository.ShoppingCartRepository;
import com.cal.yughistore.repository.user.ClientUserRepository;
import com.cal.yughistore.services.dto.applicationuser.ApplicationUserDTO;
import com.cal.yughistore.services.dto.shoppingcart.ShoppingCartDTO;
import com.cal.yughistore.services.shoppingcart.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientUserService {
    private static final Logger logger = LoggerFactory.getLogger(
            ClientUserService.class
    );

    private final ClientUserRepository clientUserRepository;
    private final ShoppingCartService shoppingCartService;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClientUserService(ClientUserRepository clientUserRepository, ShoppingCartService shoppingCartService) {
        this.clientUserRepository = clientUserRepository;
        this.shoppingCartService = shoppingCartService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public ApplicationUserDTO userSignup(ApplicationUserDTO applicationUserDTO) {
        try {
            if (
                    clientUserRepository.existsByCredentialsEmail((applicationUserDTO.getEmail())
                    )) {
                throw new RuntimeException("Email already in use");
            }
            System.out.println("service");
            applicationUserDTO.setPassword(passwordEncoder.encode(applicationUserDTO.getPassword()));



            ClientUser savedClientUser = clientUserRepository.save(applicationUserDTO.toClientUser());

//            savedClientUser.getShoppingCart().setApplicationUser(applicationUserDTO.toClientUser());
            shoppingCartService.save(ShoppingCartDTO.of(applicationUserDTO.toClientUser().getShoppingCart()));

            logger.info("Client created = {}", savedClientUser.getEmail());

            return ApplicationUserDTO.of(savedClientUser);
        } catch (Exception e) {
            logger.error("Client signup failed for "+applicationUserDTO.getEmail()+" : {}", e.getMessage());
        }
        return null;
    }

}
