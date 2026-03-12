package com.cal.yughistore.service.utils;

import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.repository.card.YughioCardRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class YughioCardVectorStoreUtil {

    private static final Logger logger = LoggerFactory.getLogger(YughioCardVectorStoreUtil.class);
    private static final int BATCH_SIZE = 250;
    private static final String DOCUMENT_SOURCE = "yughio-card";

    private final VectorStore vectorStore;
    private final YughioCardRepository cardRepository;

    public YughioCardVectorStoreUtil(
            VectorStore vectorStore,
            YughioCardRepository cardRepository
    ) {
        this.vectorStore = vectorStore;
        this.cardRepository = cardRepository;
    }

    @PostConstruct
    @Transactional(readOnly = true)
    public void init() {
        reindexAllCards();
    }

    @Transactional(readOnly = true)
    public int reindexAllCards() {
        logger.info("VectorStore: starting card reindex");

        deleteExistingCardDocuments();

        List<Document> batch = new ArrayList<>(BATCH_SIZE);
        int processed = 0;

        for (YughioCard card : cardRepository.findAll()) {
            batch.add(toDocument(card));
            processed++;

            if (batch.size() >= BATCH_SIZE) {
                vectorStore.add(List.copyOf(batch));
                logger.info("VectorStore: indexed {} cards so far", processed);
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            vectorStore.add(List.copyOf(batch));
        }

        logger.info("VectorStore: completed card reindex, total indexed={}", processed);
        return processed;
    }

    private void deleteExistingCardDocuments() {
        try {
            Filter.Expression deleteCardsFilter = new Filter.Expression(
                    Filter.ExpressionType.EQ,
                    new Filter.Key("source"),
                    new Filter.Value(DOCUMENT_SOURCE)
            );

            vectorStore.delete(deleteCardsFilter);
            logger.info("VectorStore: deleted previously indexed card documents");
        } catch (Exception e) {
            logger.warn("VectorStore: failed to delete existing card documents before reindex", e);
        }
    }

    private Document toDocument(YughioCard card) {
        return new Document(
                buildDocumentContent(
                        card.getId(),
                        card.getName(),
                        card.getDescription(),
                        card.getQuantity()
                ),
                Map.of(
                        "source", DOCUMENT_SOURCE,
                        "cardId", safeValue(card.getId()),
                        "name", safeValue(card.getName()),
                        "quantity", card.getQuantity()
                )
        );
    }

    private String buildDocumentContent(Object id, String name, String description, int quantity) {
        return String.format(
                "id: %s, name: %s, description: %s, quantity: %d",
                safeValue(id),
                safeValue(name),
                safeValue(description),
                quantity
        );
    }

    private String safeValue(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}
