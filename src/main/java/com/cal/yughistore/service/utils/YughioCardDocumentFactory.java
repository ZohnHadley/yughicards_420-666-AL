package com.cal.yughistore.service.utils;

import com.cal.yughistore.model.yughiocard.CardPrices;
import com.cal.yughistore.model.yughiocard.CardSet;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.model.yughiocard.properties.CardProperties;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class YughioCardDocumentFactory {

    private static final String DOCUMENT_SOURCE = "yughio-card";
    private static final String METADATA_SOURCE = "source";
    private static final String METADATA_CARD_ID = "cardId";
    private static final String METADATA_NAME = "name";
    private static final String METADATA_TYPE = "type";
    private static final String METADATA_FRAME_TYPE = "frameType";
    private static final String METADATA_DESCRIPTION = "description";
    private static final String METADATA_QUANTITY = "quantity";

    public Document create(YughioCard card) {
        return new Document(buildContent(card), buildMetadata(card));
    }

    private Map<String, Object> buildMetadata(YughioCard card) {
        return Map.of(
                METADATA_SOURCE, DOCUMENT_SOURCE,
                METADATA_CARD_ID, toTrimmedString(card.getId()),
                METADATA_NAME, toTrimmedString(card.getName()),
                METADATA_TYPE, toTrimmedString(card.getType()),
                METADATA_FRAME_TYPE, toTrimmedString(card.getFrameType()),
                METADATA_DESCRIPTION, toTrimmedString(card.getDescription()),
                METADATA_QUANTITY, toTrimmedString(card.getQuantity())
        );
    }

    private String buildContent(YughioCard card) {
        return """
                card:
                - id: %s
                - api id: %s
                - name: %s
                - type: %s
                - frame type: %s
                - description: %s
                - quantity: %s
                - ygoprodeck url: %s
                - properties: %s
                - prices: %s
                - sets: %s
                """.formatted(
                toTrimmedString(card.getId()),
                toTrimmedString(card.getApi_id()),
                toTrimmedString(card.getName()),
                toTrimmedString(card.getType()),
                toTrimmedString(card.getFrameType()),
                toTrimmedString(card.getDescription()),
                toTrimmedString(card.getQuantity()),
                toTrimmedString(card.getYgoprodeck_url()),
                formatProperties(card.getCardProperties()),
//                formatEmbeddableTexts(card.getCard_prices(), CardPrices::toEmbeddingText),
                formatEmbeddableTexts(card.getCard_sets(), CardSet::toEmbeddingText)
        ).trim();
    }

    private String formatProperties(CardProperties properties) {
        return properties == null ? "" : properties.toEmbeddingText();
    }

    private <T> String formatEmbeddableTexts(List<T> values, Function<T, String> textExtractor) {
        List<T> safeValues = values == null ? List.of() : values;

        return safeValues.stream()
                .map(textExtractor)
                .filter(value -> value != null && !value.isBlank())
                .map(String::trim)
                .collect(Collectors.joining("; "));
    }

    private String toTrimmedString(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}