package com.cal.yughistore.service.user;

import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.ClientUser;
import com.cal.yughistore.repository.user.ShoppingCartRepository;
import com.cal.yughistore.repository.user.ClientUserRepository;
import com.cal.yughistore.service.dto.user.ApplicationUserDTO;
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
    private final ShoppingCartRepository shoppingCartRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClientUserService(ClientUserRepository clientUserRepository, ShoppingCartRepository shoppingCartRepository) {
        this.clientUserRepository = clientUserRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public ApplicationUserDTO save(ApplicationUserDTO applicationUserDTO) {
        if (applicationUserDTO == null) {
            throw new IllegalArgumentException("applicationUserDTO must not be null");
        }
        if (applicationUserDTO.getPassword() == null || applicationUserDTO.getPassword().isBlank()) {
            throw new IllegalArgumentException("password must not be blank");
        }
        if (applicationUserDTO.getEmail() == null || applicationUserDTO.getEmail().isBlank()) {
            throw new IllegalArgumentException("email must not be blank");
        }
        if (clientUserRepository.existsByCredentialsEmail(applicationUserDTO.getEmail())) {
            throw new IllegalStateException("email is already in use");
        }

        applicationUserDTO.setPassword(passwordEncoder.encode(applicationUserDTO.getPassword()));

        ClientUser clientUserToSave = applicationUserDTO.toClientUser();

        // IMPORTANT: do NOT create/set ShoppingCart before saving the user
        clientUserToSave.setShoppingCart(null);

        ClientUser savedClientUser = clientUserRepository.save(clientUserToSave);

        ShoppingCart cart = new ShoppingCart();
        cart.setApplicationUser(savedClientUser);     // must be set (NOT NULL FK)
        savedClientUser.setShoppingCart(cart);        // keep both sides consistent in memory

        shoppingCartRepository.save(cart);

        logger.info("Client created = {}", savedClientUser.getEmail());

        ApplicationUserDTO result = ApplicationUserDTO.of(savedClientUser);
        result.setPassword(null);
        return result;
    }
}
