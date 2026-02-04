package com.cal.yughistore.services.cardServices;
import com.cal.yughistore.repository.SpellCardRepository;
import com.cal.yughistore.services.DTOs.DTOSpellCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SpellCardService {
    private static final Logger logger = LoggerFactory.getLogger(SpellCardService.class);

    private SpellCardRepository repository;

    public void save(DTOSpellCard card){
        if (card == null){
            throw new RuntimeException("card can't be null");
        }
        repository.save(card.toEntity());
        logger.info("MonsterCardService : saved spell card {}", card.getName());
    }
}
