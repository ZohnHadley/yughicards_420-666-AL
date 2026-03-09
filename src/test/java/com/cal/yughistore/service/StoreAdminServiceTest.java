package com.cal.yughistore.service;

import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.dto.yughiocard.cardProperties.CardPropertiesDTO;
import com.cal.yughistore.service.storeServices.StoreAdminService;
import com.cal.yughistore.service.yughiocard.YughioCardService;
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

    private YughioCardDTO stockCard(Long id, int quantity) {
        return YughioCardDTO.builder()
                .id(id)
                .quantity(quantity)
                .cardProperties(new CardPropertiesDTO())
                .build();
    }

    @Test
    void setCardStock_withDto_shouldReturnNullWhenDtoIsNull() {
        YughioCardDTO result = storeAdminService.setCardStock((YughioCardDTO) null, 10);

        assertNull(result);
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void setCardStock_withDto_shouldThrowWhenQuantityIsZeroOrLess() {
        YughioCardDTO card = stockCard(1L, 5);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeAdminService.setCardStock(card, 0)
        );

        assertEquals("quantity must be greater than 0", exception.getMessage());
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void setCardStock_withDto_shouldUpdateStock() {
        YughioCardDTO loadedCard = stockCard(1L, 5);

        when(yughioCardService.getById(1L)).thenReturn(loadedCard);

        YughioCardDTO result = storeAdminService.setCardStock(stockCard(1L, 99), 20);

        assertNotNull(result);
        assertEquals(20, result.getQuantity());
        assertEquals(1L, result.getId());
        assertNotSame(loadedCard, result);
        verify(yughioCardService).getById(1L);
        verifyNoMoreInteractions(yughioCardService);
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
    void setCardStock_withId_shouldLoadAndUpdateStock() {
        YughioCardDTO loadedCard = stockCard(1L, 3);

        when(yughioCardService.getById(1L)).thenReturn(loadedCard);

        YughioCardDTO result = storeAdminService.setCardStock(1L, 15);

        assertNotNull(result);
        assertEquals(15, result.getQuantity());
        assertEquals(1L, result.getId());
        assertNotSame(loadedCard, result);
        verify(yughioCardService).getById(1L);
        verifyNoMoreInteractions(yughioCardService);
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
    void incrementCardStock_shouldIncreaseStock() {
        YughioCardDTO loadedCard = stockCard(1L, 10);

        when(yughioCardService.getById(1L)).thenReturn(loadedCard);

        YughioCardDTO result = storeAdminService.incrementCardStock(1L, 7);

        assertNotNull(result);
        assertEquals(17, result.getQuantity());
        assertEquals(1L, result.getId());
        verify(yughioCardService, times(2)).getById(1L);
        verifyNoMoreInteractions(yughioCardService);
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
    void decrementCardStock_shouldDecreaseStock() {
        YughioCardDTO loadedCard = stockCard(1L, 10);

        when(yughioCardService.getById(1L)).thenReturn(loadedCard);

        YughioCardDTO result = storeAdminService.decrementCardStock(1L, 4);

        assertNotNull(result);
        assertEquals(6, result.getQuantity());
        assertEquals(1L, result.getId());
        verify(yughioCardService, times(2)).getById(1L);
        verifyNoMoreInteractions(yughioCardService);
    }

    @Test
    void updateCardById_shouldReturnNullWhenCardIdIsNull() {
        YughioCardDTO result = storeAdminService.updateCardById(null);

        assertNull(result);
        verifyNoInteractions(yughioCardService);
    }

    @Test
    void updateCardById_shouldLoadAndSaveCard() {
        YughioCardDTO card = stockCard(1L, 10);

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