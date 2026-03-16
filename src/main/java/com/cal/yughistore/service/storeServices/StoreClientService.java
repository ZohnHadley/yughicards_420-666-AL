package com.cal.yughistore.service.storeServices;

import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.repository.user.ShoppingCartRepository;
import com.cal.yughistore.repository.card.YughioCardRepository;
import com.cal.yughistore.service.exception.EntityIdentifierNullException;
import com.cal.yughistore.service.exception.storeException.ShoppingCartNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreClientService {
    private static final Logger logger = LoggerFactory.getLogger(StoreClientService.class);

    private final ShoppingCartRepository shoppingCartRepository;
    private final YughioCardRepository yughioCardRepository;

    public StoreClientService(
            ShoppingCartRepository shoppingCartRepository,
            YughioCardRepository yughioCardRepository
    ) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.yughioCardRepository = yughioCardRepository;
    }

    // ── Ajoute qty exemplaires d'une carte au panier ──────────────────────
    @Transactional
    public void addToShoppingCart(Long userId, Long cardId, int quantity) {
        if (userId == null) throw new EntityIdentifierNullException(ApplicationUser.class);
        if (cardId == null) throw new EntityIdentifierNullException(YughioCard.class);
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");

        ShoppingCart cart = shoppingCartRepository.findByApplicationUser_Id(userId);
        if (cart == null) throw new ShoppingCartNotFoundException("Cart not found for userId=" + userId);

        YughioCard card = yughioCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Carte introuvable id=" + cardId));

        // Calcule combien est déjà dans le panier
        int alreadyInCart = cart.getItems().stream()
                .filter(i -> i.getCard().getId().equals(cardId))
                .mapToInt(i -> i.getQuantity())
                .sum();

        if (alreadyInCart + quantity > card.getQuantity()) {
            throw new IllegalStateException(
                    "Stock insuffisant pour '" + card.getName() +
                            "'. Stock: " + card.getQuantity() + ", déjà dans le panier: " + alreadyInCart
            );
        }

        cart.addCard(card, quantity);
        shoppingCartRepository.save(cart);
        logger.info("Ajouté {}x '{}' au panier userId={}", quantity, card.getName(), userId);
    }

    // ── Retire UNE occurrence de la carte ─────────────────────────────────
    @Transactional
    public void removeFromShoppingCart(Long userId, Long cardId) {
        if (userId == null) throw new EntityIdentifierNullException(ApplicationUser.class, "userId can't be null");
        if (cardId == null) throw new EntityIdentifierNullException(YughioCard.class, "cardId can't be null");

        ShoppingCart cart = shoppingCartRepository.findByApplicationUser_Id(userId);
        if (cart == null) throw new ShoppingCartNotFoundException("Cart not found for userId=" + userId);

        cart.removeOneCard(cardId);
        shoppingCartRepository.save(cart);
    }

    // ── Checkout : décrémente le stock et vide le panier ──────────────────
    @Transactional
    public void buyAllFromShoppingCart(Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByApplicationUser_Id(userId);
        if (cart == null) throw new ShoppingCartNotFoundException("Cart not found for userId=" + userId);

        System.out.println("🧾 Checkout pour userId=" + userId + " — " + cart.getItems().size() + " item(s)");
        for (var item : cart.getItems()) {
            YughioCard card = item.getCard();
            System.out.println("  → id=" + card.getId() + " name='" + card.getName() + "' qty=" + item.getQuantity() + " stock=" + card.getQuantity());
        }

        for (var item : cart.getItems()) {
            YughioCard card = item.getCard();
            int newQty = Math.max(card.getQuantity() - item.getQuantity(), 0);
            card.setQuantity(newQty);
            yughioCardRepository.save(card);
            logger.info("Stock '{}' id={} : {} → {}", card.getName(), card.getId(), card.getQuantity() + item.getQuantity(), newQty);
        }

        cart.clearItems();
        shoppingCartRepository.save(cart);
    }

    // ── Vide le panier sans toucher au stock ──────────────────────────────
    @Transactional
    public void clearShoppingCart(Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByApplicationUser_Id(userId);
        if (cart == null) throw new ShoppingCartNotFoundException("Cart not found for userId=" + userId);
        cart.clearItems();
        shoppingCartRepository.save(cart);
    }
}