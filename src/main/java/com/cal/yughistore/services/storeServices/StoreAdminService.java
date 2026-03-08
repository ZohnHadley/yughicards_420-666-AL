package com.cal.yughistore.services.storeServices;

import com.cal.yughistore.services.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.services.yughiocard.YughioCardService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StoreAdminService {
    private static final Logger logger = LoggerFactory.getLogger(
            StoreAdminService.class
    );
    private final YughioCardService yughioCardService;

    public StoreAdminService(YughioCardService yughioCardService) {
        this.yughioCardService = yughioCardService;
    }

    @Transactional()
    public YughioCardDTO setCardStock(YughioCardDTO yughioCardDTO, int quantity) {
        if (yughioCardDTO == null) {
            return null;
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        logger.debug("Setting stock for card: {}", yughioCardDTO.getId());
        yughioCardDTO.setStock(quantity);
        return yughioCardService.save(yughioCardDTO);
    }

    @Transactional()
    public YughioCardDTO setCardStock(Long cardId, int quantity) {
        if (cardId == null) {
            return null;
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        logger.debug("Setting stock for card: {}", cardId);
        YughioCardDTO card = yughioCardService.getById(cardId);
        card.setStock(quantity);
        return yughioCardService.save(card);
    }

    @Transactional()
    public YughioCardDTO incrementCardStock(Long cardId, int quantity) {
        if (cardId == null) {
            return null;
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        logger.debug("Increasing stock for card: {}", cardId);
        YughioCardDTO card = yughioCardService.getById(cardId);
        card.setStock(card.getStock() + quantity);
        return yughioCardService.save(card);
    }

    @Transactional()
    public YughioCardDTO decrementCardStock(Long cardId, int quantity) {
        if (cardId == null) {
            return null;
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        logger.debug("Decreasing stock for card: {}", cardId);
        YughioCardDTO card = yughioCardService.getById(cardId);
        card.setStock(card.getStock() - quantity);
        return yughioCardService.save(card);
    }

    //can change card debugrmation
    @Transactional
    public YughioCardDTO updateCardById(Long cardId) {
        if (cardId == null) {
            return null;
        }
        logger.debug("Updating yugio card: {}", cardId);
        YughioCardDTO yughioCardDTO = yughioCardService.getById(cardId);
        return yughioCardService.save(yughioCardDTO);
    }

    //deletes card presence from store
    @Transactional
    public Boolean deleteCardById(Long cardId) {
        if (cardId == null) {
            return null;
        }
        logger.debug("Deleting yugio card: {}", cardId);
        return yughioCardService.deleteById(cardId);
    }
}
