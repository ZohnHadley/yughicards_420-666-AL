package com.cal.yughistore.repository;

import com.cal.yughistore.model.yughiocard.CardImages;
import com.cal.yughistore.model.yughiocard.CardSets;
import com.cal.yughistore.model.yughiocard.enums.EnumCardSetRarity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardSetsRepository extends JpaRepository<CardSets, Long> {


    /// price filtering
    Page<CardSets> getCardSetsBySetPriceIsBetween(Double min, Double max,
                                                  Pageable pageable);

    Page<CardSets> getCardSetsBySetPriceIsLessThan(Double max,
                                                   Pageable pageable);

    Page<CardSets> getCardSetsBySetPriceIsGreaterThan(Double min,
                                                      Pageable pageable);

    /// set rarity filtering
    Page<CardSets> getCardSetsBySetRarity(EnumCardSetRarity setRarity,
                                          Pageable pageable);

}
