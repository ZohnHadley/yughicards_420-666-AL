package com.cal.yughistore.service.utils;

import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.repository.card.YughioCardRepository;
import com.cal.yughistore.utils.ConsoleLoadingBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class YughioCardVectorStoreUtil {
    private final ConsoleLoadingBar consoleLoadingBar = new ConsoleLoadingBar();

    private static final Logger logger = LoggerFactory.getLogger(YughioCardVectorStoreUtil.class);
    private static final int BATCH_SIZE = 250;
    private static final String DOCUMENT_SOURCE = "yughio-card";

    private final YughioCardRepository cardRepository;
    private final VectorStore vectorStore;
    private final YughioCardDocumentFactory documentFactory;

    public YughioCardVectorStoreUtil(
            YughioCardRepository cardRepository,
            VectorStore vectorStore,
            YughioCardDocumentFactory documentFactory
    ) {
        this.cardRepository = cardRepository;
        this.vectorStore = vectorStore;
        this.documentFactory = documentFactory;
    }

    @Transactional(readOnly = true)
    public void indexAllCards() {
        logger.info("VectorStore: starting card reindex");
        List<Document> batch = new ArrayList<>(BATCH_SIZE);
        int processed = 0;
        List<YughioCard> cards = cardRepository.findAll();
        for (YughioCard card : cards) {
            batch.add(documentFactory.create(card));

            if (batch.size() >= BATCH_SIZE) {
                vectorStore.add(batch);
                logger.info("VectorStore: indexed {} cards so far", processed);
                batch.clear();
            }

            consoleLoadingBar.printProgress(processed, cards.size());
            processed++;
        }
        consoleLoadingBar.finish("indexing cards");
    }

//    public void deleteExistingCardDocuments() {
//        try {
//            Filter.Expression deleteCardsFilter = new Filter.Expression(
//                    Filter.ExpressionType.EQ,
//                    new Filter.Key("source"),
//                    new Filter.Value(DOCUMENT_SOURCE)
//            );
//
//            vectorStore.delete(deleteCardsFilter);
//            logger.info("VectorStore: deleting previously indexed card documents");
//        } catch (Exception e) {
//            logger.warn("VectorStore: failed to delete existing card documents before reindex", e);
//        }
//    }
}
