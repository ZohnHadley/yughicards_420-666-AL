package com.cal.yughistore.presentation.storeController;

import com.cal.yughistore.model.user.CartItem;
import com.cal.yughistore.service.dto.user.CartItemDTO;
import com.cal.yughistore.service.user.ApplicationUserService;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.storeServices.StoreClientService;
import com.cal.yughistore.service.user.ShoppingCartService;
import com.cal.yughistore.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class StoreClientShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final StoreClientService storeClientService;
    private final ApplicationUserService applicationUserService;

    @GetMapping("/get")
    public ResponseEntity<List<CartItemDTO>> getShoppingCart(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return getCartCardsResponse(userId);
    }

    @GetMapping("/add/card={cardId}/quantity={quantity}")
    public ResponseEntity<List<CartItemDTO>> addToShoppingCart(
            HttpServletRequest request,
            @PathVariable Long cardId,
            @PathVariable(required = false) int quantity
    ) {
        Long userId = getCurrentUserId(request);
        storeClientService.addToShoppingCart(userId, cardId, quantity);

        List<CartItemDTO> cartItems = shoppingCartService.getShoppingCartByUserId(userId).getCartItemsList();
        System.out.println("🛒 Panier après add: " + cartItems.size() + " cartes");
        for (CartItemDTO c : cartItems) {
            System.out.println("  → " + c.getCard().getName() + " (id=" + c.getId() + ")");
        }

        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/remove/card={cardId}")
    public ResponseEntity<List<CartItemDTO>> removeFromShoppingCart(
            HttpServletRequest request,
            @PathVariable Long cardId
    ) {
        Long userId = getCurrentUserId(request);
        storeClientService.removeFromShoppingCart(userId, cardId);
        return getCartCardsResponse(userId);
    }

    @GetMapping("/clear")
    public ResponseEntity<List<CartItemDTO>> clearShoppingCart(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        storeClientService.clearShoppingCart(userId);
        return getCartCardsResponse(userId);
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = JwtTokenUtils.getTokenFromRequest(request);
        return applicationUserService.getMe(token).getId();
    }

    private ResponseEntity<List<CartItemDTO>> getCartCardsResponse(Long userId) {
        return ResponseEntity.ok(shoppingCartService.getShoppingCartByUserId(userId).getCartItemsList());
    }

    @PostMapping("/checkout")
    public ResponseEntity<List<CartItemDTO>> checkout(
            HttpServletRequest request,
            @RequestParam String shippingMethod   // "pickup" ou "ship"
    ) {
        Long userId = getCurrentUserId(request);

        // 1. Récupère les cartes avant de vider
        List<CartItemDTO> purchasedCards = shoppingCartService
                .getShoppingCartByUserId(userId)
                .getCartItemsList();

        // 2. Décrémente le stock ET vide le panier
        storeClientService.buyAllFromShoppingCart(userId);

        // 3. Retourne les cartes achetées + méthode de livraison dans le header
        return ResponseEntity.ok()
                .header("X-Shipping-Method", shippingMethod)
                .body(purchasedCards);
    }
}