package com.cal.yughistore.services;
import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.repository.MonsterCardRepository;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MonsterCardService {
    private static final Logger logger = LoggerFactory.getLogger(MonsterCardService.class);

    private final MonsterCardRepository repository;

    public MonsterCardService(MonsterCardRepository repository) {
        this.repository = repository;
    }

    public void save(DTOYughioCard card){
        if (card == null){
            throw new RuntimeException("card can't be null");
        }
        repository.save(((MonsterCard)card.toEntity()));
        logger.info("MonsterCardService : saved monster card {}", card.getName());
    }

    public MonsterCard getById(Long id){
        if(id == null || id == -1){
            throw new RuntimeException("card id cannot be blank");
        }
        return repository.getTrapCardsById(id);
    }

    public MonsterCard getByName(String name){
        if(name.isBlank()){
            throw new RuntimeException("card name cannot be blank");
        }
        return repository.getTrapCardsByName(name);
    }
}
