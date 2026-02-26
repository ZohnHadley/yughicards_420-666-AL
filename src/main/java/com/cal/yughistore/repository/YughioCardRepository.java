package com.cal.yughistore.repository;

import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.model.yughiocard.enums.EnumCardType;
import com.cal.yughistore.model.yughiocard.enums.EnumFrameType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface YughioCardRepository extends JpaRepository<YughioCard, Long>, JpaSpecificationExecutor<YughioCard> {

    YughioCard getById(Long id);
    YughioCard getByName(String name);

    // Récupérer une carte exacte par nom (ignore case)
    Optional<YughioCard> findByNameIgnoreCase(String name);

    // Récupérer toutes les versions/sets d’une carte par nom (paged)
    Page<YughioCard> findAllByNameIgnoreCaseOrderBySetNameAsc(String name, Pageable pageable);

    // Recherche par nom partiel
    Page<YughioCard> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<YughioCard> getAllByFrameType(EnumFrameType frameType, Pageable pageable);
    Page<YughioCard> getAllByType(EnumCardType type, Pageable pageable);

    boolean existsYughioCardByApiId(int apiId);

}
