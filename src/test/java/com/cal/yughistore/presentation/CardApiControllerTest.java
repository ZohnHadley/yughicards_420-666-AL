package com.cal.yughistore.presentation;

import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.yughiocard.YughioCardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardApiControllerTest {

    @Mock
    private YughioCardService cardService;

    @InjectMocks
    private CardApiController controller;

    @Test
    void getAllCardsInformation_shouldReturnBasePath() {
        ResponseEntity<String> response = controller.getAllCardsInformation();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("/api/v1", response.getBody());
    }

    @Test
    void getCardInformationByID_shouldDelegateToService() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(1L)
                .name("Dark Magician")
                .build();

        when(cardService.getById(1L)).thenReturn(card);

        ResponseEntity<YughioCardDTO> response = controller.getCardInformationByID(1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(card, response.getBody());
        verify(cardService).getById(1L);
    }

    @Test
    void getNamedCardInformation_shouldDelegateToService() {
        YughioCardDTO card = YughioCardDTO.builder()
                .id(2L)
                .name("Blue-Eyes White Dragon")
                .build();

        when(cardService.getByName("Blue-Eyes White Dragon")).thenReturn(card);

        ResponseEntity<YughioCardDTO> response =
                controller.getNamedCardInformation("Blue-Eyes White Dragon");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(card, response.getBody());
        verify(cardService).getByName("Blue-Eyes White Dragon");
    }

    @Test
    void getCardInformationBySearchName_withExplicitPageSize_shouldDelegateToService() {
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Dark Magician").build(),
                YughioCardDTO.builder().id(2L).name("Dark Magician Girl").build()
        );

        when(cardService.getBySearchName("Dark", 3, 5)).thenReturn(cards);

        ResponseEntity<List<YughioCardDTO>> response =
                controller.getCardInformationBySearchName("Dark", 3, 5);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cards, response.getBody());
        verify(cardService).getBySearchName("Dark", 3, 5);
    }

    @Test
    void getCardInformationBySearchName_withoutExplicitPageSize_shouldUseDefaultPageSize() {
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Dark Magician").build()
        );

        when(cardService.getBySearchName("Dark", 2, 10)).thenReturn(cards);

        ResponseEntity<List<YughioCardDTO>> response =
                controller.getCardInformationBySearchName("Dark", 2);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cards, response.getBody());
        verify(cardService).getBySearchName("Dark", 2, 10);
    }

    @Test
    void getAllCardsPagedInformation_shouldDelegateToService() {
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Card A").build()
        );

        when(cardService.getAllPaged(1, 20)).thenReturn(cards);

        ResponseEntity<List<YughioCardDTO>> response =
                controller.getAllCardsPagedInformation(1, 20);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cards, response.getBody());
        verify(cardService).getAllPaged(1, 20);
    }

    @Test
    void getAllCardsPagedInformation_numberOfCardsWithSetPage_shouldUsePageZero() {
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Card A").build()
        );

        when(cardService.getAllPaged(0, 15)).thenReturn(cards);

        ResponseEntity<List<YughioCardDTO>> response =
                controller.getAllCardsPagedInformation_numberOfCardsWithSetPage(15);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cards, response.getBody());
        verify(cardService).getAllPaged(0, 15);
    }

    @Test
    void getAllCardsPagedInformation_pageWithSetNumberOfCards_shouldUseDefaultPageSize() {
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Card A").build()
        );

        when(cardService.getAllPaged(4, 10)).thenReturn(cards);

        ResponseEntity<List<YughioCardDTO>> response =
                controller.getAllCardsPagedInformation_pageWithSetNumberOfCards(4);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cards, response.getBody());
        verify(cardService).getAllPaged(4, 10);
    }

    @Test
    void getAllCardsPagedInformationByFrameType_withExplicitPageSize_shouldDelegateToService() {
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Fusion Card").build()
        );

        when(cardService.getByFrameTypePaged("fusion", 1, 8)).thenReturn(cards);

        ResponseEntity<List<YughioCardDTO>> response =
                controller.getAllCardsPagedInformationByFrameType("fusion", 1, 8);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cards, response.getBody());
        verify(cardService).getByFrameTypePaged("fusion", 1, 8);
    }

    @Test
    void getAllCardsPagedInformationByFrameType_withoutExplicitPageSize_shouldUseDefaultPageSize() {
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Fusion Card").build()
        );

        when(cardService.getByFrameTypePaged("fusion", 1, 10)).thenReturn(cards);

        ResponseEntity<List<YughioCardDTO>> response =
                controller.getAllCardsPagedInformationByFrameType("fusion", 1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cards, response.getBody());
        verify(cardService).getByFrameTypePaged("fusion", 1, 10);
    }

    @Test
    void getAllCardsPagedInformationByCardType_withExplicitPageSize_shouldDelegateToService() {
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Spell Card").build()
        );

        when(cardService.getByTypePaged("Spell Card", 0, 6)).thenReturn(cards);

        ResponseEntity<List<YughioCardDTO>> response =
                controller.getAllCardsPagedInformationByCardType("Spell Card", 0, 6);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cards, response.getBody());
        verify(cardService).getByTypePaged("Spell Card", 0, 6);
    }

    @Test
    void getAllCardsPagedInformationByCardType_withoutExplicitPageSize_shouldUseDefaultPageSize() {
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Trap Card").build()
        );

        when(cardService.getByTypePaged("Trap Card", 2, 10)).thenReturn(cards);

        ResponseEntity<List<YughioCardDTO>> response =
                controller.getAllCardsPagedInformationByCardType("Trap Card", 2);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cards, response.getBody());
        verify(cardService).getByTypePaged("Trap Card", 2, 10);
    }

    @Test
    void getAllVersionsOfCard_shouldDelegateToService() {
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Dark Magician").setCode("SDK-001").build(),
                YughioCardDTO.builder().id(2L).name("Dark Magician").setCode("LOB-005").build()
        );

        when(cardService.getAllVersionsOfCard("Dark Magician")).thenReturn(cards);

        ResponseEntity<List<YughioCardDTO>> response =
                controller.getAllVersionsOfCard("Dark Magician");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cards, response.getBody());
        verify(cardService).getAllVersionsOfCard("Dark Magician");
    }
}