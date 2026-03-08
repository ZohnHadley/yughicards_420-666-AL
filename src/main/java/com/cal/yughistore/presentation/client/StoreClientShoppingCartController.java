package com.cal.yughistore.presentation.client;

import com.cal.yughistore.service.applicaitonuser.ApplicationUserService;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.storeServices.StoreClientService;
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

    private final StoreClientService storeClientService;
    private final ApplicationUserService applicationUserService;

    @GetMapping("/get")
    public ResponseEntity<List<YughioCardDTO>> getShoppingCart(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return getCartCardsResponse(userId);
    }

    @GetMapping("/add/card={cardId}")
    public ResponseEntity<List<YughioCardDTO>> addToShoppingCart(
            HttpServletRequest request,
            @PathVariable Long cardId
    ) {
        Long userId = getCurrentUserId(request);
        storeClientService.addToShoppingCart(userId, cardId);
        return getCartCardsResponse(userId);
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
        return ResponseEntity.ok(storeClientService.getShoppingCartByUserID(userId).getCards());
    }
}