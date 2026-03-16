package com.cal.yughistore.repository.card;

import com.cal.yughistore.model.yughiocard.CardSet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardSetRepository extends JpaRepository<CardSet, Long> {
    @Transactional
    void deleteByYughioCard_Id(Long cardId);
}