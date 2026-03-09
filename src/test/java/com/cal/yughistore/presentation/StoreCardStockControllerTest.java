package com.cal.yughistore.presentation;

import com.cal.yughistore.presentation.storeController.StoreCardStockController;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.storeServices.StoreAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreCardStockControllerTest {

    @Mock
    private StoreAdminService storeAdminService;

    @InjectMocks
    private StoreCardStockController controller;

    @Test
    void setCardStock_shouldReturnUpdatedCard() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(1L)
                .name("Dark Magician")
                .quantity(25)
                .build();

        when(storeAdminService.setCardStock(1L, 25)).thenReturn(card);

        ResponseEntity<YughioCardDTO> response = controller.setCardStock(1L, 25);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(card, response.getBody());
        verify(storeAdminService).setCardStock(1L, 25);
    }

    @Test
    void setCardStock_shouldThrowWhenCardIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.setCardStock(null, 10)
        );

        assertEquals("cardId must not be null", exception.getMessage());
        verifyNoInteractions(storeAdminService);
    }

    @Test
    void setCardStock_shouldThrowWhenQuantityIsNotPositive() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.setCardStock(1L, 0)
        );

        assertEquals("quantity must be greater than 0", exception.getMessage());
        verifyNoInteractions(storeAdminService);
    }

    @Test
    void incrementCardStock_shouldReturnUpdatedCard() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(2L)
                .name("Blue-Eyes White Dragon")
                .quantity(11)
                .build();

        when(storeAdminService.incrementCardStock(2L, 3)).thenReturn(card);

        ResponseEntity<YughioCardDTO> response = controller.incrementCardStock(2L, 3);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(card, response.getBody());
        verify(storeAdminService).incrementCardStock(2L, 3);
    }

    @Test
    void incrementCardStock_shouldThrowWhenQuantityIsNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.incrementCardStock(2L, -1)
        );

        assertEquals("quantity must be greater than 0", exception.getMessage());
        verifyNoInteractions(storeAdminService);
    }

    @Test
    void decrementCardStock_shouldReturnUpdatedCard() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(3L)
                .name("Red-Eyes Black Dragon")
                .quantity(4)
                .build();

        when(storeAdminService.decrementCardStock(3L, 2)).thenReturn(card);

        ResponseEntity<YughioCardDTO> response = controller.decrementCardStock(3L, 2);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(card, response.getBody());
        verify(storeAdminService).decrementCardStock(3L, 2);
    }

    @Test
    void decrementCardStock_shouldThrowWhenCardIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.decrementCardStock(null, 2)
        );

        assertEquals("cardId must not be null", exception.getMessage());
        verifyNoInteractions(storeAdminService);
    }

    @Test
    void updateCard_shouldDelegateToService() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(4L)
                .name("Exodia")
                .build();

        when(storeAdminService.updateCardById(4L)).thenReturn(card);

        ResponseEntity<YughioCardDTO> response = controller.updateCard(4L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(card, response.getBody());
        verify(storeAdminService).updateCardById(4L);
    }

    @Test
    void updateCard_shouldThrowWhenCardIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.updateCard(null)
        );

        assertEquals("cardId must not be null", exception.getMessage());
        verifyNoInteractions(storeAdminService);
    }

    @Test
    void deleteCardById_shouldDelegateToService() {
        when(storeAdminService.deleteCardById(5L)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.deleteCardById(5L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(true, response.getBody());
        verify(storeAdminService).deleteCardById(5L);
    }

    @Test
    void deleteCardById_shouldThrowWhenCardIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.deleteCardById(null)
        );

        assertEquals("cardId must not be null", exception.getMessage());
        verifyNoInteractions(storeAdminService);
    }
}