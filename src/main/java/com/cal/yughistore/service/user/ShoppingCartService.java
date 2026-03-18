package com.cal.yughistore.service.user;

import com.cal.yughistore.model.user.ClientUser;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.repository.user.ShoppingCartRepository;
import com.cal.yughistore.repository.user.ApplicationUserRepository;
import com.cal.yughistore.service.dto.user.ShoppingCartDTO;
import com.cal.yughistore.service.exception.EntityIdentifierNullException;
import com.cal.yughistore.service.exception.storeException.ShoppingCartNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingCartService {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    private final ShoppingCartRepository shoppingCartRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public ShoppingCartService(
            ShoppingCartRepository shoppingCartRepository,
            ApplicationUserRepository applicationUserRepository
    ) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    // ── Get by userId ─────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserId(Long userId) {
        if (userId == null) {
            throw new EntityIdentifierNullException(ClientUser.class, "can't be null");
        }
        ShoppingCart cart = shoppingCartRepository.findByApplicationUser_Id(userId);
        if (cart == null) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for userId=" + userId);
        }
        logger.debug("Shopping cart for user {}: {} items", userId, cart.getItems().size());
        return ShoppingCartDTO.of(cart);
    }

    // ── Get by email ──────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) return null;

        ShoppingCart cart = shoppingCartRepository.findByApplicationUser_Credentials_Email(userEmail);
        if (cart == null) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for email=" + userEmail);
        }
        logger.info("Shopping cart for user {}: {} items", userEmail, cart.getItems().size());
        return ShoppingCartDTO.of(cart);
    }

    @Transactional(readOnly = true)
    public ShoppingCart getCartEntityByUserId(Long userId) {
        if (userId == null) throw new EntityIdentifierNullException(ClientUser.class, "can't be null");
        ShoppingCart cart = shoppingCartRepository.findByApplicationUser_Id(userId);
        if (cart == null) throw new ShoppingCartNotFoundException("Shopping cart not found for userId=" + userId);
        return cart;
    }
}