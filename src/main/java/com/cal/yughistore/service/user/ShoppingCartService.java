package com.cal.yughistore.service.user;

import com.cal.yughistore.model.user.CartItem;
import com.cal.yughistore.model.user.ClientUser;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.repository.user.CartItemRepository;
import com.cal.yughistore.repository.user.ClientUserRepository;
import com.cal.yughistore.repository.user.ShoppingCartRepository;
import com.cal.yughistore.repository.card.YughioCardRepository;
import com.cal.yughistore.repository.user.ApplicationUserRepository;
import com.cal.yughistore.service.dto.user.ApplicationUserDTO;
import com.cal.yughistore.service.dto.user.CartItemDTO;
import com.cal.yughistore.service.dto.user.ShoppingCartDTO;
import com.cal.yughistore.service.exception.EntityIdentifierNullException;
import com.cal.yughistore.service.exception.storeException.ShoppingCartNotFoundException;
import com.cal.yughistore.service.exception.storeException.ShoppingCartNotSavedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class ShoppingCartService {
    private static final Logger logger = LoggerFactory.getLogger(
            ShoppingCartService.class
    );

    private final YughioCardRepository yughioCardRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final CartItemRepository cartItemRepository;

    public ShoppingCartService(
            ShoppingCartRepository shoppingCartRepository,
            CartItemRepository cartItemRepository,
            YughioCardRepository yughioCardRepository,
            ApplicationUserRepository applicationUserRepository
    ) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        this.yughioCardRepository = yughioCardRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Transactional
    public ShoppingCartDTO save(ShoppingCartDTO shoppingCartDTO) {
        if (shoppingCartDTO == null) {
            throw new ShoppingCartNotSavedException("Shopping cart DTO is null or missing ID");
        }
        if (shoppingCartDTO.getApplicationUserId() == null) {
            throw new ShoppingCartNotSavedException("Shopping cart DTO is null or missing application user");
        }

        if (!applicationUserRepository.existsById(shoppingCartDTO.getApplicationUserId())) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for userId=" + shoppingCartDTO.getApplicationUserId());
        }

        ApplicationUser savedUser = applicationUserRepository.findById(shoppingCartDTO.getApplicationUserId()).orElseThrow();
        ShoppingCart shoppingCart = shoppingCartDTO.toShoppingCart();
        shoppingCart.setApplicationUser(savedUser);

        //save all cart items
        for (CartItem cartItem : shoppingCart.getCartItemList()) {
            cartItem.setShoppingCart(shoppingCart);
            cartItemRepository.save(cartItem);
        }

        ShoppingCart savedCart = shoppingCartRepository.save(shoppingCart);
        return ShoppingCartDTO.of(savedCart);
    }

    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserId(Long userId) {
        if (userId == null) {
            throw new EntityIdentifierNullException(ApplicationUser.class, "can't be null");
        }
        if (!applicationUserRepository.existsById(userId)) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for userId=" + userId);
        }

        Optional<ApplicationUser> savedUser = applicationUserRepository.findById(userId);
        if (savedUser.isEmpty()) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for userId=" + userId);
        }
        ShoppingCart shoppingCart = shoppingCartRepository.findByApplicationUser_Id(userId);
        shoppingCart.setApplicationUser(savedUser.get());

        logger.debug("Shopping cart for user {}: {}", userId, shoppingCart);
        return ShoppingCartDTO.of(shoppingCart);
    }

    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            throw new EntityIdentifierNullException(ApplicationUser.class, "can't be null");
        }
        Optional<ApplicationUser> savedUser = applicationUserRepository.findApplicationUserByEmail(userEmail);
        if (savedUser.isEmpty()) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for userEmail=" + userEmail);
        }
        ShoppingCart shoppingCart = shoppingCartRepository.findByApplicationUser_Credentials_Email(userEmail);
        shoppingCart.setApplicationUser(savedUser.get());

        logger.info("Shopping cart for user {}: {}", userEmail, shoppingCart);
        return ShoppingCartDTO.of(shoppingCart);
    }
}