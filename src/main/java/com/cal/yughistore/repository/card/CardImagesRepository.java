package com.cal.yughistore.repository.card;

import com.cal.yughistore.model.yughiocard.CardImages;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardImagesRepository extends JpaRepository<CardImages, Long> {
    @Transactional
    void deleteByYughioCard_Id(Long cardId);
}
