package com.cal.yughistore.presentation;

import com.cal.yughistore.services.applicaitonuser.ApplicationUserService;
import com.cal.yughistore.services.dto.shoppingcart.ShoppingCartDTO;
import com.cal.yughistore.services.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.services.shoppingcart.ShoppingCartService;
import com.cal.yughistore.services.storeServices.StoreClientServices;
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
public class ShoppingCartController {

    private final StoreClientServices storeClientServices;
    private final ApplicationUserService applicationUserService;

    ///
    @PostMapping("/get")
    public ResponseEntity<List<YughioCardDTO>> getShoppingCart(HttpServletRequest request){
        Long id = applicationUserService.getMe(JwtTokenUtils.getTokenFromRequest(request)).getId();
        storeClientServices.getShoppingCartByUserID(id);
        return ResponseEntity.ok(storeClientServices.getShoppingCartByUserID(id).getCards());
    }

    @PostMapping("/add")
    public ResponseEntity<List<YughioCardDTO>> addToShoppingCart(HttpServletRequest request, @RequestBody Long cardId){
        Long id = applicationUserService.getMe(JwtTokenUtils.getTokenFromRequest(request)).getId();
        storeClientServices.addToShoppingCart(id, cardId);
        return ResponseEntity.ok(storeClientServices.getShoppingCartByUserID(id).getCards());
    }

    @PostMapping("/remove")
    public ResponseEntity<List<YughioCardDTO>> removeFromShoppingCart(HttpServletRequest request, @RequestBody Long cardId){
        Long id = applicationUserService.getMe(JwtTokenUtils.getTokenFromRequest(request)).getId();
        storeClientServices.removeFromShoppingCart(id, cardId);
        return ResponseEntity.ok(storeClientServices.getShoppingCartByUserID(id).getCards());
    }
}
