package com.cal.yughistore.repository;

import com.cal.yughistore.model.yughiocard.CardSets;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardSetRepository extends JpaRepository<CardSets, Long> {
    CardSets getById(Long id);
}