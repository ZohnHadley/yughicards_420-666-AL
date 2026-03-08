package com.cal.yughistore;

import com.cal.yughistore.services.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.services.storeServices.StoreAdminService;
import com.cal.yughistore.services.yughiocard.YughioCardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreAdminServiceTest {

    @Mock
    private YughioCardService yughioCardService;

    @InjectMocks
    private StoreAdminService storeAdminService;

    @Test
    void setCardStock_withDto_shouldReturnNullWhenDtoIsNull() {
        YughioCardDTO result = storeAdminService.setCardStock((YughioCardDTO) null, 10);

        assertNull(result);
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void setCardStock_withDto_shouldThrowWhenQuantityIsZeroOrLess() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(1L)
                .quantity(5)
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeAdminService.setCardStock(card, 0)
        );

        assertEquals("quantity must be greater than 0", exception.getMessage());
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void setCardStock_withDto_shouldUpdateStockAndSave() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(1L)
                .quantity(5)
                .build();

        when(yughioCardService.save(card)).thenReturn(card);

        YughioCardDTO result = storeAdminService.setCardStock(card, 20);

        assertNotNull(result);
        assertEquals(20, card.getQuantity());
        assertSame(card, result);
        verify(yughioCardService).save(card);
    }

    @Test
    void setCardStock_withId_shouldReturnNullWhenCardIdIsNull() {
        YughioCardDTO result = storeAdminService.setCardStock((Long) null, 10);

        assertNull(result);
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void setCardStock_withId_shouldThrowWhenQuantityIsZeroOrLess() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeAdminService.setCardStock(1L, -1)
        );

        assertEquals("quantity must be greater than 0", exception.getMessage());
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void setCardStock_withId_shouldLoadUpdateAndSave() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(1L)
                .quantity(3)
                .build();

        when(yughioCardService.getById(1L)).thenReturn(card);
        when(yughioCardService.save(card)).thenReturn(card);

        YughioCardDTO result = storeAdminService.setCardStock(1L, 15);

        assertNotNull(result);
        assertEquals(15, card.getQuantity());
        assertSame(card, result);
        verify(yughioCardService).getById(1L);
        verify(yughioCardService).save(card);
    }

    @Test
    void incrementCardStock_shouldReturnNullWhenCardIdIsNull() {
        YughioCardDTO result = storeAdminService.incrementCardStock(null, 5);

        assertNull(result);
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void incrementCardStock_shouldThrowWhenQuantityIsZeroOrLess() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeAdminService.incrementCardStock(1L, 0)
        );

        assertEquals("quantity must be greater than 0", exception.getMessage());
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void incrementCardStock_shouldIncreaseStockAndSave() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(1L)
                .quantity(10)
                .build();

        when(yughioCardService.getById(1L)).thenReturn(card);
        when(yughioCardService.save(card)).thenReturn(card);

        YughioCardDTO result = storeAdminService.incrementCardStock(1L, 7);

        assertNotNull(result);
        assertEquals(17, card.getQuantity());
        assertSame(card, result);
        verify(yughioCardService).getById(1L);
        verify(yughioCardService).save(card);
    }

    @Test
    void decrementCardStock_shouldReturnNullWhenCardIdIsNull() {
        YughioCardDTO result = storeAdminService.decrementCardStock(null, 5);

        assertNull(result);
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void decrementCardStock_shouldThrowWhenQuantityIsZeroOrLess() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeAdminService.decrementCardStock(1L, 0)
        );

        assertEquals("quantity must be greater than 0", exception.getMessage());
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void decrementCardStock_shouldDecreaseStockAndSave() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(1L)
                .quantity(10)
                .build();

        when(yughioCardService.getById(1L)).thenReturn(card);
        when(yughioCardService.save(card)).thenReturn(card);

        YughioCardDTO result = storeAdminService.decrementCardStock(1L, 4);

        assertNotNull(result);
        assertEquals(6, card.getQuantity());
        assertSame(card, result);
        verify(yughioCardService).getById(1L);
        verify(yughioCardService).save(card);
    }

    @Test
    void updateCardById_shouldReturnNullWhenCardIdIsNull() {
        YughioCardDTO result = storeAdminService.updateCardById(null);

        assertNull(result);
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void updateCardById_shouldLoadAndSaveCard() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(1L)
                .quantity(10)
                .build();

        when(yughioCardService.getById(1L)).thenReturn(card);
        when(yughioCardService.save(card)).thenReturn(card);

        YughioCardDTO result = storeAdminService.updateCardById(1L);

        assertNotNull(result);
        assertSame(card, result);
        verify(yughioCardService).getById(1L);
        verify(yughioCardService).save(card);
    }

    @Test
    void deleteCardById_shouldReturnNullWhenCardIdIsNull() {
        Boolean result = storeAdminService.deleteCardById(null);

        assertNull(result);
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void deleteCardById_shouldDelegateToService() {
        when(yughioCardService.deleteById(1L)).thenReturn(true);

        Boolean result = storeAdminService.deleteCardById(1L);

        assertNotNull(result);
        assertTrue(result);
        verify(yughioCardService).deleteById(1L);
    }
}