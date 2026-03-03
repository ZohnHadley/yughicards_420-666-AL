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

    public ShoppingCartDTO getShoppingCart(Long userId) {
        try {
            logger.info("Getting shopping cart for user {}", userId);
            return shoppingCartService.getShoppingCartByUserId(userId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public ShoppingCartDTO getShoppingCart(String userEmail) {
        try {
            logger.info("Getting shopping cart for user {}", userEmail);
            return shoppingCartService.getShoppingCartByUserEmail(userEmail);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /// add to user shopping cart
    @Transactional
    public ShoppingCartDTO addToShoppingCart(Long userId, Long cardId) {
        try {
            ShoppingCartDTO cart = shoppingCartService.getShoppingCartByUserId(userId);
            YughioCardDTO card = yughioCardService.getById(cardId);

            if (cart == null || card == null) {
                return null;
            }
            if (cart.getCards() == null) {
                cart.setCards(new java.util.ArrayList<>());
            }

            boolean alreadyInCart = cart.getCards().stream()
                    .anyMatch(c -> c != null && c.getId() != null && c.getId().equals(cardId));

            if (!alreadyInCart) {
                cart.getCards().add(card);
            }

            return shoppingCartService.save(cart);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Transactional
    public ShoppingCartDTO removeFromShoppingCart(Long userId, Long cardId) {
        try {
            ShoppingCartDTO cart = shoppingCartService.getShoppingCartByUserId(userId);
            if (cart == null || cart.getCards() == null) {
                return null;
            }

            cart.getCards().removeIf(c -> c != null && c.getId() != null && c.getId().equals(cardId));

            return shoppingCartService.save(cart);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
