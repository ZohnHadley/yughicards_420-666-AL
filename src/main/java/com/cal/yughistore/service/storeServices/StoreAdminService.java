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

    private YughioCardDTO requireCard(Long cardId) {
        if (cardId == null) {
            throw new EntityIdentifierNullException(YughioCardDTO.class);
        }
        return yughioCardService.getById(cardId);
    }

    @Transactional
    protected YughioCardDTO updateQuantity(YughioCardDTO cardDTO, int quantity) {
        Long cardId = cardDTO != null ? cardDTO.getId() : null;

        try {
            if (cardDTO == null || cardDTO.getId() == null) {
                System.err.println("storeAdminService : updateQuantity card is invalid");
                return cardDTO;
            }
            if (quantity < 0) {
                quantity = 0;
            }

            cardDTO.setQuantity(quantity);
            return yughioCardService.save(cardDTO);
        } catch (Exception e) {
            logger.error("Error updating quantity for card: {}", cardId, e);
            throw e;
        }
    }

    @Transactional()
    public YughioCardDTO setCardStock(Long cardId, int quantity) {
        try {
            if (quantity < 0) {
                quantity = 0;
            }

            logger.debug("Setting stock for card: {}", cardId);
            YughioCardDTO card = requireCard(cardId);
            return updateQuantity(card, quantity);
        } catch (Exception e) {
            logger.error("Error setting stock for card: {}", cardId, e);
            throw e;
        }
    }

    @Transactional()
    public YughioCardDTO incrementCardStock(Long cardId, int quantity) {
        try {
            if (quantity < 0) {
                quantity = 0;
            }

            logger.debug("Increasing stock for card: {}", cardId);

            YughioCardDTO card = requireCard(cardId);
            int newQuantity = card.getQuantity() + quantity;

            return updateQuantity(card, newQuantity);
        } catch (Exception e) {
            logger.error("Error incrementing stock for card: {}", cardId, e);
            throw e;
        }
    }

    @Transactional()
    public YughioCardDTO decrementCardStock(Long cardId, int quantity) {
        try {
            if (quantity < 0) {
                quantity = 0;
            }
            logger.debug("Decreasing stock for card: {}", cardId);
            YughioCardDTO card = requireCard(cardId);
            int newQuantity = card.getQuantity() - quantity;
            return updateQuantity(card, newQuantity);
        } catch (Exception e) {
            logger.error("Error decrementing stock for card: {}", cardId, e);
            throw e;
        }
    }

    //can change card debugrmation
    @Transactional
    public YughioCardDTO updateCardById(Long cardId) {
        try {
            logger.debug("Updating yugio card: {}", cardId);
            YughioCardDTO yughioCardDTO = requireCard(cardId);
            return yughioCardService.save(yughioCardDTO);
        } catch (Exception e) {
            logger.error("Error updating yugio card: {}", cardId, e);
            throw e;
        }
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
