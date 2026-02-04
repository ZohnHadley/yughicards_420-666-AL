package com.cal.yughistore.services.cardServices;

import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.model.SpellCard;
import com.cal.yughistore.repository.SpellCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class SpellCardService {
    private SpellCardRepository repository;

    public void save(SpellCard card){
        if (card == null){
            throw new RuntimeException("card can't be null");
        }
        repository.save(card);
    }
}
