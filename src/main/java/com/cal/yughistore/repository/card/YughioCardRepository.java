package com.cal.yughistore.repository.card;

import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.model.yughiocard.enums.EnumCardType;
import com.cal.yughistore.model.yughiocard.enums.EnumFrameType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface YughioCardRepository extends JpaRepository<YughioCard, Long> {

    // Récupérer une carte exacte par nom (ignore case)
    Optional<YughioCard> findByNameIgnoreCase(String name);

    // Récupérer toutes les versions/sets d’une carte par nom (paged)
    Page<YughioCard> findAllByNameIgnoreCaseOrderBySetNameAsc(String name, Pageable pageable);

    // Recherche par nom partiel
    Page<YughioCard> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Recherche par frameType
    Page<YughioCard> findByFrameTypeContainingIgnoreCase(EnumFrameType frameType, Pageable pageable);

    // Recherche par type
    Page<YughioCard> getAllByTypeContainingIgnoreCase(EnumCardType type, Pageable pageable);

    // Récupère toutes les versions (sets et raretés) d'une carte exacte
    List<YughioCard> findAllByNameIgnoreCaseOrderByRarityAsc(String name);


}