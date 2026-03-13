package com.cal.yughistore.service;
import com.cal.yughistore.repository.card.YughioCardRepository;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.utils.YughioCardVectorStoreUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {

    @Mock
    private YughioCardVectorStoreUtil vectorStoreUtil;

    @Mock
    private YughioCardRepository cardRepository;

    @Mock
    private YughioCardService yughioCardService;

    @Mock
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    @SuppressWarnings("rawtypes")
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private ApiService apiService;

    @BeforeEach
    void setUp() {
        when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);

        apiService = new ApiService(
                vectorStoreUtil,
                cardRepository,
                yughioCardService,
                restClientBuilder,
                new ObjectMapper()
        );
    }

    @Test
    void init_shouldSkipLoadingWhenCardsAlreadyExist() {
        when(cardRepository.count()).thenReturn(5L);

        apiService.init();

        verify(cardRepository).count();
        verify(restClient, never()).get();
        verify(yughioCardService, never()).saveAll(any());
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void init_shouldLoadCardsFromApiWhenRepositoryIsEmpty() {
        String responseJson = """
                {
                  "data": [
                    {
                      "id": 123,
                      "name": "Blue-Eyes White Dragon",
                      "type": "Normal Monster",
                      "frameType": "normal",
                      "desc": "Legendary dragon",
                      "atk": 3000,
                      "def": 2500,
                      "level": 8,
                      "race": "Dragon",
                      "attribute": "LIGHT",
                      "ygoprodeck_url": "https://example.com/card/123",
                      "card_images": [
                        {
                          "id": 123,
                          "image_url": "https://example.com/image.jpg",
                          "image_url_small": "https://example.com/image-small.jpg",
                          "image_url_cropped": "https://example.com/image-cropped.jpg"
                        }
                      ],
                      "card_prices": [
                        {
                          "cardmarket_price": "1.00",
                          "tcgplayer_price": "2.00",
                          "ebay_price": "3.00",
                          "amazon_price": "4.00",
                          "coolstuffinc_price": "5.00"
                        }
                      ]
                    }
                  ]
                }
                """;

        when(cardRepository.count()).thenReturn(0L);
        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/cardinfo.php")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseJson);

        apiService.init();

        ArgumentCaptor<List<YughioCardDTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(yughioCardService).saveAll(captor.capture());

        List<YughioCardDTO> savedCards = captor.getValue();
        assertEquals(1, savedCards.size());
        assertEquals(123, savedCards.get(0).getApi_id());
        assertEquals("Blue-Eyes White Dragon", savedCards.get(0).getName());
        assertEquals(0, savedCards.get(0).getQuantity());
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void init_shouldFallbackToStaticFileWhenApiCallFails() {
        when(cardRepository.count()).thenReturn(0L);
        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/cardinfo.php"))
                .thenReturn((RestClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenThrow(new RuntimeException("API unavailable"));

        apiService.init();

        ArgumentCaptor<List<YughioCardDTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(yughioCardService).saveAll(captor.capture());

        List<YughioCardDTO> savedCards = captor.getValue();
        assertEquals(1, savedCards.size());
        assertEquals(456, savedCards.get(0).getApi_id());
        assertEquals("Dark Magician", savedCards.get(0).getName());
    }
}