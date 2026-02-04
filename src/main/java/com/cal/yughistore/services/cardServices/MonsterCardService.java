package com.cal.yughistore.services.cardServices;
import com.cal.yughistore.repository.MonsterCardRepository;
import com.cal.yughistore.services.DTOs.DTOMonsterCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MonsterCardService {
    private static final Logger logger = LoggerFactory.getLogger(MonsterCardService.class);

    private MonsterCardRepository repository;

    public void save(DTOMonsterCard card){
        if (card == null){
            throw new RuntimeException("card can't be null");
        }
        repository.save(card.toEntity());
        logger.info("MonsterCardService : saved monster card {}", card.getName());
    }
}
