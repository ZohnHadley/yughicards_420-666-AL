package com.cal.yughistore.presentation.admin;

import com.cal.yughistore.services.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.services.storeServices.StoreAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class StoreCardStockController {

    private final StoreAdminService storeAdminService;

    @PostMapping("/cards/cardId={cardId}/quantity={quantity}")
    public ResponseEntity<YughioCardDTO> setCardStock(
            @PathVariable Long cardId,
            @PathVariable int quantity
    ) {
        validateCardId(cardId);
        validatePositiveQuantity(quantity);
        return ResponseEntity.ok(storeAdminService.setCardStock(cardId, quantity));
    }

    @PatchMapping("/cards/cardId={cardId}/quantity={quantity}/increment")
    public ResponseEntity<YughioCardDTO> incrementCardStock(
            @PathVariable Long cardId,
            @PathVariable int quantity
    ) {
        validateCardId(cardId);
        validatePositiveQuantity(quantity);
        return ResponseEntity.ok(storeAdminService.incrementCardStock(cardId, quantity));
    }

    @PatchMapping("/cards/cardId={cardId}/quantity={quantity}/decrement")
    public ResponseEntity<YughioCardDTO> decrementCardStock(
            @PathVariable Long cardId,
            @PathVariable int quantity
    ) {
        validateCardId(cardId);
        validatePositiveQuantity(quantity);
        return ResponseEntity.ok(storeAdminService.decrementCardStock(cardId, quantity));
    }

    @PutMapping("/cards/cardId={cardId}")
    public ResponseEntity<YughioCardDTO> updateCard(@PathVariable Long cardId) {
        validateCardId(cardId);
        return ResponseEntity.ok(storeAdminService.updateCardById(cardId));
    }

    @DeleteMapping("/cards/cardId={cardId}")
    public ResponseEntity<Boolean> deleteCardById(@PathVariable Long cardId) {
        validateCardId(cardId);
        return ResponseEntity.ok(storeAdminService.deleteCardById(cardId));
    }

    private void validateCardId(Long cardId) {
        if (cardId == null) {
            throw new IllegalArgumentException("cardId must not be null");
        }
    }

    private void validatePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
    }
}