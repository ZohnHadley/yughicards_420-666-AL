package com.cal.yughistore.repository;

import com.cal.yughistore.model.SpellCard;
import com.cal.yughistore.model.enums.EnumCardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpellCardRepository extends JpaRepository<SpellCard, Long> {

    SpellCard getTrapCardsById(Long id);

    SpellCard getTrapCardsByName(String name);
}
