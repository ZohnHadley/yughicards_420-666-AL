package com.cal.yughistore.services.cardServices;

import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.model.SpellCard;
import com.cal.yughistore.repository.SpellCardRepository;
import com.cal.yughistore.services.DTOs.DTOSpellCard;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class SpellCardService {
    private SpellCardRepository repository;

    public void save(DTOSpellCard card){
        if (card == null){
            throw new RuntimeException("card can't be null");
        }
        repository.save(card.toEntity());
    }
}
