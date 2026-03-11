package com.cal.yughistore.repository.card;

import com.cal.yughistore.model.yughiocard.properties.CardProperties;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardPropertiesRepository extends JpaRepository<CardProperties, Long> {
    @Transactional
    void deleteByYughioCard_Id(Long cardId);
}
