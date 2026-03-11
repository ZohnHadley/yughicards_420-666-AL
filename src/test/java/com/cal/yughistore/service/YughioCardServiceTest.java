package com.cal.yughistore.service;


import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.repository.card.*;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.exception.EntityDTONullException;
import com.cal.yughistore.service.exception.EntityIdentifierNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class YughioCardServiceTest {

    @Mock
    private YughioCardRepository cardRepository;
    @Mock
    private CardPropertiesRepository cardPropertiesRepository;
    @Mock
    private CardImagesRepository cardImagesRepository;
    @Mock
    private CardPricesRepository cardPriceRepository;
    @Mock
    private CardSetRepository cardSetRepository;

    @Mock
    private YughioCardService yughioCardService;

    @BeforeEach
    void setUp() {
        yughioCardService = new YughioCardService(
                cardRepository,
                cardPropertiesRepository,
                cardImagesRepository,
                cardPriceRepository,
                cardSetRepository
        );
    }


    //region card service saving tests
    @Test
    void save_shouldReturnSavedCardDto() {
        YughioCardDTO dtoCard = YughioCardDTO.builder()
                .name("Test Card")
                .quantity(0)
                .build();

        YughioCard savedEntity = YughioCard.builder()
                .id(1L)
                .name("Test Card")
                .quantity(0)
                .build();

        when(cardRepository.save(any(YughioCard.class))).thenReturn(savedEntity);

        YughioCardDTO savedCard = yughioCardService.save(dtoCard);

        assertNotNull(savedCard.getId());
        assertEquals(1L, savedCard.getId());
        assertEquals("Test Card", savedCard.getName());

        verify(cardRepository).save(any(YughioCard.class));
    }

    @Test
    void save_null_shouldThrowEntityDTONullException() {
        YughioCardDTO dtoCard = null;

        assertThrows(EntityDTONullException.class, () -> yughioCardService.save(dtoCard));

        verify(cardRepository, never()).save(any(YughioCard.class));
    }

    @Test
    void saveAll_shouldReturnSavedCardDTOList() {
        YughioCardDTO dtoCard1 = YughioCardDTO.builder()
                .name("Test Card")
                .quantity(0)
                .build();

        YughioCard savedEntity = YughioCard.builder()
                .id(1L)
                .name("Test Card")
                .quantity(0)
                .build();

        when(cardRepository.save(any(YughioCard.class))).thenReturn(savedEntity);

        List<YughioCardDTO> dtoCards = List.of(dtoCard1);
        List<YughioCardDTO> savedCards = yughioCardService.saveAll(dtoCards);

        assertNotNull(savedCards);
        assertEquals(1, savedCards.size());
        assertEquals("Test Card", savedCards.getFirst().getName());
    }

    @Test
    void saveAll_nullList_shouldThrowEntityDTONullException() {
        List<YughioCardDTO> dtoCards = null;
        assertThrows(EntityDTONullException.class, () -> yughioCardService.saveAll(dtoCards));
    }

    @Test
    void saveAll_emptyList_shouldReturnEmptyList() {
        List<YughioCardDTO> dtoCards = List.of();

        List<YughioCardDTO> savedCards = yughioCardService.saveAll(dtoCards);

        assertNotNull(savedCards);
        assertEquals(0, savedCards.size());
    }
    //endregion

    //region card service getById tests
    @Test
    void getById_shouldReturnCardDTO() {
        YughioCard yughioCard = YughioCard.builder()
                .id(1L)
                .name("Test Card")
                .quantity(0)
                .build();

        when(cardRepository.findById(1L)).thenReturn(Optional.of(yughioCard));

        YughioCardDTO cardDTO = yughioCardService.getById(1L);

        assertNotNull(cardDTO);
        assertEquals(1L, cardDTO.getId());
        assertEquals("Test Card", cardDTO.getName());
    }

    @Test
    void getById_nonExistingId_shouldThrowRuntimeException() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityIdentifierNullException.class, () -> yughioCardService.getById(1L));
    }

    @Test
    void getById_nullId_shouldThrowEntityIdentifierNullException() {
        assertThrows(EntityIdentifierNullException.class, () -> yughioCardService.getById(null));
    }
    //endregion

    @Test
    void getSearchByName_shouldReturnCardDTO() {
        YughioCard yughioCard = YughioCard.builder()
                .id(1L)
                .name("Test Card")
                .quantity(0)
                .build();

        Pageable pageable = PageRequest.of(0, 1);
        Page<YughioCard> cards = new PageImpl<>(List.of(yughioCard));

        when(cardRepository.findByNameContainingIgnoreCase("Test Card", pageable))
        .thenReturn(cards);

        List<YughioCardDTO> cardDTOs = yughioCardService.getSearchByName("Test Card", 0, 1);

        assertNotNull(cardDTOs);
        assertEquals(1, cardDTOs.size());
        assertEquals("Test Card", cardDTOs.getFirst().getName());
    }
}
