package com.cal.yughistore.service.user;

import com.cal.yughistore.model.user.ClientUser;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.repository.user.ShoppingCartRepository;
import com.cal.yughistore.repository.card.YughioCardRepository;
import com.cal.yughistore.repository.user.ApplicationUserRepository;
import com.cal.yughistore.service.dto.user.ShoppingCartDTO;
import com.cal.yughistore.service.exception.EntityIdentifierNullException;
import com.cal.yughistore.service.exception.storeException.ShoppingCartNotFoundException;
import com.cal.yughistore.service.exception.storeException.ShoppingCartNotSavedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ShoppingCartService {
    private static final Logger logger = LoggerFactory.getLogger(
            ShoppingCartService.class
    );
    private final ShoppingCartRepository shoppingCartRepository;
    private final YughioCardRepository yughioCardRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public ShoppingCartService(
            ShoppingCartRepository shoppingCartRepository,
            YughioCardRepository yughioCardRepository,
            ApplicationUserRepository applicationUserRepository
    ) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.yughioCardRepository = yughioCardRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Transactional
    public ShoppingCartDTO save(ShoppingCartDTO shoppingCartDTO) {
        if (shoppingCartDTO == null) {
            throw new ShoppingCartNotSavedException("Shopping cart DTO is null or missing ID");
        }
        if (shoppingCartDTO.getApplicationUser() == null) {
            throw new ShoppingCartNotSavedException("Shopping cart DTO is null or missing ID");
        }

        Optional<ShoppingCart> cart = shoppingCartRepository.findById(shoppingCartDTO.getId());
        if (cart.isEmpty()) {
            ShoppingCart savedCart = shoppingCartRepository.save(shoppingCartDTO.toShoppingCart());
            return ShoppingCartDTO.of(savedCart);

        } else {
            return ShoppingCartDTO.of(cart.get());
        }
    }

    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserId(Long userId) {
        if (userId == null) {
            throw new EntityIdentifierNullException(ClientUser.class, "can't be null");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByApplicationUser_Id(userId);
        if (shoppingCart == null) {
            return new ShoppingCartDTO();
        }

        logger.debug("Shopping cart for user {}: {}", userId, shoppingCart);
        return ShoppingCartDTO.of(shoppingCart);
    }

    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return null;
        }
        ShoppingCart shoppingCart = shoppingCartRepository.findByApplicationUser_Credentials_Email(userEmail);
        if (shoppingCart == null) {
            return new ShoppingCartDTO();
        }
        logger.info("Shopping cart for user {}: {}", userEmail, shoppingCart);
        return ShoppingCartDTO.of(shoppingCart);
    }
}