package com.cal.yughistore.services;
import com.cal.yughistore.model.SpellCard;
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

    public SpellCard getById(Long id){
        if(id == null || id == -1){
            throw new RuntimeException("card id cannot be blank");
        }
        return repository.getTrapCardsById(id);
    }

    public SpellCard getByName(String name){
        if(name.isBlank()){
            throw new RuntimeException("card name cannot be blank");
        }
        return repository.getTrapCardsByName(name);
    }
}
