package com.cal.yughistore.services.cardServices;

import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.repository.MonsterCardRepository;
import com.cal.yughistore.services.DTOs.DTOMonsterCard;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class MonsterCardService {
    private MonsterCardRepository repository;

    public void save(DTOMonsterCard card){
        if (card == null){
            throw new RuntimeException("card can't be null");
        }
        repository.save(card.toEntity());
    }
}
