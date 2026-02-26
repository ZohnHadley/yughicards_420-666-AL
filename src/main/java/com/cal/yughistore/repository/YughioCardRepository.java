package com.cal.yughistore.repository;

import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.model.yughiocard.enums.EnumCardType;
import com.cal.yughistore.model.yughiocard.enums.EnumFrameType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YughioCardRepository extends JpaRepository<YughioCard, Long> {

    YughioCard getById(Long id);
    YughioCard getByName(String name);
    //search by name
    Page<YughioCard> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<YughioCard> getAllByFrameType(EnumFrameType frameType, Pageable pageable);
    Page<YughioCard> getAllByType(EnumCardType type, Pageable pageable);


    boolean existsYughioCardByApiId(int apiId);
}
