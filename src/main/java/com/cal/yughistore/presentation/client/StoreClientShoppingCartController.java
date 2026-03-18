package com.cal.yughistore.presentation.client;

import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.order.Order;
import com.cal.yughistore.service.dto.user.order.OrderDTO;
import com.cal.yughistore.service.user.ApplicationUserService;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.storeServices.StoreClientService;
import com.cal.yughistore.service.user.Order.OrderService;
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
    private final OrderService orderService;

    @GetMapping("/get")
    public ResponseEntity<List<YughioCardDTO>> getShoppingCart(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return getCartCardsResponse(userId);
    }

    @GetMapping("/add/card={cardId}/quantity={quantity}")
    public ResponseEntity<List<YughioCardDTO>> addToShoppingCart(
            HttpServletRequest request,
            @PathVariable Long cardId,
            @PathVariable(required = false) int quantity
    ) {
        Long userId = getCurrentUserId(request);
        storeClientService.addToShoppingCart(userId, cardId, quantity);

        List<YughioCardDTO> cards = shoppingCartService.getShoppingCartByUserId(userId).getCards();
        System.out.println("🛒 Panier après add: " + cards.size() + " cartes");
        for (YughioCardDTO c : cards) {
            System.out.println("  → " + c.getName() + " (id=" + c.getId() + ")");
        }

        return ResponseEntity.ok(cards);
    }

    @GetMapping("/remove/card={cardId}")
    public ResponseEntity<List<YughioCardDTO>> removeFromShoppingCart(
            HttpServletRequest request,
            @PathVariable Long cardId
    ) {
        Long userId = getCurrentUserId(request);
        storeClientService.removeFromShoppingCart(userId, cardId);
        return getCartCardsResponse(userId);
    }

    @GetMapping("/clear")
    public ResponseEntity<List<YughioCardDTO>> clearShoppingCart(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        storeClientService.clearShoppingCart(userId);
        return getCartCardsResponse(userId);
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = JwtTokenUtils.getTokenFromRequest(request);
        return applicationUserService.getMe(token).getId();
    }

    private ResponseEntity<List<YughioCardDTO>> getCartCardsResponse(Long userId) {
        return ResponseEntity.ok(shoppingCartService.getShoppingCartByUserId(userId).getCards());
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(
            HttpServletRequest request,
            @RequestParam String shippingMethod
    ) {
        Long userId = getCurrentUserId(request);

        // 1. Charge le user et le panier
        ApplicationUser user = applicationUserService.findById(userId);
        ShoppingCart cart = shoppingCartService.getCartEntityByUserId(userId);

        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 2. Crée et persiste la commande AVANT de vider le panier
        Order order = orderService.createFromCart(user, cart, shippingMethod);

        // 3. Décrémente le stock et vide le panier
        storeClientService.buyAllFromShoppingCart(userId);

        // 4. Retourne le DTO de la commande créée
        return ResponseEntity.ok()
                .header("X-Shipping-Method", shippingMethod)
                .body(OrderDTO.from(order));
    }
}