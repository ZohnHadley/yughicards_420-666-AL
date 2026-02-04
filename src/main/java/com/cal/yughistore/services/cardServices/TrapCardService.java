package com.cal.yughistore.services.cardServices;
import com.cal.yughistore.repository.TrapCardRepository;
import com.cal.yughistore.services.DTOs.DTOTrapCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TrapCardService {
    private static final Logger logger = LoggerFactory.getLogger(TrapCardService.class);

    private TrapCardRepository repository;

    public void save(DTOTrapCard card){
        if (card == null){
            throw new RuntimeException("card can't be null");
        }
        repository.save(card.toEntity());
        logger.info("MonsterCardService : saved trap card {}", card.getName());
    }
}
