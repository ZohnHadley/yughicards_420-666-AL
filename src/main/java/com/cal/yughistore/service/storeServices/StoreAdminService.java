package com.cal.yughistore.service.storeServices;

import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.YughioCardService;
import com.cal.yughistore.service.exception.EntityDTONullException;
import com.cal.yughistore.service.exception.EntityIdentifierNullException;
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

    @Transactional
    protected YughioCardDTO updateQuantity(YughioCardDTO cardDTO, int quantity) {
        if (cardDTO == null || cardDTO.getId() == null) {
            throw new EntityDTONullException(YughioCardDTO.class);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than or equal to 0");
        }

        cardDTO.setQuantity(quantity);
        return yughioCardService.save(cardDTO);
    }

    @Transactional()
    public YughioCardDTO setCardStock(Long cardId, int quantity) {
        if (cardId == null) {
            throw new EntityIdentifierNullException(YughioCardDTO.class);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        logger.debug("Setting stock for card: {}", cardId);
        YughioCardDTO card = yughioCardService.getById(cardId);
        return updateQuantity(card, quantity);
    }

    @Transactional()
    public YughioCardDTO incrementCardStock(Long cardId, int quantity) {
        if (cardId == null) {
            throw new EntityIdentifierNullException(YughioCardDTO.class);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        logger.debug("Increasing stock for card: {}", cardId);

        YughioCardDTO card = yughioCardService.getById(cardId);
        int newQuantity = card.getQuantity() + quantity;

        return updateQuantity(card, newQuantity);
    }

    @Transactional()
    public YughioCardDTO decrementCardStock(Long cardId, int quantity) {
        if (cardId == null) {
            throw new EntityIdentifierNullException(YughioCardDTO.class);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        logger.debug("Decreasing stock for card: {}", cardId);
        YughioCardDTO card = yughioCardService.getById(cardId);
        int newQuantity = card.getQuantity() - quantity;
        return updateQuantity(card, newQuantity);
    }

    //can change card debugrmation
    @Transactional
    public YughioCardDTO updateCardById(Long cardId) {
        if (cardId == null) {
            throw new EntityIdentifierNullException(YughioCardDTO.class);
        }
        logger.debug("Updating yugio card: {}", cardId);
        YughioCardDTO yughioCardDTO = yughioCardService.getById(cardId);
        return yughioCardService.save(yughioCardDTO);
    }

    //deletes card presence from store
    @Transactional
    public Boolean deleteCardById(Long cardId) {
        if (cardId == null) {
            throw new EntityIdentifierNullException(YughioCardDTO.class);
        }
        logger.debug("Deleting yugio card: {}", cardId);
        return yughioCardService.deleteById(cardId);
    }
}
