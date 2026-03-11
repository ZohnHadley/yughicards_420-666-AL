package com.cal.yughistore.service.storeServices;

import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.yughiocard.YughioCard;
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
        if (userId == null) {
            throw new EntityIdentifierNullException(ApplicationUser.class);
        }
        if (cardId == null) {
            throw new EntityIdentifierNullException(YughioCard.class);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("card quantity must be greater than 0");
        }

        ShoppingCartDTO cart = shoppingCartService.getShoppingCartByUserId(userId);
        if (cart == null) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for userId=" + userId);
        }

        YughioCardDTO card = yughioCardService.getById(cardId);
        card.setQuantity(card.getQuantity());

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
        if (userId == null) {
            throw new EntityIdentifierNullException(ApplicationUser.class,"userId can't be null");
        }
        if (cardId == null) {
            throw new EntityIdentifierNullException(YughioCard.class,"cardId can't be null");
        }

        ShoppingCartDTO cart = shoppingCartService.getShoppingCartByUserId(userId);

        if (cart == null) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for userId=" + userId);
        }

        int index = 0;
        for (YughioCardDTO card : cart.getCards()) {
            if (card.getId() != null && card.getId().equals(cardId)) {
                index = cart.getCards().indexOf(card);
                break;
            }
        }
        cart.getCards().remove(index);

        shoppingCartService.save(cart);
    }

//    @Transactional
//    public void buyAllFromShoppingCart(){
//        //decrements all cards in shopping cart by 1
//        for (YughioCardDTO card : shoppingCartService.getShoppingCartByUserId(1L).getCards()) {
//            int newQuantity = card.getQuantity() - 1;
//            yughioCardService.updateQuantity(card,);
//        }
//    }

    public void clearShoppingCart(Long id) {
        ShoppingCartDTO cart = shoppingCartService.getShoppingCartByUserId(id);
        cart.setCards(new java.util.ArrayList<>());
        shoppingCartService.save(cart);
    }
}