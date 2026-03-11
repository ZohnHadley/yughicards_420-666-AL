package com.cal.yughistore.repository.card;

import com.cal.yughistore.model.yughiocard.CardPrices;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPricesRepository extends JpaRepository<CardPrices, Long> {
    @Transactional
    void deleteByYughioCard_Id(Long cardId);
}
