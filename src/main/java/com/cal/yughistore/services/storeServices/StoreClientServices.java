package com.cal.yughistore.services.storeServices;

import com.cal.yughistore.services.applicaitonuser.ClientUserService;
import com.cal.yughistore.services.dto.shoppingcart.ShoppingCartDTO;
import com.cal.yughistore.services.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.services.shoppingcart.ShoppingCartService;
import com.cal.yughistore.services.yughiocard.YughioCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreClientServices {
    private static final Logger logger = LoggerFactory.getLogger(
            StoreClientServices.class
    );
    private final ClientUserService clientUserService;
    private final ShoppingCartService shoppingCartService;
    private final YughioCardService yughioCardService;

    public StoreClientServices(ClientUserService clientUserService, ShoppingCartService shoppingCartService, YughioCardService yughioCardService) {
        this.clientUserService = clientUserService;
        this.shoppingCartService = shoppingCartService;
        this.yughioCardService = yughioCardService;
    }

    /// get user shopping cart

    public ShoppingCartDTO getShoppingCartByUserID(Long userId) {
        logger.info("Getting shopping cart for user {}", userId);
        return shoppingCartService.getShoppingCartByUserId(userId);
    }

    public ShoppingCartDTO getShoppingCartByUserEmail(String userEmail) {
        logger.info("Getting shopping cart for user {}", userEmail);
        return shoppingCartService.getShoppingCartByUserEmail(userEmail);
    }

    /// add to user shopping cart
    @Transactional
    public void addToShoppingCart(Long userId, Long cardId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId can't be null");
        }
        if (cardId == null) {
            throw new IllegalArgumentException("cardId can't be null");
        }

        ShoppingCartDTO cart = getShoppingCartByUserID(userId);
        if (cart == null) {
            throw new IllegalStateException("Shopping cart not found for userId=" + userId);
        }

        YughioCardDTO card = yughioCardService.getById(cardId);

        // Ensure the list is mutable (Stream#toList() can produce an unmodifiable list).
        if (cart.getCards() == null) {
            cart.setCards(new java.util.ArrayList<>());
        } else if (!(cart.getCards() instanceof java.util.ArrayList<?>)) {
            cart.setCards(new java.util.ArrayList<>(cart.getCards()));
        }

        // Prevent duplicates (optional but usually desired)
        boolean alreadyInCart = cart.getCards().stream()
                .anyMatch(c -> c != null && c.getId() != null && c.getId().equals(cardId));

        if (!alreadyInCart) {
            cart.getCards().add(card);
        }

        shoppingCartService.save(cart);
    }

    @Transactional
    public void removeFromShoppingCart(Long userId, Long cardId) {
        ShoppingCartDTO cart = getShoppingCartByUserID(userId);
        if (cart == null || cart.getCards() == null) {
            return;
        }

        cart.getCards().removeIf(c -> c != null && c.getId() != null && c.getId().equals(cardId));

        shoppingCartService.save(cart);
    }
}