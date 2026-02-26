package com.cal.yughistore.repository;

import com.cal.yughistore.model.yughiocard.CardImages;
import com.cal.yughistore.model.yughiocard.CardSets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardSetsRepository extends JpaRepository<CardSets, Long> {

}
