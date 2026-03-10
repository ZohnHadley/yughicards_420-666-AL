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
        try {
            if (shoppingCartDTO == null || shoppingCartDTO.getId() == null) {
                throw new ShoppingCartNotSavedException("Shopping cart DTO is null or missing ID");
            }

            ShoppingCart cart = shoppingCartRepository.findById(shoppingCartDTO.getId())
                    .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart not found: id=" + shoppingCartDTO.getId()));

            // IMPORTANT: never attach a "new ApplicationUser()" stub.
            // If DTO provides a user id, attach a managed reference.
            if (shoppingCartDTO.getApplicationUser() != null && shoppingCartDTO.getApplicationUser().getId() != null) {
                Long userId = shoppingCartDTO.getApplicationUser().getId();
                ApplicationUser userRef = applicationUserRepository.getReferenceById(userId);
                cart.setApplicationUser(userRef);
            }
            // else: keep existing cart.applicationUser as-is

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
            logger.debug("Saved shopping cart id={}", savedShoppingCart.getId());
            return ShoppingCartDTO.of(savedShoppingCart);
        } catch (Exception e) {
            logger.error("Error saving shopping cart: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserId(Long userId) {
        if (userId == null) {
            throw new EntityIdentifierNullException(ClientUser.class ,"can't be null");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByApplicationUser_Id(userId);

        if (shoppingCart == null) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for userId=" + userId);
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
            throw new ShoppingCartNotFoundException("Shopping cart not found in db for userEmail=" + userEmail);
        }
        logger.info("Shopping cart for user {}: {}", userEmail, shoppingCart);
        return ShoppingCartDTO.of(shoppingCart);
    }
}