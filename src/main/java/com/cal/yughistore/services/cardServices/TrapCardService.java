package com.cal.yughistore.services.cardServices;

import com.cal.yughistore.model.SpellCard;
import com.cal.yughistore.model.TrapCard;
import com.cal.yughistore.repository.TrapCardRepository;
import com.cal.yughistore.services.DTOs.DTOTrapCard;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class TrapCardService {
    private TrapCardRepository repository;

    public void save(DTOTrapCard card){
        if (card == null){
            throw new RuntimeException("card can't be null");
        }
        repository.save(card.toEntity());
    }
}
