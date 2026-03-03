package com.cal.yughistore.services.shoppingcart;

import com.cal.yughistore.model.ShoppingCart;
import com.cal.yughistore.model.applicaitonuser.ApplicationUser;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.repository.ShoppingCartRepository;
import com.cal.yughistore.repository.YughioCardRepository;
import com.cal.yughistore.services.dto.shoppingcart.ShoppingCartDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingCartService {
    private static final Logger logger = LoggerFactory.getLogger(
            ShoppingCartService.class
    );
    private final ShoppingCartRepository shoppingCartRepository;
    private final YughioCardRepository yughioCardRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, YughioCardRepository yughioCardRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.yughioCardRepository = yughioCardRepository;
    }

    @Transactional
    public ShoppingCartDTO save(ShoppingCartDTO shoppingCartDTO) {
        try {
            if (shoppingCartDTO == null || shoppingCartDTO.getId() == null) {
                return null;
            }

            ShoppingCart cart = shoppingCartRepository.findById(shoppingCartDTO.getId())
                    .orElseThrow(() -> new IllegalStateException("Shopping cart not found: id=" + shoppingCartDTO.getId()));

            if (shoppingCartDTO.getApplicationUser() != null && shoppingCartDTO.getApplicationUser().getId() != null) {
                ApplicationUser userRef = new ApplicationUser();
                userRef.setId(shoppingCartDTO.getApplicationUser().getId());
                cart.setApplicationUser(userRef);
            }

            cart.getCardList().clear();
            if (shoppingCartDTO.getCards() != null) {
                for (var cardDto : shoppingCartDTO.getCards()) {
                    if (cardDto != null && cardDto.getId() != null) {
                        YughioCard cardRef = yughioCardRepository.getReferenceById(cardDto.getId());
                        cart.getCardList().add(cardRef);
                    }
                }
            }

            ShoppingCart savedShoppingCart = shoppingCartRepository.save(cart);
            logger.info("Saved shopping cart: {}", savedShoppingCart);
            return ShoppingCartDTO.of(savedShoppingCart);
        } catch (Exception e) {
            logger.error("Error saving shopping cart: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserId(Long userId) {
        if (userId == null) {
            return null;
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByApplicationUser_Id(userId);
        logger.info("Shopping cart for user {}: {}", userId, shoppingCart);
        return ShoppingCartDTO.of(shoppingCart);
    }

    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return null;
        }
        ShoppingCart shoppingCart = shoppingCartRepository.findByApplicationUser_Credentials_Email(userEmail);
        logger.info("Shopping cart for user {}: {}", userEmail, shoppingCart);
        return ShoppingCartDTO.of(shoppingCart);
    }
}
