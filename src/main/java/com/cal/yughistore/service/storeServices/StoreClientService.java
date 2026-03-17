package com.cal.yughistore.service.storeServices;

import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.user.CartItem;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.service.dto.user.CartItemDTO;
import com.cal.yughistore.service.dto.user.ShoppingCartDTO;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.exception.EntityIdentifierNullException;
import com.cal.yughistore.service.exception.storeException.ShoppingCartNotFoundException;
import com.cal.yughistore.service.user.ShoppingCartService;
import com.cal.yughistore.service.YughioCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreClientService {
    private static final Logger logger = LoggerFactory.getLogger(
            StoreClientService.class
    );
    private final ShoppingCartService shoppingCartService;
    private final YughioCardService yughioCardService;

    public StoreClientService(ShoppingCartService shoppingCartService, YughioCardService yughioCardService) {
        this.shoppingCartService = shoppingCartService;
        this.yughioCardService = yughioCardService;
    }

    /// add to user shopping cart
    @Transactional
    public void addToShoppingCart(Long userId, Long cardId, int quantity) {
        if (userId == null) throw new EntityIdentifierNullException(ApplicationUser.class);
        if (cardId == null) throw new EntityIdentifierNullException(YughioCard.class);
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");

        ShoppingCartDTO cart = shoppingCartService.getShoppingCartByUserId(userId);
        if (cart == null) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for userId=" + userId);
        }

        CartItemDTO cartItemDTO = new CartItemDTO();
        YughioCardDTO cardDTO = yughioCardService.getById(cardId);
        cartItemDTO.setCard(cardDTO);

        int alreadyInCart = cart.getCartItemsList().stream()
                .filter(i -> i.getCard().getId().equals(cardId))
                .mapToInt(CartItemDTO::getQuantity)
                .sum();

        if (alreadyInCart + quantity > cardDTO.getQuantity()) {
            throw new IllegalStateException(
                    "Stock insuffisant pour '" + cardDTO.getName() +
                            "'. Stock: " + cardDTO.getQuantity() + ", déjà dans le panier: " + alreadyInCart
            );
        }

        ShoppingCart shoppingCart = cart.toShoppingCart();
        shoppingCart.addCard(cardDTO.toYughioCard(), quantity);
        shoppingCartService.save(ShoppingCartDTO.of(shoppingCart));

        logger.info("Ajouté {}x '{}' au panier userId={}", quantity, cardDTO.getName(), userId);
    }


    @Transactional
    public void removeFromShoppingCart(Long userId, Long cardId) {
        if (userId == null) throw new EntityIdentifierNullException(ApplicationUser.class, "userId can't be null");
        if (cardId == null) throw new EntityIdentifierNullException(YughioCard.class, "cardId can't be null");

        ShoppingCartDTO cartDTO = shoppingCartService.getShoppingCartByUserId(userId);

        if (cartDTO == null) {
            throw new ShoppingCartNotFoundException("Cart not found for userId=" + userId);
        }

        if(cartDTO.getCartItemsList().isEmpty()){
            return;
        }

        ShoppingCart shoppingCart = cartDTO.toShoppingCart();
        shoppingCart.removeOneCard(cardId);
        shoppingCartService.save(ShoppingCartDTO.of(shoppingCart));
    }

    // ── Checkout : décrémente le stock et vide le panier ──────────────────
    @Transactional
    public void buyAllFromShoppingCart(Long userId) {
        ShoppingCartDTO cartDTO = shoppingCartService.getShoppingCartByUserId(userId);
        if (cartDTO == null) throw new ShoppingCartNotFoundException("Cart not found for userId=" + userId);

        System.out.println("🧾 Checkout pour userId=" + userId + " — " + cartDTO.getCartItemsList().size() + " item(s)");
        for (var item : cartDTO.getCartItemsList()) {
            YughioCardDTO card = item.getCard();
            System.out.println("  → id=" + card.getId() + " name='" + card.getName() + "' qty=" + item.getQuantity() + " stock=" + card.getQuantity());
        }

        for (var item : cartDTO.getCartItemsList()) {
            YughioCardDTO card = item.getCard();
            int newQty = Math.max(card.getQuantity() - item.getQuantity(), 0);
            card.setQuantity(newQty);
            yughioCardService.save(card);
            logger.info("Stock '{}' id={} : {} → {}", card.getName(), card.getId(), card.getQuantity() + item.getQuantity(), newQty);
        }

        ShoppingCart cart = cartDTO.toShoppingCart().clearItems();
        shoppingCartService.save(ShoppingCartDTO.of(cart));
    }


    public void clearShoppingCart(Long userId) {
        ShoppingCartDTO cartDTO = shoppingCartService.getShoppingCartByUserId(userId);
        if (cartDTO == null) {
            throw new ShoppingCartNotFoundException("Cart not found for userId=" + userId);
        }

        cartDTO.setCartItemsList(new java.util.ArrayList<>());
        shoppingCartService.save(cartDTO);
    }
}